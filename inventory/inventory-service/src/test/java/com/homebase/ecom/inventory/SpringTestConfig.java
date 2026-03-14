package com.homebase.ecom.inventory;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;


@Configuration
@PropertySource("classpath:com/homebase/ecom/inventory/TestService.properties")
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration", "com.homebase.ecom.inventory.configuration" })
@ActiveProfiles("unittest")
public class SpringTestConfig{

}

