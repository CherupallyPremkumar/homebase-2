package com.homebase.ecom.shared.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for the shared-service module.
 * Enables component scanning for CurrencyResolver and ExchangeRateService.
 */
@Configuration
@ComponentScan(basePackages = "com.homebase.ecom.shared")
@EnableCaching
public class SharedServiceConfiguration {
}
