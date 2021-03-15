package com.king.dactylology.LoginModule.Utils.Dao.Mapper;

import com.king.dactylology.LoginModule.Utils.Dao.entity.user;

public interface userMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(user record);

    int insertSelective(user record);

    user selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(user record);

    int updateByPrimaryKey(user record);

    user selectByPhoneNumber(String phoneNumber);

    Integer userMaxId();
}