package com.custom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;

/**
 * Created by olga on 27.09.15.
 */
@Configuration
public class DataSourceConfig {
    private final static String DATASOURCE_JNDI = "java:comp/env/jdbc/bankDS";

    @Bean
    public DataSource dataSource() {
        final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
        return dsLookup.getDataSource(DATASOURCE_JNDI);
    }
}
