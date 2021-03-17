package com.king.dactylology.LoginModule.Factorys;

import com.king.dactylology.LoginModule.Items.Login.LoginItem;
import com.king.dactylology.LoginModule.Utils.UtilsItems.GetBeans;
import com.king.dactylology.LoginModule.Utils.UtilsItems.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class BaseLoginFactory implements LoginFactoryInterFace {

    @Autowired
    @Qualifier("utils")
    Utils utils;

    //根据名称返回 登录服务 的实例对象
    @Override
    public LoginItem getItem(String Iid) {
        if(Iid.equals("base")){
            return (LoginItem) utils.getBean("simpleLogin");
        }
        if(Iid.equals("code")){
            return (LoginItem) utils.getBean("codeLogin");
        }
        if(Iid.equals("oauth")){
            return (LoginItem) utils.getBean("simpleOauthLogin");
        }
        return (LoginItem) utils.getBean("simpleLogin");
    }
}
