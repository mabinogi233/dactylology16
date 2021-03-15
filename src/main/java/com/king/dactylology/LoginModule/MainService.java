package com.king.dactylology.LoginModule;


import com.alibaba.fastjson.JSONObject;
import com.king.dactylology.LoginModule.Factorys.GetPassWordFactoryInterFace;
import com.king.dactylology.LoginModule.Factorys.LoginFactoryInterFace;
import com.king.dactylology.LoginModule.Factorys.RegisterFactoryInterFace;
import com.king.dactylology.LoginModule.Items.GetPassword.GetPassWordItem;
import com.king.dactylology.LoginModule.Items.Login.LoginItem;
import com.king.dactylology.LoginModule.Items.Register.RegisterItem;
import com.king.dactylology.LoginModule.Utils.Code;
import com.king.dactylology.LoginModule.Utils.Dao.utils.QuickSql;
import com.king.dactylology.LoginModule.Utils.UtilsItems.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

//登陆服务器，提供登录相关的操作
@Service
public class MainService {
    @Autowired
    @Qualifier("baseGetPasswordFactory")
    GetPassWordFactoryInterFace getPassWordFactory;

    @Autowired
    @Qualifier("baseLoginFactory")
    LoginFactoryInterFace loginFactory;

    @Autowired
    @Qualifier("baseRegisterFactory")
    RegisterFactoryInterFace registerFactory;

    @Autowired
    @Qualifier("quickSql")
    QuickSql quickSql;

    @Autowired
    @Qualifier("utils")
    Utils utils;

    /**
     *
     * @param type 登录类型，目前仅有手机号登录，此字段为1
     * @param id type=1时，id为手机号 type不为1时，id无效
     * @param password type=1时，password为密码，type不为1时，password为oauth2.0中的code
     * @return 返回json字符串，有两个字段：code和token，code为状态码，token仅在code=000时有效
     */
    public String login(String type, String id, String password){
        try {
            if (type.equals("1")) {
                LoginItem loginItem = loginFactory.getItem("base");

                //执行登录操作，返回错误码
                String code = loginItem.login(id, password);
                //登录成功
                if (code.equals("000")) {
                    //获取token
                    String token = quickSql.selectTokenById(id, type);
                    Map<String, String> rMap = new HashMap<>();
                    rMap.put("code", code);
                    rMap.put("token", token);
                    return JSONObject.toJSONString(rMap);
                } else {
                    //登录失败
                    Map<String, String> rMap = new HashMap<>();
                    rMap.put("code", code);
                    rMap.put("token", "");
                    return JSONObject.toJSONString(rMap);
                }
            } else {
                LoginItem loginItem = loginFactory.getItem("oauth");
                int newId = quickSql.createId();
                String code = loginItem.login(String.valueOf(newId), password);
                if (code.equals("000")) {
                    //获取token
                    String token = quickSql.selectTokenById(String.valueOf(newId), type);
                    Map<String, String> rMap = new HashMap<>();
                    rMap.put("code", code);
                    rMap.put("token", token);
                    return JSONObject.toJSONString(rMap);
                } else {
                    //登录失败
                    Map<String, String> rMap = new HashMap<>();
                    rMap.put("code", code);
                    rMap.put("token", "");
                    return JSONObject.toJSONString(rMap);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Map<String, String> rMap = new HashMap<>();
            rMap.put("code", Code.ServiceError.getCode());
            rMap.put("token", "");
            return JSONObject.toJSONString(rMap);
        }
    }

    /**
     * 注册
     * @param id 手机号
     * @param code 验证码
     * @param password 密码
     * @param type 与login相同
     * @return 返回json字符串，只有code参数
     */
    public String register(String id,String code,String password,String type){
        try {
            if (type.equals("1")) {
                RegisterItem registerItem = registerFactory.getItem("base");
                String rcode = registerItem.register(id, password, code);
                Map<String, String> rMap = new HashMap<>();
                rMap.put("code", rcode);
                return JSONObject.toJSONString(rMap);
            } else {
                Map<String, String> rMap = new HashMap<>();
                rMap.put("code", Code.RegisterError.getCode());
                return JSONObject.toJSONString(rMap);
            }
        }catch (Exception e){
            e.printStackTrace();
            Map<String, String> rMap = new HashMap<>();
            rMap.put("code", Code.ServiceError.getCode());
            return JSONObject.toJSONString(rMap);
        }
    }

    /**
     *
     * @param id 手机号
     * @param newPassword 新密码
     * @param code 验证码
     * @param type 与login相同
     * @return 返回json字符串，只有code参数
     */
    public String getPassword(String id,String newPassword,String code,String type){
        try {
            if (type.equals("1")) {
                GetPassWordItem getPassWordItem = getPassWordFactory.getItem("base");
                String rcode = getPassWordItem.updatePassWord(id, code, newPassword);
                Map<String, String> rMap = new HashMap<>();
                rMap.put("code", rcode);
                return JSONObject.toJSONString(rMap);
            } else {
                Map<String, String> rMap = new HashMap<>();
                rMap.put("code", Code.updatePasswordError.getCode());
                return JSONObject.toJSONString(rMap);
            }
        }catch (Exception e){
            e.printStackTrace();
            Map<String, String> rMap = new HashMap<>();
            rMap.put("code", Code.ServiceError.getCode());
            return JSONObject.toJSONString(rMap);
        }
    }

    /**
     * id为token
     * @param id token
     * @param type 与login相同
     * @return code
     */
    public String unlogin(String id,String type){
        try {
            if (type.equals("1")) {
                LoginItem loginItem = loginFactory.getItem("base");
                String code = loginItem.unLogin(id);
                Map<String, String> rMap = new HashMap<>();
                rMap.put("code", code);
                return JSONObject.toJSONString(rMap);
            } else {
                LoginItem loginItem = loginFactory.getItem("oauth");
                String code = loginItem.unLogin(id);
                Map<String, String> rMap = new HashMap<>();
                rMap.put("code", code);
                return JSONObject.toJSONString(rMap);
            }
        }catch (Exception e){
            e.printStackTrace();
            Map<String, String> rMap = new HashMap<>();
            rMap.put("code", Code.ServiceError.getCode());
            return JSONObject.toJSONString(rMap);
        }
    }

    /**
     * 注销账号
     * @param id 手机号
     * @param password 密码
     * @param type 与login相同
     * @return 返回code
     */
    public String unRegister(String id,String password,String type){
        try {
            if (type.equals("1")) {
                RegisterItem registerItem = registerFactory.getItem("base");
                String rcode = registerItem.unRegister(id, password);
                Map<String, String> rMap = new HashMap<>();
                rMap.put("code", rcode);
                return JSONObject.toJSONString(rMap);
            } else {
                RegisterItem registerItem = registerFactory.getItem("oauth");
                String rcode = registerItem.unRegister(id, password);
                Map<String, String> rMap = new HashMap<>();
                rMap.put("code", rcode);
                return JSONObject.toJSONString(rMap);
            }
        }catch (Exception e){
            e.printStackTrace();
            Map<String, String> rMap = new HashMap<>();
            rMap.put("code", Code.ServiceError.getCode());
            return JSONObject.toJSONString(rMap);
        }
    }

    /**
     * 发送验证码短信，
     * @param id 手机号，无+86
     * @return 无返回
     */
    public void sendCheckNum(String id){
        utils.sendMessage(id);
    }

    /**
     * 验证token
     * @param token token
     * @return 返回token是否有效
     */
    public boolean checkToken(String token){
        return quickSql.checkToken(token);
    }

    /**
     * 根据token获取用户id
     * @param token
     * @return
     */
    public String getIdByToken(String token){
        return quickSql.getIdByToken(token);
    }
}
