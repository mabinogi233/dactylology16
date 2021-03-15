package com.king.dactylology.LoginModule.Items.Register;

import org.springframework.stereotype.Component;

@Component
public interface RegisterItem {
    /**
     * 注册
     * @param id  用户标识符，确定唯一用户
     * @param password  口令
     * @param code 验证码
     * @return 返回状态码
     */
    public String register(String id,String password,String code);

    /**
     * 注销账号
     * @param id 用户标识符，确定唯一用户
     * @return 返回状态码
     */
    public String unRegister(String id,String password);
}
