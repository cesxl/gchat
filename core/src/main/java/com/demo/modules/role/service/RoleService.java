package com.demo.modules.role.service;

import com.demo.modules.role.dao.RoleDao;
import com.demo.modules.role.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    public List<Role> findRoleByUserId(String userId){
        return roleDao.findRoleByUserId(userId);
    }

}
