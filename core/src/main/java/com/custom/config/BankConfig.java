package com.custom.config;

import com.custom.service.ClientService;
import com.custom.service.TransactionService;
import com.custom.service.impl.ClientServiceImpl;
import com.custom.service.impl.TransactionServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by olga on 17.09.15.
 */
@Configuration
@Import({DataSourceConfig.class, WebMvcConfig.class})
public class BankConfig {
    @Bean
    public ClientService getClientService() {
        return new ClientServiceImpl();
    }

    @Bean
    public TransactionService getTransactionService() {
        return new TransactionServiceImpl();
    }

}
