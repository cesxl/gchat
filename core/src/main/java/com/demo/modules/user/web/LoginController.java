package com.demo.modules.user.web;

import com.demo.common.shiro.filter.FormAuthenticationFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录
 *
 * @author gc
 */
@Controller
public class LoginController {

    /**
     * 登录
     */
    @GetMapping(value = "/login")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {

        // 如果已经登录，则跳转到管理首页
        if (SecurityUtils.getSubject().getPrincipal() != null) {
            return "redirect:index";
        }
        return "login";
    }

    /**
     * 登录失败
     */
    @PostMapping(value = "/login")
    public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {

        if (SecurityUtils.getSubject().getPrincipal() != null) {
            return "redirect: index";
        }

        String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
        String message = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);

        if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")) {
            message = "用户或密码错误, 请重试.";
        }

        model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);

        return "login";
    }

    /**
     * 登录成功，进入管理首页
     */
    @RequiresPermissions("user")
    @GetMapping(value = {"/index"})
    public String index(HttpServletRequest request, HttpServletResponse response) {
        return "index";
    }

}
