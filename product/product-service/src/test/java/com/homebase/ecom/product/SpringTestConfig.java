package com.homebase.ecom.product;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

import org.chenile.utils.entity.service.EntityStore;
import com.homebase.ecom.product.model.Product;


@Configuration
@PropertySource("classpath:com/homebase/ecom/product/TestService.properties")
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration", "com.homebase.ecom.product.configuration" })
@ActiveProfiles("unittest")
public class SpringTestConfig extends SpringBootServletInitializer{
	
}

