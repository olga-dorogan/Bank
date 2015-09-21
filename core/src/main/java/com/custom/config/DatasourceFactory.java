package com.custom.config;

import com.custom.exception.BankDAOException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Created by olga on 17.09.15.
 */
public class DatasourceFactory {
    public final static String DATASOURCE_JNDI = "java:comp/env/jdbc/bankDS";

    public static DataSource getDataSource() {
        try {
            InitialContext ctx = new InitialContext();
            return (DataSource) ctx.lookup(DatasourceFactory.DATASOURCE_JNDI);

        } catch (NamingException e) {
            throw new BankDAOException(e.getMessage(), e);
        }
    }
    private DatasourceFactory() {

    }
}
