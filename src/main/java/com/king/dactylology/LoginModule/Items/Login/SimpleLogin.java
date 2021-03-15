package com.king.dactylology.LoginModule.Items.Login;

import com.king.dactylology.LoginModule.Utils.Code;
import com.king.dactylology.LoginModule.Utils.Dao.Mapper.tokenMapper;
import com.king.dactylology.LoginModule.Utils.Dao.Mapper.userMapper;
import com.king.dactylology.LoginModule.Utils.Dao.entity.user;
import com.king.dactylology.LoginModule.Utils.Dao.entity.token;
import com.king.dactylology.LoginModule.Utils.Dao.utils.QuickSql;
import com.king.dactylology.LoginModule.Utils.UtilsItems.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;


@Component
public class SimpleLogin implements LoginItem {

    @Autowired
    @Qualifier("utils")
    Utils utils;

    @Autowired
    @Qualifier("quickSql")
    QuickSql quickSql;

    //失效时间，token_hour小时后token失效
    final int token_hour = 2400;

    //失效时间，refresh_hour小时后refresh_token失效
    final int refresh_hour = 24000;


    /**
     * 登录
     * @param id  用户标识符，确定唯一用户
     * @param password  口令
     * @return 返回状态码
     */
    @Override
    public String login(String id, String password) {
        //未输入ID(手机号)
        if (id==null){
            return Code.NoPhoneNum.getCode();
        }
        //未输入密码
        if(password==null){
            return Code.NoPassword.getCode();
        }
        //判断手机号格式
        if(!utils.isPhoneLegal(id)){
            //不是手机号
            return Code.NotPhoneNum.getCode();
        }
        user loginUser = quickSql.selectUserByPhoneNumber(id);
        //手机号未注册
        if(loginUser==null){
            return Code.NotRegister.getCode();
        }
        //密码未设置，请切换登录方式
        if(loginUser.getPassword()==null){
            return Code.NotSetPassword.getCode();
        }

        //登陆成功
        if(loginUser.getPassword().equals(password)){
            //添加token
            String tokenStr = utils.createToken(String.valueOf(loginUser.getPhonenumber())+(new Date().toString()));
            //生成ref-token
            String refTokenStr = utils.createRefreshToken(tokenStr);
            token token1 = new token();
            token1.setId(loginUser.getId());
            token1.setToken(tokenStr);
            token1.setTokendeadtime(utils.addDateHour(new Date(),this.token_hour));
            token1.setRefreshtokendeadtime(utils.addDateHour(new Date(),this.refresh_hour));
            token1.setTokengettime(new Date());
            token1.setRefreshtoken(refTokenStr);
            //此账号已登录
            if (quickSql.selectTokenByPrimaryKey(loginUser.getId())!=null){
                //重新登录
                quickSql.updateTokenByPrimaryKey(token1);
            }else{
                quickSql.insertToken(token1);
            }
            return Code.LoginSuccess.getCode();
        }else{
            //密码错误
            return Code.PasswordError.getCode();
        }

    }

    /**
     * 通过token退出登录
     * @param id 用户标识符，确定唯一用户
     * @return
     */
    @Override
    public String unLogin(String id) {
        if(id!=null) {
            token to = quickSql.selectTokenByToken(id);
            if(to!=null){
                quickSql.deleteTokenByPrimaryKey(to.getId());
                return Code.UnLoginSuccess.getCode();
            }
        }
        return Code.TokenError.getCode();
    }

}
