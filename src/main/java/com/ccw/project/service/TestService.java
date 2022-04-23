package com.ccw.project.service;

import com.ccw.project.dao.TestMapper;
import com.ccw.project.entities.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Autowired
    private TestMapper testMapper;

    public Test getInfo(int id){
        return testMapper.selectByPrimaryKey(id);
    }

}
