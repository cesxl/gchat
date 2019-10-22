package com.demo.modules.user.service;

import com.demo.common.utils.EncodeUtil;
import com.demo.modules.user.dao.UserDao;
import com.demo.modules.user.entity.User;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getUserByLoginName(String loginName) {
        return userDao.getUserByLoginName(loginName);
    }

    public boolean validatePassword(String password, User user) {
        byte[] salt = EncodeUtil.decodeHex(user.getSalt());
        SimpleHash hash = new SimpleHash("SHA1", password, salt, 1024);
        return user.getPassword().equals(hash.toHex());
    }
}
