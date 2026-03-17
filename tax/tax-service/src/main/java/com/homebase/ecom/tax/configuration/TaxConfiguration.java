package com.homebase.ecom.tax.configuration;

import com.homebase.ecom.tax.infrastructure.persistence.adapter.*;
import com.homebase.ecom.tax.infrastructure.persistence.mapper.TaxMapper;
import com.homebase.ecom.tax.model.TaxContext;
import com.homebase.ecom.tax.model.port.OrderTaxLineRepository;
import com.homebase.ecom.tax.model.port.TaxCategoryMappingRepository;
import com.homebase.ecom.tax.model.port.TaxRateRepository;
import com.homebase.ecom.tax.service.TaxService;
import com.homebase.ecom.tax.service.command.*;
import com.homebase.ecom.tax.service.impl.TaxServiceImpl;
import org.chenile.owiz.OrchExecutor;
import org.chenile.owiz.config.impl.XmlOrchConfigurator;
import org.chenile.owiz.impl.OrchExecutorImpl;
import org.chenile.owiz.impl.OwizSpringFactoryAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaxConfiguration {

    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/tax/tax-flow.xml";

    // ═══════════════════════════════════════════════════════════════════
    // Infrastructure Beans
    // ═══════════════════════════════════════════════════════════════════

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
    public TaxCategoryMappingRepository taxCategoryMappingRepository(
            TaxCategoryMappingJpaRepository jpaRepository, TaxMapper mapper) {
        return new TaxCategoryMappingRepositoryImpl(jpaRepository, mapper);
    }

    // ═══════════════════════════════════════════════════════════════════
    // OWIZ Pipeline Infrastructure
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    OwizSpringFactoryAdapter taxOwizBeanFactory() {
        return new OwizSpringFactoryAdapter();
    }

    @Bean
    XmlOrchConfigurator<TaxContext> taxOwizConfigurator(
            @Qualifier("taxOwizBeanFactory") OwizSpringFactoryAdapter beanFactory) throws Exception {
        XmlOrchConfigurator<TaxContext> config = new XmlOrchConfigurator<>();
        config.setBeanFactoryAdapter(beanFactory);
        config.setFilename(FLOW_DEFINITION_FILE);
        return config;
    }

    @Bean
    OrchExecutor<TaxContext> taxOwizExecutor(
            @Qualifier("taxOwizConfigurator") XmlOrchConfigurator<TaxContext> configurator) {
        OrchExecutorImpl<TaxContext> executor = new OrchExecutorImpl<>();
        executor.setOrchConfigurator(configurator);
        return executor;
    }

    // ═══════════════════════════════════════════════════════════════════
    // OWIZ Pipeline Commands
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    ResolveHsnCodesCommand resolveHsnCodesCommand(TaxCategoryMappingRepository categoryMappingRepository) {
        return new ResolveHsnCodesCommand(categoryMappingRepository);
    }

    @Bean
    DetermineTaxTypeCommand determineTaxTypeCommand() {
        return new DetermineTaxTypeCommand();
    }

    @Bean
    LookupTaxRatesCommand lookupTaxRatesCommand(TaxCategoryMappingRepository categoryMappingRepository,
                                                 TaxRateRepository taxRateRepository) {
        return new LookupTaxRatesCommand(categoryMappingRepository, taxRateRepository);
    }

    @Bean
    CalculateTaxAmountsCommand calculateTaxAmountsCommand() {
        return new CalculateTaxAmountsCommand();
    }

    @Bean
    BuildTaxResponseCommand buildTaxResponseCommand() {
        return new BuildTaxResponseCommand();
    }

    // ═══════════════════════════════════════════════════════════════════
    // Service Implementation
    // ═══════════════════════════════════════════════════════════════════

    @Bean("taxServiceImpl")
    public TaxService taxServiceImpl(
            @Qualifier("taxOwizExecutor") OrchExecutor<TaxContext> taxPipeline,
            TaxRateRepository taxRateRepository,
            OrderTaxLineRepository orderTaxLineRepository) {
        return new TaxServiceImpl(taxPipeline, taxRateRepository, orderTaxLineRepository);
    }
}
