package com.custom.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by olga on 17.09.15.
 */
@Configuration
@ComponentScan({"com.custom.app.service", "com.custom.app.service.impl"})
public class BankConfig {

}
