package com.king.dactylology.LoginModule.Items.Login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.king.dactylology.LoginModule.Utils.Code;
import com.king.dactylology.LoginModule.Utils.Dao.Mapper.oauthTokenMapper;
import com.king.dactylology.LoginModule.Utils.Dao.Mapper.tokenMapper;
import com.king.dactylology.LoginModule.Utils.Dao.Mapper.userMapper;
import com.king.dactylology.LoginModule.Utils.Dao.entity.token;
import com.king.dactylology.LoginModule.Utils.Dao.entity.oauthToken;
import com.king.dactylology.LoginModule.Utils.Dao.utils.QuickSql;
import com.king.dactylology.LoginModule.Utils.UtilsItems.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

//第三方登录抽象类，对于不同的第三方平台需要自行实现getTokenAndRefreshToken函数，
//用于获取第三方token和refresh-token
@Component
public abstract class SimpleOauthLogin implements LoginItem {

    @Autowired
    @Qualifier("quickSql")
    QuickSql quickSql;

    @Autowired
    @Qualifier("utils")
    Utils utils;

    //失效时间，token_hour小时后token失效
    private static final int token_hour = 2400;

    //失效时间，refresh_hour小时后refresh_token失效
    private static final int refresh_hour = 24000;


    /**
     *
     * @param id 用户的id，需要提供user、token表中不存在的id
     * @param password  第三方登录中Oauth2.0协议的code
     * @return
     */
    @Override
    public String login(String id, String password) {
        try {
            //通过code获取token和ref-token
            JSONObject jsonObject = this.getTokenAndRefreshToken(password);

            //从json中提取token和refresh-token
            String access_token = jsonObject.getString("access_token");
            String ref_token = jsonObject.getString("refresh_token");

            //成功获取授权,交换token
            if (access_token != null && ref_token != null) {

                //信息入库
                oauthToken to2 = new oauthToken();

                to2.setId(Integer.parseInt(id));
                to2.setOauthname(password);
                to2.setRefreshtoken(ref_token);
                to2.setRefreshtokendeadtime(utils.addDateHour(new Date(),720));
                to2.setToken(access_token);
                to2.setTokendeadtime(new Date());
                quickSql.insertOauthToken(to2);

                //添加token
                String tokenStr = utils.createToken(access_token + (new Date().toString()));
                //生成ref-token
                String refTokenStr = utils.createRefreshToken(tokenStr);
                token token1 = new token();
                token1.setId(to2.getId());
                token1.setToken(tokenStr);
                token1.setTokendeadtime(utils.addDateHour(new Date(),token_hour));
                token1.setRefreshtokendeadtime(utils.addDateHour(new Date(),refresh_hour));
                token1.setTokengettime(new Date());
                token1.setRefreshtoken(refTokenStr);
                quickSql.insertToken(token1);

                return Code.LoginSuccess.getCode();
            } else {
                return Code.OAuth2loginError.getCode();
            }
        } catch (Exception e) {
            return Code.OAuth2loginError.getCode();
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

    /**
     * 获取含有token和refresh-token的JSON返回值，需要子类实现
     * @param code
     * @return
     */
    abstract JSONObject getTokenAndRefreshToken(String code);

}
