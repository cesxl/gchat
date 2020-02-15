package com.demo.autoconfigure;

import com.demo.common.utils.PropertiesUtils;
import com.google.common.collect.Maps;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.TemplateHashModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * FreeMarkerAutoConfiguration
 *
 * @author gc
 */
@Configuration
@EnableConfigurationProperties(FreeMarkerProperties.class)
@AutoConfigureBefore({org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration.class})
public class FreeMarkerAutoConfiguration {

    private static final Log logger = LogFactory
            .getLog(FreeMarkerAutoConfiguration.class);

    private final ApplicationContext applicationContext;

    private final FreeMarkerProperties properties;

    public FreeMarkerAutoConfiguration(ApplicationContext applicationContext, FreeMarkerProperties properties) {
        this.applicationContext = applicationContext;
        this.properties = properties;
    }

    /**
     * 自定义视图解析器
     *
     * @return
     */
    @Bean
    public FreeMarkerViewResolver freeMarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setOrder(0);
        resolver.setViewClass(org.springframework.web.servlet.view.freemarker.FreeMarkerView.class);
        resolver.setPrefix(properties.getPrefix());
        resolver.setSuffix(properties.getSuffix());
        resolver.setCache(properties.isCache());
        if (properties.getContentType() != null) {
            resolver.setContentType(properties.getContentType().toString());
        }
        resolver.setViewNames(properties.getViewNames());
        resolver.setExposeRequestAttributes(properties.isExposeRequestAttributes());
        resolver.setAllowRequestOverride(properties.isAllowRequestOverride());
        resolver.setAllowSessionOverride(properties.isAllowSessionOverride());
        resolver.setExposeSessionAttributes(properties.isExposeSessionAttributes());
        resolver.setExposeSpringMacroHelpers(properties.isExposeSpringMacroHelpers());
        resolver.setRequestContextAttribute(properties.getRequestContextAttribute());
        resolver.setAttributesMap(this.properties());
        return resolver;
    }

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setTemplateLoaderPaths(properties.getTemplateLoaderPath());
        configurer.setPreferFileSystemAccess(properties.isPreferFileSystemAccess());
        configurer.setDefaultEncoding(properties.getCharsetName());
        Properties settings = new Properties();
        settings.putAll(properties.getSettings());
        configurer.setFreemarkerSettings(settings);
        return configurer;
    }

    /**
     * freemarker.properties
     *
     * @return
     */
    public Map properties() {
        Map<Object, Object> map = Maps.newHashMap();
        Properties properties = PropertiesUtils.readProperties("config", "freemarker.properties");
        Set<String> keys = properties.stringPropertyNames();
        for (String key : keys) {
            map.put(key, staticModel(properties.getProperty(key)));
        }
        return map;
    }

    /**
     * Accessing static methods
     *
     * @param cla
     * @return
     * @see <a href="https://freemarker.apache.org/docs/pgui_misc_beanwrapper.html">beanwrapper</a>
     */
    public TemplateHashModel staticModel(String cla) {
        try {
            BeansWrapper wrapper = new BeansWrapperBuilder(freemarker.template.Configuration.VERSION_2_3_28).build();
            TemplateHashModel staticModels = wrapper.getStaticModels();
            TemplateHashModel fileStatics = (TemplateHashModel) staticModels.get(cla);
            return fileStatics;
        } catch (Exception e) {
            logger.error("[Template BeansWrapper Error]：", e);
        }
        return null;
    }

}
