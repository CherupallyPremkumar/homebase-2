package com.homebase.ecom.tax.query.service.bdd;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.test.context.ActiveProfiles;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration", "com.homebase.ecom.query.service"})
@ActiveProfiles("unittest")
public class SpringTestConfig {

	@Bean
	public SpringLiquibase liquibase(@Qualifier("queryDatasource") DataSource dataSource) {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog("classpath:liquibase/query/changelog/changelog-master.xml");
		liquibase.setResourceLoader(new DefaultResourceLoader());
		return liquibase;
	}
}
