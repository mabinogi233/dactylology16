package com.king.dactylology.LoginModule.Items.GetPassword;

import com.king.dactylology.LoginModule.Utils.Code;
import com.king.dactylology.LoginModule.Utils.Dao.Mapper.tokenMapper;
import com.king.dactylology.LoginModule.Utils.Dao.Mapper.userMapper;
import com.king.dactylology.LoginModule.Utils.Dao.entity.user;
import com.king.dactylology.LoginModule.Utils.Dao.utils.QuickSql;
import com.king.dactylology.LoginModule.Utils.UtilsItems.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
public class SimpleGetPassword implements GetPassWordItem {

    //执行sql语句
    @Autowired
    @Qualifier("quickSql")
    QuickSql quickSql;

    //工具类
    @Autowired
    @Qualifier("utils")
    Utils utils;


    /**
     * 找回密码
     * @param id 唯一用户标识
     * @param checkCode 验证码
     * @param newPassword 新密码
     * @return 返回状态码
     */
    @Override
    public String updatePassWord(String id, String checkCode, String newPassword) {

        if(id==null){
            return Code.NoPhoneNum.getCode();
        }
        if(newPassword==null){
            return Code.NoPassword.getCode();
        }
        if(checkCode==null){
            return Code.NoCheckNum.getCode();
        }
        if(!utils.isPhoneLegal(id)){
            return Code.NotPhoneNum.getCode();
        }

        user updateUser = quickSql.selectUserByPhoneNumber(id);

        if(updateUser==null){
            return Code.NotRegister.getCode();
        }
        if(utils.checkMessage(id,checkCode)){
            //设置新密码
            updateUser.setPassword(newPassword);
            //删除原有登录状态
            if(quickSql.selectTokenByPrimaryKey(updateUser.getId())!=null){
                quickSql.deleteTokenByPrimaryKey(updateUser.getId());
            }
            //更新
            quickSql.updateUserByPrimaryKey(updateUser);
            return Code.updatePasswordSuccess.getCode();
        }else{
            return Code.CheckNumError.getCode();
        }
    }
}
