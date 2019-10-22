package com.demo.modules.api;

import com.demo.modules.common.utils.JwtTokenUtil;
import com.demo.modules.common.utils.ReturnUtil;
import com.demo.modules.user.entity.User;
import com.demo.modules.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登陆
 *
 * @author gc
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/")
@Api(value = "登陆接口", tags = "登陆接口")
public class ApiLoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiLoginController.class);

    @Autowired
    private UserService userService;

    @PostMapping(value = "/api/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "登陆")
    public ReturnUtil login(
            @ApiParam(name = "username", value = "登录名") String username,
            @ApiParam(name = "password", value = "密码") String password) {
        User user = null;
        try {
            //用户名密码登录
            user = userService.getUserByLoginName(username);
            if (user != null) {
                if (!userService.validatePassword(password, user)) {
                    return ReturnUtil.error("用户名或密码错误");
                } else {
                    //设置token识别用户  ......  Authorization: Bearer + token
                    return ReturnUtil.ok().put(user).put("token", JwtTokenUtil.generateToken(String.valueOf(user.getId())));
                }
            } else {
                return ReturnUtil.error("用户名或密码错误");
            }
        } catch (Exception e) {
            LOGGER.error("[api login failed]：", e);
            throw e;
        }
    }

}
