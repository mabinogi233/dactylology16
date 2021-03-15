package com.king.dactylology.LoginModule.Utils.UtilsItems;

import com.king.dactylology.LoginModule.Utils.Dao.Mapper.tokenMapper;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class TokenCreator {

    @Autowired
    tokenMapper mapper;

    /**
     * 使用MD5加密，生成token
     * @param code
     * @return
     */
    String createToken(String code){
        String token = code;
        //采用循环，防止生成俩个相同的token
        do {
            //进行MD5加密
            token = this.EncoderByMd5(token);
        }while (mapper.selectByToken(token)!=null);
        return token;
    }


    /**
     * 使用MD5加密，生成refresh_token
     * @param code
     * @return
     */
    String createRefreshToken(String code){
        String refToken = code;
        //采用循环，防止生成俩个相同的token
        do {
            //进行MD5加密
            refToken = this.EncoderByMd5(refToken);
        }while (mapper.selectByToken(refToken)!=null);
        return refToken;
    }


    /**
     * MD5进行加密
     * @param str  待加密的字符串
     * @return  加密后的字符串
     */
    private String EncoderByMd5(String str){
        try {
            //确定计算方法
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            Base64.Encoder encoder = Base64.getEncoder();
            //加密后的字符串
            return encoder.encodeToString(md5.digest(str.getBytes("utf-8")));
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return null;
    }

}
