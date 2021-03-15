package com.king.dactylology.ResourceOperator.Dao.Mapper;

import com.king.dactylology.ResourceOperator.Dao.entity.resource;

import java.util.List;

public interface resourceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(resource record);

    int insertSelective(resource record);

    resource selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(resource record);

    int updateByPrimaryKey(resource record);

    List<resource> selectResourceByWord(String word);

    Integer MaxId();

    void deleteAll();

    List<Integer> getAll();

}