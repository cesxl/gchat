package com.demo.modules.common.interceptor;

import com.demo.modules.common.constant.JwtConstants;
import com.demo.modules.common.utils.JwtTokenUtil;
import com.demo.modules.common.utils.RenderUtil;
import com.demo.modules.common.utils.ReturnUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Rest Api接口鉴权
 *
 * @author gc
 */
@Configuration
public class RestApiInteceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof org.springframework.web.servlet.resource.ResourceHttpRequestHandler) {
            return true;
        }
        return check(request, response);
    }

    private boolean check(HttpServletRequest request, HttpServletResponse response) {
        //放行预检请求OPTIONS 登陆接口 入区登记接口 放行
        if (request.getServletPath().equals(JwtConstants.AUTH_PATH)
                || HttpMethod.OPTIONS.name().equals((request.getMethod()))) {
            return true;
        }
        final String requestHeader = request.getHeader(JwtConstants.AUTH_HEADER);
        String authToken;
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            authToken = requestHeader.substring(7);

            //验证token是否过期,包含了验证jwt是否正确
            try {
                boolean flag = JwtTokenUtil.isTokenExpired(authToken);
                if (flag) {
                    RenderUtil.renderJson(response, ReturnUtil.error(700, "token过期"));
                    return false;
                }
            } catch (JwtException e) {
                //有异常就是token解析失败
                RenderUtil.renderJson(response, ReturnUtil.error(700, "token验证失败"));
                return false;
            }
        } else {
            //header没有带Bearer字段
            RenderUtil.renderJson(response, ReturnUtil.error(700, "token验证失败"));
            return false;
        }
        return true;
    }

}
