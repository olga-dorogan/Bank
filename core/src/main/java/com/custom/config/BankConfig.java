package com.custom.config;

import com.custom.app.service.ClientService;
import com.custom.app.service.TransferService;
import com.custom.app.service.impl.ClientServiceImpl;
import com.custom.app.service.impl.TransferServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by olga on 17.09.15.
 */
@Configuration
@Import({DataSourceConfig.class, WebConfiguration.class})
public class BankConfig {
    @Bean
    public ClientService getClientService() {
        return new ClientServiceImpl();
    }

    @Bean
    public TransferService getTransactionService() {
        return new TransferServiceImpl();
    }

}
