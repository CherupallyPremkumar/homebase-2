package com.homebase.ecom.promo.configuration;

import com.homebase.ecom.promo.model.Coupon;
import com.homebase.ecom.promo.service.cmds.*;
import com.homebase.ecom.promo.service.event.PromoEventHandler;
import com.homebase.ecom.promo.port.PromoEventPublisherPort;
import com.homebase.ecom.promo.service.postSaveHooks.*;
import com.homebase.ecom.promo.service.store.PromoEntityStore;
import com.homebase.ecom.promo.service.validator.PromoPolicyValidator;
import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.action.scriptsupport.IfAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.ognl.OgnlScriptingStrategy;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.param.MinimalPayload;
import com.homebase.ecom.core.workflow.HmStateEntityServiceImpl;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import org.chenile.workflow.service.stmcmds.StmBodyTypeSelector;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Promo module Spring configuration.
 *
 * STM flow: DRAFT -> SCHEDULED -> ACTIVE -> EXPIRED -> ARCHIVED
 *           ACTIVE -> PAUSED (resume -> ACTIVE)
 *           DRAFT/SCHEDULED/ACTIVE/PAUSED -> CANCELLED -> ARCHIVED
 *
 * Auto-states: CHECK_USAGE (usageCount >= usageLimit), CHECK_EXPIRATION (endDate past)
 */
@Configuration
@ComponentScan(basePackages = "com.homebase.ecom.promo")
@EnableJpaRepositories(basePackages = "com.homebase.ecom.promo.repository")
public class PromoConfiguration {

    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/promo/promo-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Promo";
    public static final String PREFIX_FOR_RESOLVER = "promo";

    // ===================== STM Infrastructure =====================

    @Bean
    BeanFactoryAdapter promoBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl promoFlowStore(
            @Qualifier("promoBeanFactoryAdapter") BeanFactoryAdapter promoBeanFactoryAdapter) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(promoBeanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean
    STM<Coupon> promoEntityStm(@Qualifier("promoFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<Coupon> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider promoActionsInfoProvider(@Qualifier("promoFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("promo", provider);
        return provider;
    }

    @Bean
    EntityStore<Coupon> promoEntityStore() {
        return new PromoEntityStore();
    }

    @Bean
    StateEntityServiceImpl<Coupon> _promoStateEntityService_(
            @Qualifier("promoEntityStm") STM<Coupon> stm,
            @Qualifier("promoActionsInfoProvider") STMActionsInfoProvider promoInfoProvider,
            @Qualifier("promoEntityStore") EntityStore<Coupon> entityStore) {
        return new HmStateEntityServiceImpl<>(stm, promoInfoProvider, entityStore);
    }

    @Bean
    XmlFlowReader promoFlowReader(@Qualifier("promoFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean
    STMTransitionActionResolver promoTransitionActionResolver(
            @Qualifier("defaultPromoSTMTransitionAction") STMTransitionAction<Coupon> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean
    STMTransitionAction<Coupon> defaultPromoSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    DefaultPostSaveHook<Coupon> promoDefaultPostSaveHook(
            @Qualifier("promoTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean
    GenericEntryAction<Coupon> promoEntryAction(@Qualifier("promoEntityStore") EntityStore<Coupon> entityStore,
            @Qualifier("promoActionsInfoProvider") STMActionsInfoProvider promoInfoProvider,
            @Qualifier("promoFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("promoDefaultPostSaveHook") DefaultPostSaveHook<Coupon> postSaveHook) {
        GenericEntryAction<Coupon> entryAction = new GenericEntryAction<>(entityStore, promoInfoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    GenericExitAction<Coupon> promoExitAction(@Qualifier("promoFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<Coupon> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    // OGNL scripting strategy for auto-states (Item 16)
    @Bean
    OgnlScriptingStrategy ognlScriptingStrategy() {
        return new OgnlScriptingStrategy();
    }

    // IfAction for auto-states (CHECK_USAGE, CHECK_EXPIRATION)
    @Bean
    IfAction ifAction() {
        return new IfAction();
    }

    @Bean
    StmBodyTypeSelector promoBodyTypeSelector(
            @Qualifier("promoActionsInfoProvider") STMActionsInfoProvider promoInfoProvider,
            @Qualifier("promoTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(promoInfoProvider, stmTransitionActionResolver);
    }

    @Bean
    java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> promoEventAuthoritiesSupplier(
            @Qualifier("promoActionsInfoProvider") STMActionsInfoProvider promoInfoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(promoInfoProvider, false);
        return builder.build();
    }

    // ===================== STM Actions =====================

    @Bean
    public SchedulePromoAction promoScheduleAction() {
        return new SchedulePromoAction();
    }

    @Bean
    public ActivatePromoAction promoActivateAction() {
        return new ActivatePromoAction();
    }

    @Bean
    public PausePromoAction promoPauseAction() {
        return new PausePromoAction();
    }

    @Bean
    public ResumePromoAction promoResumeAction() {
        return new ResumePromoAction();
    }

    @Bean
    public ExpirePromoAction promoExpireAction() {
        return new ExpirePromoAction();
    }

    @Bean
    public CancelPromoAction promoCancelAction() {
        return new CancelPromoAction();
    }

    @Bean
    public ArchivePromoAction promoArchiveAction() {
        return new ArchivePromoAction();
    }

    @Bean
    public IncrementUsageAction promoIncrementUsageAction() {
        return new IncrementUsageAction();
    }

    // ===================== PostSaveHooks =====================

    @Bean
    DRAFTPromoPostSaveHook promoDRAFTPostSaveHook() {
        return new DRAFTPromoPostSaveHook();
    }

    @Bean
    SCHEDULEDPromoPostSaveHook promoSCHEDULEDPostSaveHook() {
        return new SCHEDULEDPromoPostSaveHook();
    }

    @Bean
    ACTIVEPromoPostSaveHook promoACTIVEPostSaveHook(PromoEventPublisherPort promoEventPublisherPort) {
        return new ACTIVEPromoPostSaveHook(promoEventPublisherPort);
    }

    @Bean
    PAUSEDPromoPostSaveHook promoPAUSEDPostSaveHook() {
        return new PAUSEDPromoPostSaveHook();
    }

    @Bean
    EXPIREDPromoPostSaveHook promoEXPIREDPostSaveHook(PromoEventPublisherPort promoEventPublisherPort) {
        return new EXPIREDPromoPostSaveHook(promoEventPublisherPort);
    }

    @Bean
    CANCELLEDPromoPostSaveHook promoCANCELLEDPostSaveHook() {
        return new CANCELLEDPromoPostSaveHook();
    }

    @Bean
    ARCHIVEDPromoPostSaveHook promoARCHIVEDPostSaveHook() {
        return new ARCHIVEDPromoPostSaveHook();
    }

    // ===================== Domain Services =====================

    @Bean
    com.homebase.ecom.promo.service.RuleEvaluator promoRuleEvaluator() {
        return new com.homebase.ecom.promo.service.RuleEvaluator();
    }

    @Bean
    com.homebase.ecom.promo.service.ConflictResolver promoConflictResolver() {
        return new com.homebase.ecom.promo.service.ConflictResolver();
    }

    @Bean
    com.homebase.ecom.promo.service.PricingCalculator promoPricingCalculator(
            com.homebase.ecom.promo.service.RuleEvaluator ruleEvaluator,
            com.homebase.ecom.promo.service.ConflictResolver conflictResolver) {
        return new com.homebase.ecom.promo.service.PricingCalculator(ruleEvaluator, conflictResolver);
    }

    @Bean
    com.homebase.ecom.promo.service.CouponUsageTracker promoCouponUsageTracker(
            com.homebase.ecom.promo.repository.CouponRepository couponRepository,
            com.homebase.ecom.promo.repository.CouponUsageLogRepository usageLogRepository) {
        return new com.homebase.ecom.promo.service.CouponUsageTracker(couponRepository, usageLogRepository);
    }

    // ===================== Validator =====================

    @Bean
    PromoPolicyValidator promoPolicyValidator() {
        return new PromoPolicyValidator();
    }

    // ===================== Kafka Event Handler (Item 10, 14) =====================

    @Bean("promoEventService")
    PromoEventHandler promoEventService(
            @Qualifier("_promoStateEntityService_") StateEntityServiceImpl<Coupon> promoStateEntityService,
            tools.jackson.databind.ObjectMapper objectMapper) {
        return new PromoEventHandler(promoStateEntityService, objectMapper);
    }
}
