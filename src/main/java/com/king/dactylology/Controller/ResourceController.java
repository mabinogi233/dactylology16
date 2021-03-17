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
     * 根据fid下载资源，返回Resource格式资源
     * @param fid
     * @param token
     * @return
     */
    @ResponseBody
    @RequestMapping("/resource1")
    public Resource getAsResource(@RequestParam("fid") String fid,@RequestParam("token")String token) {
        //验证token
        if(!loginService.checkToken(token)){
            return null;
        }
        String filePath = resourceService.selectResoucePathById(Integer.parseInt(fid));
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
    public void getAsByte(HttpServletResponse response, @RequestParam("fid") String fid,@RequestParam("token")String token){
        //验证token
        if(!loginService.checkToken(token)){
            return;
        }

        String filePath = resourceService.selectResoucePathById(Integer.parseInt(fid));
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


    @RequestMapping("/resource3")
    @ResponseBody
    public String getByOSS(@RequestParam("fid") String fid,@RequestParam("token")String token){
        try {
            //验证token
            if (!loginService.checkToken(token)) {
                //出错，返回状态码
                Map<String, String> rMap = new HashMap<>();
                rMap.put("code", com.king.dactylology.LoginModule.Utils.Code.TokenError.getCode());
                rMap.put("url", "");
                rMap.put("text", "");
                rMap.put("pos", "");
                return JSONObject.toJSONString(rMap);
            } else {
                String url = osSservice.getUrlById(Integer.parseInt(fid));
                //成功
                Map<String, String> rMap = new HashMap<>();
                wordText wt = osSservice.getWordText(Integer.parseInt(fid));
                if (wt != null) {
                    rMap.put("code", Code.ResourceGetSuccess.getCode());
                    rMap.put("url", url.split("\\?")[0]);
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
            rMap.put("url", "");
            rMap.put("text", "");
            rMap.put("pos", "");
            return JSONObject.toJSONString(rMap);
        }
    }

}
