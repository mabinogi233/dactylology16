package com.king.dactylology.Controller;


import com.alibaba.fastjson.JSONObject;
import com.king.dactylology.LoginModule.Utils.Code;
import com.king.dactylology.fileUpLoad.FileServices;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/fileService")
public class FileController {

    //文件上传服务器
    @Autowired
    @Qualifier("fileServices")
    FileServices fileServices;


    /**
     * 上传文件
     * @param file muiltpart类型文件
     * @param token token
     * @param fileName 文件名带后缀
     * @return json字符串，仅有code键，code的含义见com.king.dactylology.LoginModule.Utils.Code
     */
    @ResponseBody
    @RequestMapping("/uploadFile")
    public String upFile(@RequestParam("file") MultipartFile file, @RequestParam("token") String token, @RequestParam("fileName") String fileName){
        try {
            return fileServices.uploadFile(file, token, fileName);
        }catch (Exception e){
            Map<String,String> rMap = new HashMap<>();
            rMap.put("code", Code.ServiceError.getCode());
            return JSONObject.toJSONString(rMap);
        }
    }

    /**
     * 上传文件，参数同上
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/uploadFileNew")
    public String upFileNew(HttpServletRequest request){
        System.out.println("上传文件");
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("file");
            String token = multipartRequest.getParameter("token");
            String fileName = multipartRequest.getParameter("fileName");
            System.out.println("开始传输文件");
            return fileServices.uploadFile(file, token, fileName);
        }catch (Exception e){
            Map<String,String> rMap = new HashMap<>();
            rMap.put("code", Code.ServiceError.getCode());
            System.out.println("传输文件异常");
            return JSONObject.toJSONString(rMap);
        }
    }


    //进度获取
    /**
     * 无输入，获取上传进度，（百分比），返回0~100之间
     * @param request
     * @return JSON字符串，仅有percent键，其值为上传进度
     */
    @RequestMapping(value = "/fileUploadStatus",method = RequestMethod.POST)
    @ResponseBody
    public String getStatus(HttpServletRequest request){
        double uploadPercent = 0;
        if(request.getSession().getAttribute("uploadPercent")!=null){
            uploadPercent = (double) request.getSession().getAttribute("uploadPercent");
        }
        Map<String,String> rMap = new HashMap<>();
        rMap.put("percent",String.valueOf(uploadPercent));
        return JSONObject.toJSONString(rMap);
    }

}
