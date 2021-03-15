package com.king.dactylology.Controller;


import com.king.dactylology.LoginModule.MainService;
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

@Controller
@RequestMapping("/resourceService")
public class ResourceController {

    //资源服务器
    @Autowired
    @Qualifier("resourceService")
    ResourceService resourceService;

    //登陆服务器，用于验证token
    @Autowired
    @Qualifier("mainService")
    MainService loginService;

    /**
     * 更新资源数据库，后端管理使用
     */
    @RequestMapping("/update")
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


}
