package com.homebase.ecom.supplier;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;


@Configuration
@PropertySource("classpath:com/homebase/ecom/supplier/TestService.properties")
@SpringBootApplication(scanBasePackages = {
    "org.chenile.configuration",
    "com.homebase.ecom.supplier.configuration",
    "com.homebase.ecom.supplier.infrastructure"
})
@ActiveProfiles("unittest")
public class SpringTestConfig {

}
