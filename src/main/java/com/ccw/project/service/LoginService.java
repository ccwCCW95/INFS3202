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

    /**
     * Return the user's salt
     * @param userName
     * @return
     */
    public String getSalt(String userName){
        return userMapper.getSalt(userName);
    }

    /**
     * Check if the secrete question 1 is match
     * @param username
     * @param sequs1
     * @return
     */
    public boolean checkSequs1(String username, String sequs1){
        User user = new User();
        user.setUsername(username);
        user.setSequs1(sequs1);

        int num = userMapper.checkSequs1(user);

        if (num == 1){
            return true;
        }

        return false;
    }

    /**
     * Check if the secrete question 2 is match
     * @param username
     * @param sequs2
     * @return
     */
    public boolean checkSequs2(String username, String sequs2){
        User user = new User();
        user.setUsername(username);
        user.setSequs2(sequs2);

        int num = userMapper.checkSequs2(user);

        if (num == 1){
            return true;
        }

        return false;
    }

    /**
     * Update the password
     * @param username
     * @param password
     * @return
     */
    public boolean updatePassword(String username, String password){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        int num = userMapper.updatePassByUserName(user);

        if (num == 1){
            return true;
        }

        return false;
    }
}
