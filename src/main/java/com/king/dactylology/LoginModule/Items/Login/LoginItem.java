package com.king.dactylology.LoginModule.Items.Login;

import org.springframework.stereotype.Component;

@Component
public interface LoginItem {
    /**
     * 登录
     * @param id  用户标识符，确定唯一用户
     * @param password  口令
     * @return 返回状态码
     */
    public String login(String id,String password);

    /**
     * 退出登录
     * @param id 用户标识符，确定唯一用户
     * @return 返回状态码
     */
    public String unLogin(String id);
}
