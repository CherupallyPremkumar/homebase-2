package com.homebase.ecom.organisation.query.service.bdd;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration", "com.homebase.ecom.query.service"})
@ActiveProfiles("unittest")
public class SpringTestConfig {
}
