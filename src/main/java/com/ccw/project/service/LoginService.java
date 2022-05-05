package com.ccw.project.service;

import com.ccw.project.dao.UserMapper;
import com.ccw.project.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserMapper userMapper;

    /**
     * Check if the username is already exist
     * @return true if not exist
     */
    public boolean checkUserExist(String username){

        int num = userMapper.selectByUserName(username);

        if (num == 1){
            return false;
        }

        return true;
    }

    /**
     * Check if the email is already exist
     * @return true if not exist
     */
    public boolean checkEmailExist(String email){

        int num = userMapper.selectByEmail(email);

        if (num == 1){
            return false;
        }

        return true;
    }

    /**
     * Add the user into database
     * @param user
     * @return
     */
    public int addUser(User user){
        int result = userMapper.insertSelective(user);

        return result;
    }

    /**
     * Check if the password is correct
     * @param userName
     * @param password
     * @return
     */
    public boolean checkPassword(String userName, String password){
        String returnPassword = userMapper.selectRecordByUsername(userName).getPassword();

        if(returnPassword.equals(password)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * Return the user all information
     * @param userName
     * @return
     */
    public User getUserInfor(String userName){
        return userMapper.selectRecordByUsername(userName);
    }
}
