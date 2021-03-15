package com.king.dactylology.LoginModule.Factorys;

import com.king.dactylology.LoginModule.Items.GetPassword.GetPassWordItem;
import com.king.dactylology.LoginModule.Utils.UtilsItems.GetBeans;
import com.king.dactylology.LoginModule.Utils.UtilsItems.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class BaseGetPasswordFactory implements GetPassWordFactoryInterFace{

    //工具类，此类中用于获取bean的实例对象
    @Autowired
    @Qualifier("utils")
    Utils utils;

    //根据名称返回 修改密码服务 的实例对象
    @Override
    public GetPassWordItem getItem(String Iid) {
        if(Iid.equals("base")){
            return (GetPassWordItem) utils.getBean("simpleGetPassword");
        }
        return (GetPassWordItem) utils.getBean("simpleGetPassword");
    }
}
