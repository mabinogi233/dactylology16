package com.king.dactylology.Controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.king.dactylology.Dactylology.Code;
import com.king.dactylology.Dactylology.runModel;
import com.king.dactylology.LoginModule.MainService;

import com.king.dactylology.ResourceOperator.ResourceService;
import com.king.dactylology.Searcher.entry.Items.Answer;
import com.king.dactylology.Searcher.entry.Items.Question;
import com.king.dactylology.Searcher.workflow.RunMain;
import com.king.dactylology.fileUpLoad.FileServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 手语图片（视频）与手语动作查询
 */
@Controller
@RequestMapping("/dactylology")
public class DactylologyController {


    //登陆服务器
    @Autowired
    @Qualifier("mainService")
    private MainService loginService;

    //查询服务器
    @Autowired
    @Qualifier("runMain")
    private RunMain searchService;

    //上传文件服务器
    @Autowired
    @Qualifier("fileServices")
    private FileServices fileService;

    //网络模型运行
    @Autowired
    @Qualifier("runModel")
    private runModel runService;

    //资源服务器
    @Autowired
    @Qualifier("resourceService")
    private ResourceService resourceService;

    /**
     * 将token对应的用户上传的视频全部翻译为字符串
     * @param token 用户token
     * @return code和JsonArray,每一个元素为一个翻译结果，
     * 有两个键,code的内容见com.king.dactylology.Dactylology.Code
     * 示例：{ code = "200";
     *        items = [{fileName="xxx1";result="yyy1"},{fileName="xxx2";result="yyy2"},……]}
     */
    @RequestMapping("/transfor1")
    @ResponseBody
    public String Vedio2String(@RequestParam("token")String token){

        try {
            //验证token
            if (token != null && loginService.checkToken(token)) {
                //验证成功，获取用户上传的文件
                File[] files = fileService.getAllFilesById(token);
                String rootPath = fileService.getRootPathById(token);
                //创建json
                JSONObject result = new JSONObject();
                List<Map<String, String>> items = new ArrayList<>();
                //对每个文件进行预测
                if (files != null && files.length > 0) {
                    for(File file:files) {
                        //执行具体翻译流程
                        // param： file  视频或图片文件
                        // param： rootPath 用户空间根目录
                        // return ： String 识别到的意义
                        String answer = runService.runPython(file, rootPath);
                        //添加到json
                        Map<String, String> item = new HashMap<>();
                        //Introduction存储名称
                        item.put("fileName", file.getName());
                        //url存储id
                        item.put("result", answer);
                        items.add(item);

                    }
                }
                //添加外层Json
                result.put("code", Code.TransforSuccess.getCode());
                result.put("item", items);
                //翻译完毕，删除用户空间内文件
                fileService.deleteAllFilesByToken(token);
                return result.toJSONString();
            } else {
                //token错误
                Map<String, String> rMap = new HashMap<>();
                rMap.put("code", com.king.dactylology.LoginModule.Utils.Code.TokenError.getCode());
                rMap.put("item", "");
                return JSONObject.toJSONString(rMap);
            }
        }catch (Exception e) {
            e.printStackTrace();
            //出错，返回状态码
            Map<String, String> rMap = new HashMap<>();
            rMap.put("code", Code.TransforError.getCode());
            rMap.put("item", "");
            return JSONObject.toJSONString(rMap);
        }

    }

    /**
     * 查询句子分词后每个词对应的手语动作图或视频
     * @param sentence 句子
     * @param token token
     * @return 每个词对应的手语动作资源的id，可通过id与token访问资源服务器获取实际资源
     * 示例返回：
     * {code = "200" ;
     *  item = [{word="xxx1",result="id1"},{{word="xxx2",result="id2"},……]}
     */

    @RequestMapping("/transfor2")
    @ResponseBody
    public String String2Vedio(@RequestParam("sentence")String sentence,@RequestParam("token") String token){
        try {
            //验证token
            if (token != null && loginService.checkToken(token)) {
                //实际流程
                Question question = new Question();
                question.setSelectSentence(sentence);
                //执行搜索
                List<Answer> answers = searchService.run(question);
                //封装为json
                JSONObject result = new JSONObject();
                List<Map<String,String>> items = new ArrayList<>();
                for(Answer answer:answers){
                    Map<String,String> item = new HashMap<>();
                    //Introduction存储名称
                    item.put("word",answer.getIntroduction());
                    //url存储id
                    item.put("result",answer.getUrl());
                    items.add(item);
                }
                result.put("code",Code.TransforSuccess.getCode());
                result.put("item",items);
                return result.toJSONString();
            } else {
                //token错误
                Map<String, String> rMap = new HashMap<>();
                rMap.put("code", com.king.dactylology.LoginModule.Utils.Code.TokenError.getCode());
                rMap.put("item", "");
                return JSONObject.toJSONString(rMap);
            }
        }catch (Exception e){
            e.printStackTrace();
            //出错，返回状态码
            Map<String, String> rMap = new HashMap<>();
            rMap.put("code", Code.TransforError.getCode());
            rMap.put("item", "");
            return JSONObject.toJSONString(rMap);
        }

    }

    /**
     * 获取全部手语图片/视频资源的id
     * @param token
     * @return JSON字符串
     * 示例：
     * {
     *     code = "200"
     *     item = [1,2,3,4,5,6]
     * }
     *
     */
    @RequestMapping("/transfor3")
    @ResponseBody
    public String getAll(@RequestParam("token") String token){
        try {
            if (token != null && loginService.checkToken(token)) {
                List<Integer> rList = resourceService.getAllResourceId();
                //出错，返回状态码
                Map<String, Object> rMap = new HashMap<>();
                rMap.put("code", Code.TransforSuccess.getCode());
                rMap.put("item", rList);
                return JSONObject.toJSONString(rMap);
            } else {
                //token错误
                Map<String, String> rMap = new HashMap<>();
                rMap.put("code", com.king.dactylology.LoginModule.Utils.Code.TokenError.getCode());
                rMap.put("item", "");
                return JSONObject.toJSONString(rMap);
            }
        }catch (Exception e){
            e.printStackTrace();
            //出错，返回状态码
            Map<String, String> rMap = new HashMap<>();
            rMap.put("code", Code.TransforError.getCode());
            rMap.put("item", "");
            return JSONObject.toJSONString(rMap);
        }
    }

}
