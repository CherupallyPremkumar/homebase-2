package com.homebase.ecom.user;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

import org.chenile.utils.entity.service.EntityStore;
import com.homebase.ecom.user.domain.model.User;


@Configuration
@PropertySource("classpath:com/homebase/ecom/user/TestService.properties")
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration", "com.homebase.ecom.user.configuration", "com.homebase.ecom.user.infrastructure" })
@ActiveProfiles("unittest")
public class SpringTestConfig{
	
}

