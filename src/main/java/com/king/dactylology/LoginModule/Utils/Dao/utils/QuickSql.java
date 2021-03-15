package com.king.dactylology.LoginModule.Utils.Dao.utils;


import com.king.dactylology.LoginModule.Utils.Dao.Mapper.oauthTokenMapper;
import com.king.dactylology.LoginModule.Utils.Dao.Mapper.tokenMapper;
import com.king.dactylology.LoginModule.Utils.Dao.Mapper.userMapper;
import com.king.dactylology.LoginModule.Utils.Dao.entity.oauthToken;
import com.king.dactylology.LoginModule.Utils.Dao.entity.token;
import com.king.dactylology.LoginModule.Utils.Dao.entity.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * 快速执行sql
 */
@Component
public class QuickSql {
    @Autowired
    tokenMapper myTokenMapper;

    @Autowired
    oauthTokenMapper myOauthTokenMappr;

    @Autowired
    userMapper myUserMapper;

    /**
     * type==1时id为手机号，type不为1时为token的主键
     * @param id
     * @param type
     * @return
     */
    public String selectTokenById(String id,String type){
        if(type.equals("1")){
            int Tid = myUserMapper.selectByPhoneNumber(id).getId();
            return myTokenMapper.selectByPrimaryKey(Tid).getToken();
        }else{
            return myTokenMapper.selectByPrimaryKey(Integer.parseInt(id)).getToken();
        }
    }

    /**
     * 生成与token，user中不重复的id值
     * @return
     */
    public int createId(){
        int userMaxId = 0;
        int tokenMaxId = 0;
        if(myUserMapper.userMaxId()!=null){
            userMaxId = myUserMapper.userMaxId();
        }
        if(myTokenMapper.MaxId()!=null){
            tokenMaxId = myTokenMapper.MaxId();
        }
        int newId = Math.max(userMaxId,tokenMaxId) + 1;
        while(myTokenMapper.selectByPrimaryKey(newId)!=null){
            newId++;
        }
        return newId;
    }

    /**
     * 验证用户token是否有效
     * @param tokenStr
     * @return
     */
    public boolean checkToken(String tokenStr){
        if(tokenStr!=null){
            token to = myTokenMapper.selectByToken(tokenStr);
            if(to!=null){
                if(to.getTokendeadtime().after(new Date())){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 根据手机号查询用户
     * @param phoneNumber
     * @return
     */
    public user selectUserByPhoneNumber(String phoneNumber){
        return myUserMapper.selectByPhoneNumber(phoneNumber);
    }

    /**
     * 更新token
     * @param to1
     */
    public void updateTokenByPrimaryKey(token to1){
        myTokenMapper.updateByPrimaryKey(to1);
    }

    /**
     * 根据主键查询token
     * @param id
     * @return
     */
    public token selectTokenByPrimaryKey(int id){
        return myTokenMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据token字符串查询token对象
     * @param tokenStr
     * @return
     */
    public token selectTokenByToken(String tokenStr){
        return myTokenMapper.selectByToken(tokenStr);
    }

    /**
     * 插入token
     * @param to1
     */
    public void insertToken(token to1){
        myTokenMapper.insert(to1);
    }

    /**
     * 删除token
     * @param id
     */
    public void deleteTokenByPrimaryKey(int id){
        myTokenMapper.deleteByPrimaryKey(id);
    }

    /**
     * 添加第三方token
     * @param otoken
     */
    public void insertOauthToken(oauthToken otoken){
        myOauthTokenMappr.insert(otoken);
    }

    /**
     * 更新用户数据
     * @param user1
     */
    public void updateUserByPrimaryKey(user user1){
        myUserMapper.updateByPrimaryKey(user1);
    }

    /**
     * 返回用户id的最大值
     * @return
     */
    public Integer userMaxId(){
        return myUserMapper.userMaxId();
    }

    /**
     * 根据主键查询用户
     * @param id
     * @return
     */
    public user selectUserByPrimaryKey(int id){
        return myUserMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加用户对象
     * @param user1
     */
    public void insertUser(user user1){
        myUserMapper.insert(user1);
    }

    /**
     * 删除用户对象
     * @param id
     */
    public void deleteUserByPrimaryKey(int id){
        myUserMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据token字符串获取此token对应的用户的id值
     * @param tokenStr
     * @return
     */
    public String getIdByToken(String tokenStr){
        if(tokenStr!=null){
            token to = myTokenMapper.selectByToken(tokenStr);
            if(to!=null){
                return String.valueOf(to.getId());
            }
        }
        return null;
    }
}
