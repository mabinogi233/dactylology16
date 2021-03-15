package com.king.dactylology.LoginModule.Factorys;

import com.king.dactylology.LoginModule.Items.Login.LoginItem;
import org.springframework.stereotype.Component;

@Component
public interface LoginFactoryInterFace {
    /**
     * 根据ID名称获取登录器实例对象
     * @param Iid
     * @return
     */
    public LoginItem getItem(String Iid);
}
