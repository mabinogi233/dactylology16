package com.king.dactylology.LoginModule.Factorys;

import com.king.dactylology.LoginModule.Items.Register.RegisterItem;
import org.springframework.stereotype.Component;

@Component
public interface RegisterFactoryInterFace {
    /**
     * 根据ID名称获取注册器实例对象
     * @param Iid
     * @return
     */
    public RegisterItem getItem(String Iid);
}
