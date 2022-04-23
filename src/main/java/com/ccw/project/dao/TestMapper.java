package com.ccw.project.dao;

import com.ccw.project.entities.Test;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Test record);

    int insertSelective(Test record);

    List<Test> selectTestAll();

    Test selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Test record);

    int updateByPrimaryKey(Test record);
}