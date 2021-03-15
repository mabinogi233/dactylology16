package com.king.dactylology.LoginModule.Factorys;

import com.king.dactylology.LoginModule.Items.Register.RegisterItem;
import com.king.dactylology.LoginModule.Utils.UtilsItems.GetBeans;
import com.king.dactylology.LoginModule.Utils.UtilsItems.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class BaseRegisterFactory implements RegisterFactoryInterFace{

    @Autowired
    @Qualifier("utils")
    Utils utils;

    //根据名称返回 注册服务 的实例对象
    @Override
    public RegisterItem getItem(String Iid) {
        if(Iid.equals("base")){
            return (RegisterItem) utils.getBean("simpleRegister");
        }
        return (RegisterItem) utils.getBean("simpleRegister");
    }
}
