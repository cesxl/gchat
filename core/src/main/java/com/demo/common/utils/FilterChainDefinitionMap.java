package com.demo.common.utils;

import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;

public class FilterChainDefinitionMap implements FactoryBean<Section> {

    private String filterChainDefinitions;

    public Class<?> getObjectType() {
        return this.getClass();
    }

    public boolean isSingleton() {
        return false;
    }

    @Override
    public Section getObject() throws BeansException {
        Ini ini = new Ini();
        ini.load(filterChainDefinitions);
        Section section = ini.getSection("urls");
        if (CollectionUtils.isEmpty(section)) {
            section = ini.getSection("");
        }
        return section;
    }

    public void setFilterChainDefinitions(String filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }

    public FilterChainDefinitionMap() {
    }
}
