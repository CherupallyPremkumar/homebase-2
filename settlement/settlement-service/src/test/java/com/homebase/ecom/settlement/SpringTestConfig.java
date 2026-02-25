package com.homebase.ecom.settlement;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

import org.chenile.utils.entity.service.EntityStore;
import com.homebase.ecom.settlement.model.Settlement;


@Configuration
@PropertySource("classpath:com/homebase/ecom/settlement/TestService.properties")
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration", "com.homebase.ecom.settlement.configuration" })
@ActiveProfiles("unittest")
public class SpringTestConfig extends SpringBootServletInitializer{
	
}

