package com.mycompany.myorg.catalog.query.service.bdd;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration", "com.homebase.ecom"})
@ActiveProfiles("unittest")
public class SpringTestConfig{
}

