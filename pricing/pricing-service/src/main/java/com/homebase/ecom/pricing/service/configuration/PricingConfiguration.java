package com.homebase.ecom.pricing.service.configuration;

import com.homebase.ecom.rulesengine.api.dto.DecisionDto;
import com.homebase.ecom.rulesengine.api.dto.EvaluateRequest;
import com.homebase.ecom.rulesengine.api.service.DecisionService;
import com.homebase.ecom.pricing.domain.model.PricingContext;
import com.homebase.ecom.pricing.domain.port.CustomerTierPort;
import com.homebase.ecom.pricing.domain.port.OfferPricePort;
import com.homebase.ecom.pricing.domain.port.PolicyEvaluationPort;
import com.homebase.ecom.pricing.domain.port.PromoValidationPort;
import com.homebase.ecom.pricing.domain.port.PriceLockPort;
import com.homebase.ecom.pricing.domain.port.TaxCalculationPort;
import com.homebase.ecom.pricing.domain.service.IHashCalculator;
import com.homebase.ecom.pricing.domain.service.ILockTokenGenerator;
import com.homebase.ecom.pricing.service.PricingPolicyValidator;
import com.homebase.ecom.pricing.service.PricingServiceImpl;
import com.homebase.ecom.pricing.service.command.*;
import com.homebase.ecom.shared.Money;
import org.chenile.owiz.OrchExecutor;
import org.chenile.owiz.config.impl.XmlOrchConfigurator;
import org.chenile.owiz.impl.OrchExecutorImpl;
import org.chenile.owiz.impl.OwizSpringFactoryAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

/**
 * Pricing service layer configuration.
 * Wires OWIZ pipeline, commands, and service implementation.
 * Only references domain ports/interfaces — never infrastructure classes.
 * Stub adapters provided here as fallbacks via @ConditionalOnMissingBean.
 */
@Configuration
public class PricingConfiguration {

    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/pricing/pricing-flow.xml";

    // ═══════════════════════════════════════════════════════════════════
    // OWIZ Pipeline Infrastructure
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    OwizSpringFactoryAdapter pricingOwizBeanFactory() {
        return new OwizSpringFactoryAdapter();
    }

    @Bean
    XmlOrchConfigurator<PricingContext> pricingOwizConfigurator(
            @Qualifier("pricingOwizBeanFactory") OwizSpringFactoryAdapter beanFactory) throws Exception {
        XmlOrchConfigurator<PricingContext> config = new XmlOrchConfigurator<>();
        config.setBeanFactoryAdapter(beanFactory);
        config.setFilename(FLOW_DEFINITION_FILE);
        return config;
    }

    @Bean
    OrchExecutor<PricingContext> pricingOwizExecutor(
            @Qualifier("pricingOwizConfigurator") XmlOrchConfigurator<PricingContext> configurator) {
        OrchExecutorImpl<PricingContext> executor = new OrchExecutorImpl<>();
        executor.setOrchConfigurator(configurator);
        return executor;
    }

    // ═══════════════════════════════════════════════════════════════════
    // Service Implementation
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    PricingServiceImpl _pricingService_(
            @Qualifier("pricingOwizExecutor") OrchExecutor<PricingContext> pricingPipeline,
            PriceLockPort priceLockPort,
            ILockTokenGenerator lockTokenGenerator,
            IHashCalculator hashCalculator,
            PricingPolicyValidator pricingPolicyValidator) {
        return new PricingServiceImpl(pricingPipeline, priceLockPort, lockTokenGenerator, hashCalculator, pricingPolicyValidator);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Cconfig Policy Validator
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    PricingPolicyValidator pricingPolicyValidator() {
        return new PricingPolicyValidator();
    }

    // ═══════════════════════════════════════════════════════════════════
    // OWIZ Pipeline Commands
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    ResolveBasePricesCommand resolveBasePricesCommand(OfferPricePort offerPricePort) {
        return new ResolveBasePricesCommand(offerPricePort);
    }

    @Bean
    ValidateSellerMinPriceCommand validateSellerMinPriceCommand(PricingPolicyValidator v) {
        return new ValidateSellerMinPriceCommand(v);
    }

    @Bean
    ApplySegmentPricingCommand applySegmentPricingCommand(CustomerTierPort customerTierPort) {
        return new ApplySegmentPricingCommand(customerTierPort);
    }

    @Bean
    RulesEngineDiscountCommand rulesEngineDiscountCommand(PolicyEvaluationPort policyEvaluationPort) {
        return new RulesEngineDiscountCommand(policyEvaluationPort);
    }

    @Bean
    ApplyPromoPriceCommand applyPromoPriceCommand(OfferPricePort offerPricePort) {
        return new ApplyPromoPriceCommand(offerPricePort);
    }

    @Bean
    ApplyCouponCommand applyCouponCommand(PromoValidationPort promoValidationPort,
                                          PricingPolicyValidator v) {
        return new ApplyCouponCommand(promoValidationPort, v);
    }

    @Bean
    EnforceDiscountCapCommand enforceDiscountCapCommand(PricingPolicyValidator v) {
        return new EnforceDiscountCapCommand(v);
    }

    @Bean
    CalculateTaxCommand calculateTaxCommand(TaxCalculationPort taxCalculationPort) {
        return new CalculateTaxCommand(taxCalculationPort);
    }

    @Bean
    CalculateTCSCommand calculateTCSCommand(PricingPolicyValidator v) {
        return new CalculateTCSCommand(v);
    }

    @Bean
    BuildPricingResponseCommand buildPricingResponseCommand(IHashCalculator hashCalculator) {
        return new BuildPricingResponseCommand(hashCalculator);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Stub Adapters — fallbacks when real modules aren't deployed
    // Replaced by real adapters from PricingInfrastructureConfiguration
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    @ConditionalOnMissingBean(PromoValidationPort.class)
    PromoValidationPort defaultPromoValidationPort() {
        return (code, cartTotal, userId) ->
                PromoValidationPort.CouponResult.invalid(code, "Promo service not available");
    }

    @Bean
    @ConditionalOnMissingBean(CustomerTierPort.class)
    CustomerTierPort defaultCustomerTierPort() {
        return userId -> "REGULAR";
    }

    /**
     * Stub: Offer module not yet connected.
     * Returns empty — ResolveBasePricesCommand falls back to cart snapshot basePrice.
     * Replace with real OfferPriceAdapter in infrastructure when Offer module is ready.
     */
    @Bean
    @ConditionalOnMissingBean(OfferPricePort.class)
    OfferPricePort defaultOfferPricePort() {
        return (variantId, sellerId, currency) -> Optional.empty();
    }

    @Bean
    @ConditionalOnMissingBean(TaxCalculationPort.class)
    TaxCalculationPort defaultTaxCalculationPort(PricingPolicyValidator v) {
        return (taxableAmount, region, category) ->
                Money.of(Math.round((double) taxableAmount.getAmount() * v.getDefaultGstRate() / 100), taxableAmount.getCurrency());
    }

    @Bean
    @ConditionalOnMissingBean(DecisionService.class)
    DecisionService defaultDecisionService() {
        return new DecisionService() {
            @Override
            public DecisionDto evaluate(EvaluateRequest request) {
                return new DecisionDto();
            }

            @Override
            public List<DecisionDto> evaluateAll(EvaluateRequest request) {
                return List.of();
            }

            @Override
            public List<DecisionDto> getDecisions() {
                return List.of();
            }

            @Override
            public List<DecisionDto> getDecisionsByRuleSetId(String ruleSetId) {
                return List.of();
            }
        };
    }
}
