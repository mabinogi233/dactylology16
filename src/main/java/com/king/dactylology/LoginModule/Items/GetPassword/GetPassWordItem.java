package com.king.dactylology.LoginModule.Items.GetPassword;

import org.springframework.stereotype.Component;

@Component
public interface GetPassWordItem {

    /**
     * 找回密码
     * @param id 唯一用户标识
     * @param checkCode 验证码
     * @param newPassword 新密码
     * @return 返回状态码
     */
    public String updatePassWord(String id,String checkCode,String newPassword);
}
