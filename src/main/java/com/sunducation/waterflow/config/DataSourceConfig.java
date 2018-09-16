package com.sunducation.waterflow.config;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

/**
 * Created by garychen on 17/8/10.
 */
@Configuration
public class DataSourceConfig {

    private Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Value("${spring.datasource.waterflow.jdbc-url}")
    private String dbUrl;

    @Value("${spring.datasource.waterflow.username}")
    private String username;

    @Value("${spring.datasource.waterflow.password}")
    private String password;

    @Value("${spring.datasource.waterflow.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.waterflow.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.waterflow.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.waterflow.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.waterflow.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.waterflow.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.waterflow.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.waterflow.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.waterflow.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.waterflow.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.waterflow.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.datasource.waterflow.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${spring.datasource.waterflow.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;

    @Value("${spring.datasource.waterflow.filters}")
    private String filters;

    @Value("{spring.datasource.waterflow.connectionProperties}")
    private String connectionProperties;


    // 学校DB
    @Bean(name = "waterflowDataSource" ,destroyMethod = "close" ,initMethod = "init")
//    @ConfigurationProperties(prefix = "spring.datasource.rbac") // application.properteis中对应属性的前缀
    public DataSource schoolDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(this.dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);

        //configuration
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
            logger.error("druid configuration initialization filter", e);
        }
        datasource.setConnectionProperties(connectionProperties);

        return datasource;
    }

}