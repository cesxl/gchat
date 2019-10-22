package com.demo.common.utils;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * 获取 Key 值
 *
 * @author gc
 */
public class Global {

    private static final Properties properties = new Properties();

    static {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:config/application.yml");
        YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
        bean.setResources(resource);
        for (Map.Entry<Object, Object> entry : bean.getObject().entrySet()) {
            properties.put(Objects.toString(entry.getKey()),
                    Objects.toString(entry.getValue()));
        }
    }

    public static String getConfig(String key) {
        String value = (String) properties.get(key);
        return value;
    }

}
