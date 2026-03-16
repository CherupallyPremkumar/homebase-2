package com.homebase.ecom.cart;


import javax.sql.DataSource;

import org.chenile.query.service.SearchService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import liquibase.integration.spring.SpringLiquibase;


@Configuration
@PropertySource("classpath:com/homebase/ecom/cart/TestService.properties")
@SpringBootApplication(scanBasePackages = {
	"org.chenile.configuration",
	"org.chenile.service.registry.configuration",
	"com.homebase.ecom.cart.configuration",
	"com.homebase.ecom.cart.infrastructure",
	"com.homebase.ecom.cart.bdd",
	"org.chenile.configuration.query.service"
})
@EnableJpaRepositories(basePackages = { "com.homebase.ecom.cart", "org.chenile.service.registry.configuration.dao" })
@EntityScan(basePackages = { "com.homebase.ecom.cart", "org.chenile.service.registry.model" })
@ActiveProfiles("unittest")
public class SpringTestConfig extends SpringBootServletInitializer {

	@Bean
	public SpringLiquibase queryLiquibase(@Qualifier("queryDatasource") DataSource dataSource) {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog("classpath:liquibase/query/changelog/changelog-master.xml");
		liquibase.setResourceLoader(new DefaultResourceLoader());
		return liquibase;
	}

	/**
	 * Product search client backed by local Chenile query SearchService.
	 * Handles variant-exists query from product mapper.
	 */
	@Bean("productSearchServiceClient")
	public SearchService productSearchServiceClient(@Qualifier("searchService") SearchService searchService) {
		return searchService;
	}

	/**
	 * Inventory search client backed by local Chenile query SearchService.
	 * Handles check-availability query from inventory mapper.
	 */
	@Bean("inventorySearchServiceClient")
	public SearchService inventorySearchServiceClient(@Qualifier("searchService") SearchService searchService) {
		return searchService;
	}

}

