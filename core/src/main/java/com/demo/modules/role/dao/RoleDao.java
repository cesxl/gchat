package com.demo.modules.role.dao;

import com.demo.common.annotation.MyBatisDao;
import com.demo.modules.role.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao
public interface RoleDao {

    List<Role> findRoleByUserId(@Param("userId") String userId);

}
