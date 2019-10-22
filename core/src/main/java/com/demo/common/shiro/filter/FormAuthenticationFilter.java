package com.demo.common.shiro.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 表单验证
 *
 * @author gc
 */
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

    public static final String DEFAULT_MESSAGE_PARAM = "message";

    private String messageParam = DEFAULT_MESSAGE_PARAM;

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        if (password == null) {
            password = "";
        }
        boolean rememberMe = isRememberMe(request);
        return new UsernamePasswordToken(username, password.toCharArray(), rememberMe);
    }

    public String getMessageParam() {
        return messageParam;
    }

    /**
     * 登录成功之后跳转URL
     */
    @Override
    public String getSuccessUrl() {
        return super.getSuccessUrl();
    }

    /**
     * 登录失败调用事件
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token,
                                     AuthenticationException e, ServletRequest request, ServletResponse response) {
        String className = e.getClass().getName(), message = "";
        if (IncorrectCredentialsException.class.getName().equals(className)
                || UnknownAccountException.class.getName().equals(className)) {
            message = "用户或密码错误, 请重试.";
        } else if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")) {
            message = StringUtils.replace(e.getMessage(), "msg:", "");
        } else {
            message = "系统出现点问题，请稍后再试！";
            e.printStackTrace();
        }
        request.setAttribute(getFailureKeyAttribute(), className);
        request.setAttribute(getMessageParam(), message);
        return true;
    }

}