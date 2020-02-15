package com.demo.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import com.demo.common.properties.DruidProperties;
import com.demo.common.properties.JdbcProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * DataSourceAutoConfiguration
 *
 * @author gc
 */
@Configuration
@EnableConfigurationProperties({DruidProperties.class, JdbcProperties.class})
@AutoConfigureBefore({org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class})
public class DataSourceAutoConfiguration {

    private final JdbcProperties jdbcProperties;
    private final DruidProperties druidProperties;

    public DataSourceAutoConfiguration(JdbcProperties jdbcProperties, DruidProperties druidProperties) {
        this.druidProperties = druidProperties;
        this.jdbcProperties = jdbcProperties;
    }

    @Autowired
    private Environment environment;

    @Bean
    @ConditionalOnMissingBean({DataSource.class})
    public DruidDataSource createDruidDataSource() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();

        // docker environment
        String jdbcDriver = environment.getProperty("jdbc.driver");
        String jdbcUrl = environment.getProperty("jdbc.url");
        String jdbcUsername = environment.getProperty("jdbc.username");
        String jdbcPassword = environment.getProperty("jdbc.password");

        if (StringUtils.isNotBlank(jdbcDriver)) {
            dataSource.setDriverClassName(jdbcDriver);
            dataSource.setUrl(jdbcUrl);
            dataSource.setUsername(jdbcUsername);
            dataSource.setPassword(jdbcPassword);
        } else {
            dataSource.setDriverClassName(jdbcProperties.getDriver());
            dataSource.setUrl(jdbcProperties.getUrl());
            dataSource.setUsername(jdbcProperties.getUsername());
            dataSource.setPassword(jdbcProperties.getPassword());
        }

        // jdbc
        dataSource.setInitialSize(druidProperties.getInit());
        dataSource.setMinIdle(druidProperties.getMinIdle());
        dataSource.setMaxActive(druidProperties.getMaxActive());
        dataSource.setMaxWait(druidProperties.getMaxWait());
        dataSource.setTestOnBorrow(druidProperties.isTestOnBorrow());
        dataSource.setTestOnReturn(druidProperties.isTestOnReturn());
        dataSource.setTimeBetweenEvictionRunsMillis(druidProperties.getTimeBetweenEvictionRunsMillis());
        dataSource.setMinEvictableIdleTimeMillis(druidProperties.getMinEvictableIdleTimeMillis());
        dataSource.setMaxEvictableIdleTimeMillis(druidProperties.getMaxEvictableIdleTimeMillis());
        dataSource.setRemoveAbandoned(druidProperties.isRemoveAbandoned());
        dataSource.setRemoveAbandonedTimeout(druidProperties.getRemoveAbandonedTimeout());

        dataSource.setValidationQuery(jdbcProperties.getTestSql());

        // SQL Stat View JSON API
        dataSource.setFilters("stat");

        return dataSource;
    }

}
