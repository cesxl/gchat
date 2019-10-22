package com.demo.autoconfigure;

import com.demo.common.annotation.MyBatisDao;
import com.demo.common.properties.MybatisProperties;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * MybatisAutoConfiguration
 *
 * @author gc
 */
@Configuration
@EnableConfigurationProperties({MybatisProperties.class})
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
public class MybatisAutoConfiguration {

    private final ResourceLoader resourceLoader;
    private final MybatisProperties properties;

    public MybatisAutoConfiguration(ResourceLoader resourceLoader, MybatisProperties properties) {
        this.resourceLoader = resourceLoader;
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, VendorDatabaseIdProvider databaseIdProvider) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
        factory.setMapperLocations(properties.resolveMapperLocations());
        factory.setTypeAliasesPackage(properties.getTypeAliasesPackage());
        factory.setTypeAliasesSuperType(properties.getTypeAliasesSuperType());
        factory.setDataSource(dataSource);
        factory.setDatabaseIdProvider(databaseIdProvider);
        return factory.getObject();
    }

    @Bean
    public VendorDatabaseIdProvider databaseIdProvider() {
        VendorDatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        databaseIdProvider.setProperties(properties.getDatabaseId());
        return databaseIdProvider;
    }

    @Bean
    public static MapperScannerConfigurer mapperScannerConfigurer() {

        MapperScannerConfigurer msc = new MapperScannerConfigurer();
        msc.setSqlSessionFactoryBeanName("sqlSessionFactory");
        msc.setBasePackage("com.demo");
        msc.setAnnotationClass(MyBatisDao.class);
        return msc;
    }

    @Bean({"transactionManager"})
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
        return dataSourceTransactionManager;
    }

}
