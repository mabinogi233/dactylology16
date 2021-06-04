package com.king.dactylology.Controller;


import com.alibaba.fastjson.JSONObject;
import com.king.dactylology.LoginModule.MainService;
import com.king.dactylology.ResourceOperator.Code;
import com.king.dactylology.ResourceOperator.Dao.entity.wordText;
import com.king.dactylology.ResourceOperator.OSS.OSSservice;
import com.king.dactylology.ResourceOperator.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/resourceService")
public class ResourceController {

    //资源服务器
    @Autowired
    @Qualifier("resourceService")
    ResourceService resourceService;

    //OSS资源服务器
    @Autowired
    @Qualifier("OSSservice")
    OSSservice osSservice;

    //登陆服务器，用于验证token
    @Autowired
    @Qualifier("mainService")
    MainService loginService;

    /**
     * 更新资源数据库，后端管理使用
     */
    @RequestMapping("/update")
    @ResponseBody
    public void updateResouce(){
        resourceService.updateResouce();
    }

    /**
     * 更新词意解释数据库，后端管理使用
     */
    @RequestMapping("/updateWordText")
    @ResponseBody
    public void insertWordText(){
        resourceService.insertWordText();
    }

    /**
     * 根据fid下载资源，返回Resource格式资源
     * @param fid
     * @param token
     * @return
     */
    @ResponseBody
    @RequestMapping("/resource1")
    public Resource getAsResource(@RequestParam("fid") String fid,@RequestParam("type")String type,@RequestParam("token")String token) {
        //验证token
        if(!loginService.checkToken(token)){
            return null;
        }
        String filePath = null;
        if(type.equals("pic")) {
            filePath = resourceService.selectResoucePicPathById(Integer.parseInt(fid));
        }
        if(type.equals("mov")){
            filePath = resourceService.selectResouceMovPathById(Integer.parseInt(fid));
        }
        if(filePath==null){
            return null;
        }
        return new FileSystemResource(filePath);
    }


    /**
     * 根据fid下载资源，MIME传输
     * @param response
     * @param fid
     * @param token
     */
    @RequestMapping("/resource2")
    @ResponseBody
    public void getAsByte(HttpServletResponse response, @RequestParam("type")String type,@RequestParam("fid") String fid,@RequestParam("token")String token){
        //验证token
        if(!loginService.checkToken(token)){
            return;
        }

        String filePath = null;
        if(type.equals("pic")) {
            filePath = resourceService.selectResoucePicPathById(Integer.parseInt(fid));
        }
        if(type.equals("mov")){
            filePath = resourceService.selectResouceMovPathById(Integer.parseInt(fid));
        }

        String filename = fid;
        if(filePath==null){
            return;
        }
        File file = new File(filePath);
        try {
            //这行代码主要用于解决文件名为中文是，下载显示文件名乱码
            filename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.reset();
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + filename + "\"");
        try {
            InputStream inStream = new FileInputStream(file);
            OutputStream os = response.getOutputStream();

            byte[] buff = new byte[1024];
            int len = -1;
            while ((len = inStream.read(buff)) > 0) {
                os.write(buff, 0, len);
            }
            os.flush();
            os.close();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取资源url
     * @param fid 资源主键
     * @param token token
     * @return 返回示例：
     * {"code":"300",
     * "urlPic":"http://dactylology.oss-cn-beijing.aliyuncs.com/D%3A%5C%E8%AF%95%E9%AA%8C%E7%94%B0%5Cresources%5Cpic%5C%E6%B5%8B%E8%AF%95.jpg",
     * "pos":"adv",
     * "urlMov":"http://dactylology.oss-cn-beijing.aliyuncs.com/D%3A%5C%E8%AF%95%E9%AA%8C%E7%94%B0%5Cresources%5Cmov%5C%E6%B5%8B%E8%AF%95.mp4",
     * "text":"测试一下而已",
     * "word":"测试"}
     */
    @RequestMapping("/resource3")
    @ResponseBody
    public String getByOSS(@RequestParam("fid") String fid,@RequestParam("token")String token){
        try {
            //验证token
            if (!loginService.checkToken(token)) {
                //出错，返回状态码
                Map<String, String> rMap = new HashMap<>();
                rMap.put("code", com.king.dactylology.LoginModule.Utils.Code.TokenError.getCode());
                rMap.put("urlPic", "");
                rMap.put("word", "");
                rMap.put("urlMov", "");
                rMap.put("text", "");
                rMap.put("pos", "");
                return JSONObject.toJSONString(rMap);
            } else {
                String url1 = osSservice.getUrlPicById(Integer.parseInt(fid));
                String url2 = osSservice.getUrlMovById(Integer.parseInt(fid));
                //成功
                Map<String, String> rMap = new HashMap<>();
                wordText wt = osSservice.getWordText(Integer.parseInt(fid));
                String word = resourceService.selectNameById(Integer.parseInt(fid));
                if (wt != null) {
                    rMap.put("code", Code.ResourceGetSuccess.getCode());
                    rMap.put("urlPic", url1.split("\\?")[0]);
                    rMap.put("urlMov", url2.split("\\?")[0]);
                    rMap.put("word", word);
                    rMap.put("text", wt.getText());
                    rMap.put("pos", wt.getPos());
                }
                return JSONObject.toJSONString(rMap);
            }
        }catch (Exception e){
            e.printStackTrace();
            //出错，返回状态码
            Map<String, String> rMap = new HashMap<>();
            rMap.put("code", Code.ResourceNotFind.getCode());
            rMap.put("urlPic", "");
            rMap.put("urlMov", "");
            rMap.put("word", "");
            rMap.put("text", "");
            rMap.put("pos", "");
            return JSONObject.toJSONString(rMap);
        }
    }

}
