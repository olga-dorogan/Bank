package com.custom.config;

import com.custom.dao.AccountDAO;
import com.custom.dao.ClientDAO;
import com.custom.dao.TransactionDAO;
import com.custom.dao.impl.AccountDAOImpl;
import com.custom.dao.impl.ClientDAOImpl;
import com.custom.dao.impl.TransactionDAOImpl;
import com.custom.service.ClientService;
import com.custom.service.TransactionService;
import com.custom.service.impl.ClientServiceImpl;
import com.custom.service.impl.TransactionServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by olga on 17.09.15.
 */
@Configuration
public class BankConfig {
    @Bean
    public ClientDAO getClientDAO() {
        return new ClientDAOImpl();
    }

    @Bean
    public AccountDAO getAccountDAO() {
        return new AccountDAOImpl();
    }

    @Bean
    public TransactionDAO getTransactionDAO() {
        return new TransactionDAOImpl();
    }

    @Bean
    public ClientService getClientService() {
        return new ClientServiceImpl();
    }

    @Bean
    public TransactionService getTransactionService() {
        return new TransactionServiceImpl();
    }

}
