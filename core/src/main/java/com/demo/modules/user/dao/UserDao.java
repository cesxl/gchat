package com.demo.modules.user.dao;

import com.demo.common.annotation.MyBatisDao;
import com.demo.modules.user.entity.User;
import org.apache.ibatis.annotations.Param;

@MyBatisDao
public interface UserDao {

    User getUserByLoginName(@Param("loginName") String loginName);

}
