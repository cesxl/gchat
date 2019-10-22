package com.demo.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for JDBC.
 *
 * @author gc
 */
@ConfigurationProperties(prefix = JdbcProperties.JDBC_PREFIX)
public class JdbcProperties {

    public static final String JDBC_PREFIX = "jdbc";

    private String type;
    private String driver;
    private String url;
    private String username;
    private String password;
    private String testSql;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTestSql() {
        return testSql;
    }

    public void setTestSql(String testSql) {
        this.testSql = testSql;
    }
}
