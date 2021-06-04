package com.king.dactylology.Controller;


import com.alibaba.fastjson.JSONObject;
import com.king.dactylology.LoginModule.MainService;
import com.king.dactylology.UserDataBase.Code;
import com.king.dactylology.UserDataBase.UserInfomationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/userInformationService")
public class UserInformationController {


    @Autowired
    @Qualifier("userInfomationService")
    UserInfomationService userInfomationService;

    @Autowired
    @Qualifier("mainService")
    MainService loginService;

    /**
     * 修改头像
     * @param file mutipart头像
     * @param houzhui 头像后缀名eg："png" "jpg"
     * @param token 用户token
     * @return 返回{code="500",url="xxx"}
     */
    @RequestMapping("/updateHeadPic")
    @ResponseBody
    public String updateHeadPic(@RequestParam("file") MultipartFile file,
                                @RequestParam("houzhui") String houzhui,
                                @RequestParam("token")String token){
        try {
            if (loginService.checkToken(token)) {
                String uid = loginService.getIdByToken(token);
                String url = userInfomationService.uploadFile(file,houzhui,Integer.parseInt(uid));

                Map<String, String> rMap = new HashMap<>();
                rMap.put("code", Code.UpdateSuccess.getCode());
                rMap.put("url", url);
                return JSONObject.toJSONString(rMap);
            }else {
                //token错误
                Map<String, String> rMap = new HashMap<>();
                rMap.put("code", com.king.dactylology.LoginModule.Utils.Code.TokenError.getCode());
                rMap.put("url", "");
                return JSONObject.toJSONString(rMap);
            }
        }catch (Exception e){
            e.printStackTrace();
            Map<String, String> rMap = new HashMap<>();
            rMap.put("code", Code.UpdateError.getCode());
            rMap.put("url", "");
            return JSONObject.toJSONString(rMap);
        }
    }

    /**
     * 修改头像，参数同上
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateHeadPicNew")
    public String upHeadPicNew(HttpServletRequest request){
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("file");
            String token = multipartRequest.getParameter("token");
            String houzhui = multipartRequest.getParameter("houzhui");
            if (loginService.checkToken(token)) {
                String uid = loginService.getIdByToken(token);
                String url = userInfomationService.uploadFile(file,houzhui,Integer.parseInt(uid));

                Map<String, String> rMap = new HashMap<>();
                rMap.put("code", Code.UpdateSuccess.getCode());
                rMap.put("url", url);
                return JSONObject.toJSONString(rMap);
            }else {
                //token错误
                Map<String, String> rMap = new HashMap<>();
                rMap.put("code", com.king.dactylology.LoginModule.Utils.Code.TokenError.getCode());
                rMap.put("url", "");
                return JSONObject.toJSONString(rMap);
            }
        }catch (Exception e){
            e.printStackTrace();
            Map<String, String> rMap = new HashMap<>();
            rMap.put("code", Code.UpdateError.getCode());
            rMap.put("url", "");
            return JSONObject.toJSONString(rMap);
        }
    }


    /**
     * 获取头像url
     * @param token 用户token
     * @return 返回{code="500",url="xxx"}
     */
    @RequestMapping("/getHeadPic")
    @ResponseBody
    public String getHeadPic(@RequestParam("token") String token){
        try{
            if (loginService.checkToken(token)) {
                String uid = loginService.getIdByToken(token);
                String url = userInfomationService.getHeadPictureUrl(Integer.parseInt(uid));
                if(url!=null) {
                    Map<String, String> rMap = new HashMap<>();
                    rMap.put("code", Code.UpdateSuccess.getCode());
                    rMap.put("url", url);
                    return JSONObject.toJSONString(rMap);
                }else{
                    Map<String, String> rMap = new HashMap<>();
                    rMap.put("code", Code.GetError.getCode());
                    rMap.put("url", "");
                    return JSONObject.toJSONString(rMap);
                }
            }else {
                //token错误
                Map<String, String> rMap = new HashMap<>();
                rMap.put("code", com.king.dactylology.LoginModule.Utils.Code.TokenError.getCode());
                rMap.put("url", "");
                return JSONObject.toJSONString(rMap);
            }
        }catch (Exception e){
            e.printStackTrace();
            Map<String, String> rMap = new HashMap<>();
            rMap.put("code", Code.GetError.getCode());
            rMap.put("url", "");
            return JSONObject.toJSONString(rMap);
        }
    }

    @RequestMapping("/updateNickName")
    @ResponseBody
    public String updateNickName(@RequestParam("token") String token,@RequestParam("nickName") String nickName){
        try{
            if (loginService.checkToken(token)) {
                String uid = loginService.getIdByToken(token);
                boolean fl = userInfomationService.updateNickName(Integer.parseInt(uid),nickName);
                if(fl) {
                    Map<String, String> rMap = new HashMap<>();
                    rMap.put("code", Code.UpdateSuccess.getCode());
                    rMap.put("nickName", nickName);
                    return JSONObject.toJSONString(rMap);
                }else{
                    Map<String, String> rMap = new HashMap<>();
                    rMap.put("code", Code.UpdateError.getCode());
                    rMap.put("nickName", "");
                    return JSONObject.toJSONString(rMap);
                }
            }else {
                //token错误
                Map<String, String> rMap = new HashMap<>();
                rMap.put("code", com.king.dactylology.LoginModule.Utils.Code.TokenError.getCode());
                rMap.put("nickName", "");
                return JSONObject.toJSONString(rMap);
            }
        }catch (Exception e){
            e.printStackTrace();
            Map<String, String> rMap = new HashMap<>();
            rMap.put("code", Code.UpdateError.getCode());
            rMap.put("nickName", "");
            return JSONObject.toJSONString(rMap);
        }
    }

    @RequestMapping("/getNickName")
    @ResponseBody
    public String getNickName(@RequestParam("token") String token){
        try{
            if (loginService.checkToken(token)) {
                String uid = loginService.getIdByToken(token);
                String nickName = userInfomationService.getNickName(Integer.parseInt(uid));
                if(nickName!=null) {
                    Map<String, String> rMap = new HashMap<>();
                    rMap.put("code", Code.GetSuccess.getCode());
                    rMap.put("nickName", nickName);
                    return JSONObject.toJSONString(rMap);
                }else {
                    Map<String, String> rMap = new HashMap<>();
                    rMap.put("code", Code.GetError.getCode());
                    rMap.put("nickName", "");
                    return JSONObject.toJSONString(rMap);
                }
            }else {
                //token错误
                Map<String, String> rMap = new HashMap<>();
                rMap.put("code", com.king.dactylology.LoginModule.Utils.Code.TokenError.getCode());
                rMap.put("nickName", "");
                return JSONObject.toJSONString(rMap);
            }
        }catch (Exception e){
            e.printStackTrace();
            Map<String, String> rMap = new HashMap<>();
            rMap.put("code", Code.GetError.getCode());
            rMap.put("nickName", "");
            return JSONObject.toJSONString(rMap);
        }
    }



}
