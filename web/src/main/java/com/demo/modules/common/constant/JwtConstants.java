package com.demo.modules.common.constant;

/**
 * jwt相关配置
 *
 * @author gc
 */
public interface JwtConstants {

    /***
     * 请求头
     */
    String AUTH_HEADER = "Authorization";

    /***
     * 加密
     */
    String SECRET = "GChat@";

    /***
     * 默认登陆路径
     */
    String AUTH_PATH = "/api/login";

}
