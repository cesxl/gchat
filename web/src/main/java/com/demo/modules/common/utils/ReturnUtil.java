package com.demo.modules.common.utils;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 */
public class ReturnUtil extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public ReturnUtil() {
        put("code", 0);
        put("success", true);
    }

    public static ReturnUtil error() {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "未知异常，请联系管理员");
    }

    public static ReturnUtil unauthorized() {
        return error(HttpStatus.UNAUTHORIZED.value(), "未经授权");
    }

    public static ReturnUtil error(String msg) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
    }

    public static ReturnUtil error(int code, String msg) {
        ReturnUtil r = new ReturnUtil();
        r.put("code", code);
        r.put("errorMsg", msg);
        r.put("success", false);
        return r;
    }

    public static ReturnUtil ok(String msg) {
        ReturnUtil r = new ReturnUtil();
        r.put("msg", msg);
        return r;
    }

    public static ReturnUtil ok(Map<String, Object> map) {
        ReturnUtil r = new ReturnUtil();
        r.putAll(map);
        return r;
    }

    public static ReturnUtil ok() {
        return new ReturnUtil();
    }

    @Override
    public ReturnUtil put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public ReturnUtil put(Object value) {
        super.put("result", value);
        return this;
    }
}
