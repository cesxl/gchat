package com.demo.autoconfigure;

import com.demo.common.properties.ShiroProperties;
import com.demo.common.shiro.filter.FormAuthenticationFilter;
import com.demo.common.shiro.realm.SystemAuthorizingRealm;
import com.demo.common.utils.FilterChainDefinitionMap;
import com.demo.common.utils.IdGenerate;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.servlet.Filter;
import java.util.Map;

/**
 * ShiroAutoConfiguration
 *
 * @author gc
 */
@Configuration
@EnableConfigurationProperties({ShiroProperties.class})
public class ShiroAutoConfiguration {

    private final ShiroProperties shiroProperties;

    public ShiroAutoConfiguration(ShiroProperties shiroProperties) {
        this.shiroProperties = shiroProperties;
    }

    @Bean(name = "shiroFilterRegistrationBean")
    @ConditionalOnMissingBean
    public FilterRegistrationBean<Filter> shiroFilterProxy(ShiroFilterFactoryBean shiroFilter) throws Exception {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter((Filter) shiroFilter.getObject());
        filterFilterRegistrationBean.addUrlPatterns("/*");
        filterFilterRegistrationBean.setOrder(1);
        return filterFilterRegistrationBean;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
            DefaultWebSecurityManager securityManager, EhCacheManager shiroCacheManager, SessionManager sessionManager) {

        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        bean.setLoginUrl(shiroProperties.getLoginUrl());
        bean.setSuccessUrl(shiroProperties.getSuccessUrl());

        Map<String, Filter> filters = bean.getFilters();
        filters.put("authc", formAuthenticationFilter());
        filters.put("logout", logoutFilter());

        FilterChainDefinitionMap chains = new FilterChainDefinitionMap();
        chains.setFilterChainDefinitions(shiroProperties.getFilterChainDefinitions());
        bean.setFilterChainDefinitionMap(chains.getObject());
        return bean;
    }

    private FormAuthenticationFilter formAuthenticationFilter() {
        FormAuthenticationFilter bean = new FormAuthenticationFilter();
        return bean;
    }

    private LogoutFilter logoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setRedirectUrl(shiroProperties.getLoginUrl());
        return logoutFilter;
    }

    @Bean
    public SystemAuthorizingRealm authorizingRealm(HashedCredentialsMatcher credentialsMatcher) {
        SystemAuthorizingRealm realm = new SystemAuthorizingRealm();
        realm.setCachingEnabled(false);
        realm.setAuthenticationCachingEnabled(false);
        realm.setAuthenticationCacheName("authenticationCache");
        realm.setAuthorizationCachingEnabled(false);
        realm.setAuthorizationCacheName("authorizationCache");
        realm.setCredentialsMatcher(credentialsMatcher);
        return realm;
    }

    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName(shiroProperties.getSession().getSessionIdCookieName());
        simpleCookie.setPath(shiroProperties.getSession().getSessionIdCookiePath());
        simpleCookie.setMaxAge(-1);
        simpleCookie.setHttpOnly(true);
        return simpleCookie;
    }

    @Bean(name = "sessionDAO")
    @DependsOn({"shiroCacheManager"})
    public SessionDAO sessionDAO(EhCacheManager shiroCacheManager) {
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setCacheManager(shiroCacheManager);
        sessionDAO.setActiveSessionsCacheName("activeSessionsCache");
        sessionDAO.setSessionIdGenerator(new IdGenerate());
        return sessionDAO;
    }

    @Bean
    public DefaultWebSessionManager sessionManager(SessionDAO sessionDAO, SimpleCookie sessionIdCookie) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setGlobalSessionTimeout(shiroProperties.getSession().getSessionTimeout());
        sessionManager.setSessionValidationInterval(shiroProperties.getSession().getSessionTimeoutClean());
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookie(sessionIdCookie);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdUrlRewritingEnabled(true);
        return sessionManager;
    }

    @Bean(name = "securityManager")
    @DependsOn({"sessionManager"})
    public DefaultWebSecurityManager securityManager(
            SystemAuthorizingRealm authorizingRealm, SessionManager sessionManager, EhCacheManager shiroCacheManager) {

        DefaultWebSecurityManager bean = new DefaultWebSecurityManager();
        bean.setRealm(authorizingRealm);
        bean.setSessionManager(sessionManager);
        bean.setCacheManager(shiroCacheManager);
        return bean;
    }

    @Bean(value = "cacheManager")
    public EhCacheManagerFactoryBean cacheManager() {
        EhCacheManagerFactoryBean cacheManager = new EhCacheManagerFactoryBean();
        Resource resource = new ClassPathResource("cache/ehcache-local.xml");
        cacheManager.setConfigLocation(resource);
        return cacheManager;
    }

    @Bean(name = "shiroCacheManager")
    @DependsOn({"cacheManager"})
    public EhCacheManager cacheManager(CacheManager cacheManager) {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManager(cacheManager);
        return ehCacheManager;
    }

    @Bean
    public HashedCredentialsMatcher credentialsMatcher(){
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("SHA1");
        matcher.setHashIterations(1024);
        return matcher;
    }

    /**
     * Shiro 生命周期处理器，实现初始化和销毁回调
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * Shiro 过滤器代理配置
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator bean = new DefaultAdvisorAutoProxyCreator();
        bean.setProxyTargetClass(true);
        return bean;
    }

    /**
     * 启用Shrio授权注解拦截方式，AOP式方法级权限检查
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor bean = new AuthorizationAttributeSourceAdvisor();
        bean.setSecurityManager(securityManager);
        return bean;
    }

}