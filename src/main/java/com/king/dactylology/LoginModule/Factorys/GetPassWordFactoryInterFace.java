package com.king.dactylology.LoginModule.Factorys;

import com.king.dactylology.LoginModule.Items.GetPassword.GetPassWordItem;
import org.springframework.stereotype.Component;

@Component
public interface GetPassWordFactoryInterFace {
    /**
     * 根据ID名称获取重置器实例对象
     * @param Iid
     * @return
     */
    public GetPassWordItem getItem(String Iid);
}
