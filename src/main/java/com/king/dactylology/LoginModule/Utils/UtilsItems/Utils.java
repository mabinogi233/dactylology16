package com.king.dactylology.LoginModule.Utils.UtilsItems;

import com.alibaba.fastjson.JSONObject;
import com.king.dactylology.LoginModule.Utils.UtilsItems.Check;
import com.king.dactylology.LoginModule.Utils.UtilsItems.DateOpeartor;
import com.king.dactylology.LoginModule.Utils.UtilsItems.PhoneNumberCheck;
import com.king.dactylology.LoginModule.Utils.UtilsItems.TokenCreator;
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
import java.util.Date;
import java.util.regex.PatternSyntaxException;

//工具类接口，用于提供工具方法的调用接口
@Component
public class Utils {
    @Autowired
    @Qualifier("check")
    Check check;

    @Autowired
    @Qualifier("dateOpeartor")
    DateOpeartor dateOpeartor;


    @Autowired
    @Qualifier("phoneNumberCheck")
    PhoneNumberCheck phoneNumberCheck;

    @Autowired
    @Qualifier("tokenCreator")
    TokenCreator tokenCreator;

    /**
     * 发送验证码
     * @param phoneNumber 手机号
     */
    public void sendMessage(String phoneNumber){
        check.sendMessage(phoneNumber);
    }

    /**
     * 验证code
     * @param phoneNumber 手机号
     * @param code 用户输入的验证码
     * @return
     */
    public boolean checkMessage(String phoneNumber,String code){
        return check.checkMessage(phoneNumber,code);
    }

    /**
     * 获取min分钟后的日期
     * @param nowDate
     * @param min
     * @return
     */
    public Date addDateMin(Date nowDate, int min){
        return dateOpeartor.addDateMin(nowDate,min);
    }

    /**
     * 获取hours小时后的日期
     * @param nowDate
     * @param hours
     * @return
     */
    public Date addDateHour(Date nowDate,int hours){
        return dateOpeartor.addDateHour(nowDate, hours);
    }

    /**
     * 获取bean的实例对象
     * @param name
     * @return
     */
    public Object getBean(String name) {
        return GetBeans.getBean(name);
    }

    /**
     * 获取bean的实例对象
     * @param clazz
     * @return
     */
    public Object getBean(Class<?> clazz) {
        return GetBeans.getBean(clazz);
    }

    /**
     * 匹配中国（含香港）手机号
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public boolean isPhoneLegal(String str){
        return phoneNumberCheck.isPhoneLegal(str);
    }

    /**
     * 匹配中国（不含香港）手机号
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public boolean isChinaPhoneLegal(String str){
        return phoneNumberCheck.isChinaPhoneLegal(str);
    }

    /**
     * 匹配香港手机号
     * @param str
     * @return
     */
    boolean isHKPhoneLegal(String str) {
        return phoneNumberCheck.isHKPhoneLegal(str);
    }

    /**
     * 使用MD5加密，获取token
     * @param code
     * @return
     */
    public String createToken(String code){
        return tokenCreator.createToken(code);
    }


    /**
     * 使用MD5加密，获取refresh_token
     * @param code
     * @return
     */
    public String createRefreshToken(String code){
        return tokenCreator.createRefreshToken(code);
    }


    /**
     * 发送get请求至指定URL，并获取JSON格式的反馈
     * @param URL
     * @return
     * @throws IOException
     */
    public JSONObject doGetJson(String URL) throws IOException {
        JSONObject jsonObject = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        BufferedReader br = null;
        StringBuilder result = new StringBuilder();
        try {
            //创建远程url连接对象
            java.net.URL url = new URL(URL);
            //通过远程url连接对象打开一个连接，强转成HTTPURLConnection类
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(60000);
            conn.setRequestProperty("Accept", "application/json");
            //发送请求
            conn.connect();
            //通过conn取得输入流，并使用Reader读取
            if (200 == conn.getResponseCode()) {
                is = conn.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                    System.out.println(line);
                }
            } else {
                System.out.println("ResponseCode is an error code:" + conn.getResponseCode());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            conn.disconnect();
        }
        jsonObject = JSONObject.parseObject(result.toString());
        return jsonObject;
    }

}
