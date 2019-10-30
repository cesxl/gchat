package com.demo.autoconfigure;

import com.demo.modules.config.ErrorPageConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * WebMvcAutoConfiguration
 *
 * @author gc
 */
@Configuration
public class WebMvcAutoConfiguration implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebMvcAutoConfiguration.class);

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /*
     * 注册静态文件的自定义映射路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(31536000);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer
                // 是否是后缀模式匹配，如“/user”是否匹配/user.*
                .setUseSuffixPatternMatch(true)
                // 是否自动后缀路径模式匹配，如“/user”是否匹配“/user/”
                .setUseTrailingSlashMatch(true);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        //是否通过请求Url的扩展名来决定media type
        configurer.favorPathExtension(true);
        // 不检查Accept请求头
        configurer.ignoreAcceptHeader(true);
        configurer.favorParameter(false);
        configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
        configurer.mediaType("json", MediaType.APPLICATION_JSON_UTF8);
        configurer.mediaType("xml", MediaType.APPLICATION_XML);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        stringHttpMessageConverter.setWriteAcceptCharset(false);
        converters.add(stringHttpMessageConverter);
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new ResourceRegionHttpMessageConverter());
        converters.add(new AllEncompassingFormHttpMessageConverter());
        converters.add(new MappingJackson2XmlHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/index");
        registry.addViewController("/home").setViewName("/modules/home");
        registry.addViewController("/404").setViewName("/error/404");
        registry.addViewController("/500").setViewName("/error/500");
        registry.addViewController("/401").setViewName("/error/401");
        registry.addViewController("/403").setViewName("/error/403");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("Authorization")
                .allowCredentials(false)
                .maxAge(3600);
    }

    @Bean
    @ConditionalOnMissingBean(name = {"requestContextListener"})
    public ServletListenerRegistrationBean<RequestContextListener> requestContextListener() {
        ServletListenerRegistrationBean slrb = new ServletListenerRegistrationBean();
        slrb.setListener(new RequestContextListener());
        return slrb;
    }

    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return new ErrorPageConfig();
    }

}
