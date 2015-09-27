package com.custom.app.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Created by olga on 27.09.15.
 */
@Configuration
public class TestConfig {
    @Bean(name = "dataSource")
    @Profile(value = "spring-test")
    public DataSource dataSourceForTest() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(org.h2.Driver.class.getName());
        dataSource.setUrl("jdbc:h2:mem:bank_db");
        dataSource.setUsername("sa");
        dataSource.setPassword("sa");
        return dataSource;
    }

    /**
     * Method to create a database schema.
     * As a singleton the bean is loaded eagerly, and
     * it has injected EntityManagerFactory field.
     * When field injects, hibernate automatically creates the ddl-schema.
     * @return dummy object
     */
    @Bean(initMethod = "init")
    @Profile(value = "spring-test")
    public TestDataInitializer initTestData() {
        return new TestDataInitializer();
    }
}
