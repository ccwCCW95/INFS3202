package com.ccw.project.dao;

import com.ccw.project.entities.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int selectByUserName(String username);

    int selectByEmail(String email);

    User selectRecordByUsername(String username);

    String getSalt(String username);

    int checkSequs1(User user);

    int checkSequs2(User user);

    int updatePassByUserName(User user);

    int updateImgById(User user);
}