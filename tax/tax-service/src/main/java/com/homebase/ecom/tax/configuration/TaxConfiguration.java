package com.homebase.ecom.tax.configuration;

import com.homebase.ecom.tax.infrastructure.persistence.adapter.OrderTaxLineJpaRepository;
import com.homebase.ecom.tax.infrastructure.persistence.adapter.OrderTaxLineRepositoryImpl;
import com.homebase.ecom.tax.infrastructure.persistence.adapter.TaxRateJpaRepository;
import com.homebase.ecom.tax.infrastructure.persistence.adapter.TaxRateRepositoryImpl;
import com.homebase.ecom.tax.infrastructure.persistence.mapper.TaxMapper;
import com.homebase.ecom.tax.model.port.OrderTaxLineRepository;
import com.homebase.ecom.tax.model.port.TaxRateRepository;
import com.homebase.ecom.tax.service.TaxService;
import com.homebase.ecom.tax.service.impl.TaxServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaxConfiguration {

    @Bean
    public TaxMapper taxMapper() {
        return new TaxMapper();
    }

    @Bean
    public TaxRateRepository taxRateRepository(TaxRateJpaRepository jpaRepository, TaxMapper mapper) {
        return new TaxRateRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public OrderTaxLineRepository orderTaxLineRepository(OrderTaxLineJpaRepository jpaRepository, TaxMapper mapper) {
        return new OrderTaxLineRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public TaxService taxService(TaxRateRepository taxRateRepository, OrderTaxLineRepository orderTaxLineRepository) {
        return new TaxServiceImpl(taxRateRepository, orderTaxLineRepository);
    }
}
