package com.homebase.ecom.analytics.query.service.bdd;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration"})
@ActiveProfiles("unittest")
public class SpringTestConfig{
}
