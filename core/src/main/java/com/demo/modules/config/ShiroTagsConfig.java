package com.demo.modules.config;

import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;

/**
 * shiro tags
 *
 * @author gc
 */
@Component
public class ShiroTagsConfig {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @PostConstruct
    public void setSharedVariable() {
        Configuration fmConfig = freeMarkerConfigurer.getConfiguration();
        fmConfig.setSharedVariable("shiro", new ShiroTags());
    }
}