package com.king.dactylology.LoginModule.Utils.Dao.Mapper;

import com.king.dactylology.LoginModule.Utils.Dao.entity.oauthToken;

public interface oauthTokenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(oauthToken record);

    int insertSelective(oauthToken record);

    oauthToken selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(oauthToken record);

    int updateByPrimaryKey(oauthToken record);

    oauthToken selectByToken(String token);

}