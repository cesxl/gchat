package com.demo.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Shiro.
 *
 * @author gc
 */
@ConfigurationProperties(prefix = ShiroProperties.SHIRO_PREFIX)
public class ShiroProperties {

    public static final String SHIRO_PREFIX = "shiro";

    private String loginUrl;
    private String logoutUrl;
    private String successUrl;
    private String filterChainDefinitions;
    private SessionProperties session;

    public static class SessionProperties {
        private long sessionTimeout;
        private long sessionTimeoutClean;
        private String sessionIdCookieName;
        private String sessionIdCookiePath;

        public long getSessionTimeout() {
            return sessionTimeout;
        }

        public void setSessionTimeout(long sessionTimeout) {
            this.sessionTimeout = sessionTimeout;
        }

        public long getSessionTimeoutClean() {
            return sessionTimeoutClean;
        }

        public void setSessionTimeoutClean(long sessionTimeoutClean) {
            this.sessionTimeoutClean = sessionTimeoutClean;
        }

        public String getSessionIdCookieName() {
            return sessionIdCookieName;
        }

        public void setSessionIdCookieName(String sessionIdCookieName) {
            this.sessionIdCookieName = sessionIdCookieName;
        }

        public String getSessionIdCookiePath() {
            return sessionIdCookiePath;
        }

        public void setSessionIdCookiePath(String sessionIdCookiePath) {
            this.sessionIdCookiePath = sessionIdCookiePath;
        }
    }

    public SessionProperties getSession() {
        return session;
    }

    public void setSession(SessionProperties session) {
        this.session = session;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getFilterChainDefinitions() {
        return filterChainDefinitions;
    }

    public void setFilterChainDefinitions(String filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }
}
