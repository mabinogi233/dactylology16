package com.king.dactylology.fileUpLoad;


import com.alibaba.fastjson.JSONObject;
import com.king.dactylology.LoginModule.MainService;
import com.king.dactylology.LoginModule.Utils.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 提供文件上传相关的服务，不同用户的文件存于不同的用户空间，用户空间路径为 存储空间路径\用户id
 */
@Service
public class FileServices {

    //登陆服务器
    @Autowired
    private MainService loginService;

    //文件上传功能的实际实现
    @Autowired
    @Qualifier("upLoad")
    UpLoad upLoad;

    /**
     * 删除token对应的用户的全部文件
     * @param token
     */
    public void deleteAllFilesByToken(String token){
        if(loginService.checkToken(token)) {
            String uid = loginService.getIdByToken(token);
            if(uid!=null) {
                upLoad.deleteAllFilesById(uid);
            }
        }
    }

    /**
     * 上传文件
     * @param file
     * @param token
     * @param fileName 文件名（含后缀）
     * @return 返回JSON字符串，仅有code键
     */
    public String uploadFile(MultipartFile file, String token, String fileName){
        //验证Token
        if(loginService.checkToken(token)) {
            String uid = loginService.getIdByToken(token);
            if(uid!=null) {
                return JSONObject.toJSONString(upLoad.uploadFile(file, uid, fileName));
            }
            Map<String,String> rMap = new HashMap<>();
            rMap.put("code", Code.TokenError.getCode());
            return JSONObject.toJSONString(rMap);
        }
        Map<String,String> rMap = new HashMap<>();
        rMap.put("code", Code.TokenError.getCode());
        return JSONObject.toJSONString(rMap);
    }

    /**
     * 获取token对应的用户的全部文件
     * @param token
     * @return
     */
    public File[] getAllFilesById(String token){
        if(loginService.checkToken(token)) {
            String uid = loginService.getIdByToken(token);
            if (uid != null) {
                return upLoad.getAllFilesById(uid);
            }
        }
        return null;
    }


    /**
     * 获取token对应的用户的空间目录的路径
     * @param token
     * @return
     */
    public String getRootPathById(String token){
        if(loginService.checkToken(token)) {
            String uid = loginService.getIdByToken(token);
            if (uid != null) {
                return upLoad.getRootPathById(uid);
            }
        }
        return null;
    }

}
