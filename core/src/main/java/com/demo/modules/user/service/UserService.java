package com.demo.modules.user.service;

import com.demo.modules.user.dao.UserDao;
import com.demo.modules.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getUserByLoginName(String loginName){
        return userDao.getUserByLoginName(loginName);
    }
}
