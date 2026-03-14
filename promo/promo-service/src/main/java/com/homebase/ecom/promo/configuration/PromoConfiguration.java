package com.homebase.ecom.promo.configuration;

import com.homebase.ecom.promo.repository.*;
import com.homebase.ecom.promo.service.*;
import com.homebase.ecom.promo.service.impl.PromotionServiceImpl;
import com.homebase.ecom.promo.service.store.PromoEntityStore;
import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import com.homebase.ecom.promo.model.Coupon;
import com.homebase.ecom.promo.service.cmds.*;
import com.homebase.ecom.promo.service.postSaveHooks.*;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "com.homebase.ecom.promo")
@EnableJpaRepositories(basePackages = "com.homebase.ecom.promo.repository")
public class PromoConfiguration {

    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/promo/promo-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Promo";
    public static final String PREFIX_FOR_RESOLVER = "promo";

    @Bean
    public PromotionService promotionService(
            PromotionRepository promotionRepository,
            CouponRepository couponRepository,
            CouponUsageTracker couponUsageTracker,
            PricingCalculator pricingCalculator) {
        return new PromotionServiceImpl(promotionRepository, couponRepository, couponUsageTracker, pricingCalculator);
    }

    @Bean
    public PricingCalculator pricingCalculator(RuleEvaluator ruleEvaluator, ConflictResolver conflictResolver) {
        return new PricingCalculator(ruleEvaluator, conflictResolver);
    }

    @Bean
    public RuleEvaluator ruleEvaluator() {
        return new RuleEvaluator();
    }

    @Bean
    public ConflictResolver conflictResolver() {
        return new ConflictResolver();
    }

    @Bean
    public CouponUsageTracker couponUsageTracker(
            CouponRepository couponRepository,
            CouponUsageLogRepository usageLogRepository) {
        return new CouponUsageTracker(couponRepository, usageLogRepository);
    }

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
        return new StateEntityServiceImpl<>(stm, promoInfoProvider, entityStore);
    }

    // STM Actions

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
    public ClosePromoAction promoCloseAction() {
        return new ClosePromoAction();
    }

    @Bean
    public ReactivatePromoAction promoReactivateAction() {
        return new ReactivatePromoAction();
    }

    // PostSaveHooks

    @Bean
    DRAFTPromoPostSaveHook promoDRAFTPostSaveHook() {
        return new DRAFTPromoPostSaveHook();
    }

    @Bean
    ACTIVEPromoPostSaveHook promoACTIVEPostSaveHook() {
        return new ACTIVEPromoPostSaveHook();
    }

    @Bean
    PAUSEDPromoPostSaveHook promoPAUSEDPostSaveHook() {
        return new PAUSEDPromoPostSaveHook();
    }

    @Bean
    EXPIREDPromoPostSaveHook promoEXPIREDPostSaveHook() {
        return new EXPIREDPromoPostSaveHook();
    }

    @Bean
    CLOSEDPromoPostSaveHook promoCLOSEDPostSaveHook() {
        return new CLOSEDPromoPostSaveHook();
    }

    // STM Infrastructure

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
}
