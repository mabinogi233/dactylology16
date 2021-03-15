package com.king.dactylology.LoginModule.Utils.Dao.Mapper;

import com.king.dactylology.LoginModule.Utils.Dao.entity.token;

public interface tokenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(token record);

    int insertSelective(token record);

    token selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(token record);

    int updateByPrimaryKey(token record);

    token selectByToken(String token);

    Integer MaxId();
}