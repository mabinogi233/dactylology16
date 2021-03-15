package com.king.dactylology.LoginModule.Items.Register;

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
public class SimpleRegister implements RegisterItem {

    @Autowired
    @Qualifier("quickSql")
    QuickSql quickSql;

    @Autowired
    @Qualifier("utils")
    Utils utils;


    /**
     * 注册
     * @param id  用户标识符，确定唯一用户
     * @param password  口令
     * @param code 验证码
     * @return 返回状态码
     */
    @Override
    public String register(String id, String password,String code) {
        if(id==null){
            return Code.NoPhoneNum.getCode();
        }
        if(password==null){
            return Code.NoPassword.getCode();
        }
        if(code==null){
            return Code.NoCheckNum.getCode();
        }
        if(!utils.isPhoneLegal(id)){
            return Code.NotPhoneNum.getCode();
        }
        user registerUser = quickSql.selectUserByPhoneNumber(id);
        if(registerUser!=null){
            //账号已存在
            return Code.HasPhoneNumber.getCode();
        }
        if(utils.checkMessage(id,code)){
            //可以注册
            registerUser = new user();
            int new_id;
            if(quickSql.userMaxId()==null){
                new_id = 0;
            }else {
                new_id = quickSql.userMaxId()+1;
            }

            while(quickSql.selectUserByPrimaryKey(new_id)!=null){
                new_id+=1;
            }
            registerUser.setId(new_id);
            registerUser.setName("用户"+id);
            registerUser.setPassword(password);
            registerUser.setPhonenumber(id);
            quickSql.insertUser(registerUser);
            //注册成功
            return Code.RegisterSuccess.getCode();
        }else{
            //验证码错误
            return Code.CheckNumError.getCode();
        }
    }


    /**
     * 注销账号
     * @param id 用户标识符，确定唯一用户
     * @return 返回状态码
     */
    @Override
    public String unRegister(String id, String password) {
        if(id==null){
            return Code.NoPhoneNum.getCode();
        }
        if(password==null){
            return Code.NoPassword.getCode();
        }
        if(!utils.isPhoneLegal(id)){
            return Code.NotPhoneNum.getCode();
        }
        user registerUser = quickSql.selectUserByPhoneNumber(id);
        if(registerUser==null){
            //账号不存在
            return Code.NotRegister.getCode();
        }else {
            if(registerUser.getPassword().equals(password)){
                //删除账号信息，包含token
                quickSql.deleteUserByPrimaryKey(registerUser.getId());
                quickSql.deleteTokenByPrimaryKey(registerUser.getId());
                //注销成功
                return Code.UnRegisterSuccess.getCode();
            }
            //密码错误
            return Code.PasswordError.getCode();
        }
    }
}
