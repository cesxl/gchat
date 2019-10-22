package com.demo.modules.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;

/**
 * 定义具体异常页面
 *
 * @author gc
 */
public class ErrorPageConfig implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry errorPageRegistry) {
        ErrorPage page404 = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
        ErrorPage page500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500");
        ErrorPage page401 = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401");
        ErrorPage page403 = new ErrorPage(HttpStatus.FORBIDDEN, "/403");

        errorPageRegistry.addErrorPages(page404, page500, page401, page403);
    }
}