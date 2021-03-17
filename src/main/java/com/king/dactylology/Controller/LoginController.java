package com.king.dactylology.Controller;


import com.king.dactylology.LoginModule.MainService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/loginService")
public class LoginController {

    //登录服务器
    @Autowired
    @Qualifier("mainService")
    MainService mainService;

    /**
     *
     * @param type 登录类型，目前仅有手机号登录，此字段目前需要恒置为1
     * @param id type=1时，id为手机号 type不为1时，id无效
     * @param password type=1时，password为密码，type不为1时，password为oauth2.0中的code
     * @return 返回json字符串，有两个字段：code和token，code为状态码，token仅在code=000时有效
     *
     * update type=2时为验证码登录，此时id为手机号，password为验证码
     *
     * code参数的更多含义见com.king.dactylology.LoginModule.Utils.Code
     */
    @RequestMapping("login")
    @ResponseBody
    public String login(@Param("type")String type,
                        @Param("id")String id,
                        @Param("password")String password){
        return mainService.login(type, id, password);
    }

    /**
     * 注册
     * @param phoneNumber 手机号
     * @param code 验证码
     * @param password 密码
     * @param type 与login相同
     * @return 返回json字符串，只有code参数
     */
    @RequestMapping("register")
    @ResponseBody
    public String register(@Param("phoneNumber")String phoneNumber,@Param("code")String code,
                           @Param("password")String password,@Param("type")String type){
        System.out.println(phoneNumber);
        System.out.println(password);
        System.out.println(code);
        return mainService.register(phoneNumber, code, password, type);
    }

    /**
     *
     * @param phoneNumber 手机号
     * @param newPassword 新密码
     * @param code 验证码
     * @param type 与login相同
     * @return 返回json字符串，只有code参数
     */
    @RequestMapping("getPassword")
    @ResponseBody
    public String getPassword(
            @Param("phoneNumber")String phoneNumber,@Param("newPassword")String newPassword,
            @Param("code")String code,@Param("type")String type){

        return mainService.getPassword(phoneNumber, newPassword, code, type);
    }

    /**
     *
     * @param token token
     * @param type 与login相同
     * @return code
     */
    @RequestMapping("unLogin")
    @ResponseBody
    public String unLogin(@Param("token")String token,@Param("type")String type){
        return mainService.unlogin(token,type);
    }

    /**
     * 注销账号
     * @param phoneNumber 手机号
     * @param password 密码
     * @param type 与login相同
     * @return 返回code
     */
    @RequestMapping("unRegister")
    @ResponseBody
    public String unRegister(
            @Param("phoneNumber")String phoneNumber,@Param("password")String password,@Param("type")String type){
        return mainService.unRegister(phoneNumber, password, type);
    }

    /**
     * 发送验证码短信，
     * @param phoneNumber 手机号，无+86
     * @return 无返回
     */
    @RequestMapping("sendMessage")
    @ResponseBody
    public void sendCheckNum(@Param("phoneNumber") String phoneNumber){
        mainService.sendCheckNum(phoneNumber);
    }
}
