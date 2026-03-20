package com.homebase.ecom.offer.service.configuration;

import com.homebase.ecom.offer.api.OfferService;
import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.port.NotificationPort;
import com.homebase.ecom.offer.domain.port.OfferRepository;
import com.homebase.ecom.offer.domain.port.PricingPort;
import com.homebase.ecom.offer.infrastructure.adapter.NotificationAdapter;
import com.homebase.ecom.offer.infrastructure.adapter.PricingAdapter;
import com.homebase.ecom.offer.infrastructure.persistence.ChenileOfferEntityStore;
import com.homebase.ecom.offer.infrastructure.persistence.adapter.OfferJpaRepository;
import com.homebase.ecom.offer.infrastructure.persistence.adapter.OfferRepositoryImpl;
import com.homebase.ecom.offer.infrastructure.persistence.mapper.OfferMapper;
import com.homebase.ecom.offer.service.cmds.*;
import com.homebase.ecom.offer.service.event.OfferEventHandler;
import com.homebase.ecom.offer.service.impl.OfferServiceImpl;
import com.homebase.ecom.offer.service.postSaveHooks.*;
import com.homebase.ecom.offer.service.validator.OfferPolicyValidator;
import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.action.scriptsupport.IfAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.ognl.OgnlScriptingStrategy;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.chenile.workflow.service.impl.HmStateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OfferConfiguration {

    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/offer/offer-states.xml";
    public static final String PREFIX_FOR_RESOLVER = "offer";

    // ════════════════════════════════════════════════════════════════════════
    // Infrastructure
    // ════════════════════════════════════════════════════════════════════════

    @Bean
    public OfferMapper offerMapper() {
        return new OfferMapper();
    }

    @Bean
    public OfferRepository offerRepository(OfferJpaRepository offerJpaRepository, OfferMapper offerMapper) {
        return new OfferRepositoryImpl(offerJpaRepository, offerMapper);
    }

    @Bean
    public EntityStore<Offer> offerEntityStore(OfferJpaRepository offerJpaRepository, OfferMapper offerMapper) {
        return new ChenileOfferEntityStore(offerJpaRepository, offerMapper);
    }

    // Item 12: Hexagonal ports / adapters
    @Bean
    public PricingPort pricingPort() {
        return new PricingAdapter();
    }

    @Bean
    public NotificationPort notificationPort() {
        return new NotificationAdapter();
    }

    // ════════════════════════════════════════════════════════════════════════
    // STM Infrastructure
    // ════════════════════════════════════════════════════════════════════════

    @Bean
    BeanFactoryAdapter offerBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl offerFlowStore(
            @Qualifier("offerBeanFactoryAdapter") BeanFactoryAdapter beanFactoryAdapter) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(beanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean
    STM<Offer> offerEntityStm(@Qualifier("offerFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<Offer> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider offerActionsInfoProvider(@Qualifier("offerFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("offer", provider);
        return provider;
    }

    @Bean
    XmlFlowReader offerFlowReader(@Qualifier("offerFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean
    STMTransitionAction<Offer> defaultOfferSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver offerTransitionActionResolver(
            @Qualifier("defaultOfferSTMTransitionAction") STMTransitionAction<Offer> defaultAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultAction, true);
    }

    @Bean
    StateEntityServiceImpl<Offer> _offerStateEntityService_(
            @Qualifier("offerEntityStm") STM<Offer> stm,
            @Qualifier("offerActionsInfoProvider") STMActionsInfoProvider infoProvider,
            @Qualifier("offerEntityStore") EntityStore<Offer> entityStore) {
        return new HmStateEntityServiceImpl<>(stm, infoProvider, entityStore);
    }

    // Item 16: OGNL scripting for auto-states
    @Bean
    OgnlScriptingStrategy ognlScriptingStrategy() {
        return new OgnlScriptingStrategy();
    }

    @Bean
    IfAction<Offer> ifAction() {
        return new IfAction<>();
    }

    // ════════════════════════════════════════════════════════════════════════
    // STM Entry/Exit/PostSaveHook/AutoState infrastructure
    // ════════════════════════════════════════════════════════════════════════

    @Bean
    DefaultPostSaveHook<Offer> offerDefaultPostSaveHook(
            @Qualifier("offerTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean
    GenericEntryAction<Offer> offerEntryAction(@Qualifier("offerEntityStore") EntityStore<Offer> entityStore,
            @Qualifier("offerActionsInfoProvider") STMActionsInfoProvider offerInfoProvider,
            @Qualifier("offerFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("offerDefaultPostSaveHook") DefaultPostSaveHook<Offer> postSaveHook) {
        GenericEntryAction<Offer> entryAction = new GenericEntryAction<>(entityStore, offerInfoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    DefaultAutomaticStateComputation<Offer> offerDefaultAutoState(
            @Qualifier("offerTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("offerFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<Offer> autoState = new DefaultAutomaticStateComputation<>(
                stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    GenericExitAction<Offer> offerExitAction(@Qualifier("offerFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<Offer> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    StmBodyTypeSelector offerBodyTypeSelector(
            @Qualifier("offerActionsInfoProvider") STMActionsInfoProvider offerInfoProvider,
            @Qualifier("offerTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(offerInfoProvider, stmTransitionActionResolver);
    }

    // ════════════════════════════════════════════════════════════════════════
    // STM Actions (Item 4)
    // Convention: "offer" + eventId + "Action"
    // ════════════════════════════════════════════════════════════════════════

    @Bean
    public OfferSubmitAction offerSubmitAction() {
        return new OfferSubmitAction();
    }

    @Bean
    public OfferApproveAction offerApproveAction() {
        return new OfferApproveAction();
    }

    @Bean
    public OfferRejectAction offerRejectAction() {
        return new OfferRejectAction();
    }

    @Bean
    public OfferGoLiveAction offerGoLiveAction() {
        return new OfferGoLiveAction();
    }

    @Bean
    public OfferExpireAction offerExpireAction() {
        return new OfferExpireAction();
    }

    @Bean
    public OfferSuspendAction offerSuspendAction() {
        return new OfferSuspendAction();
    }

    @Bean
    public OfferArchiveAction offerArchiveAction() {
        return new OfferArchiveAction();
    }

    @Bean
    public OfferResubmitAction offerResubmitAction() {
        return new OfferResubmitAction();
    }

    @Bean
    public OfferResumeAction offerResumeAction() {
        return new OfferResumeAction();
    }

    // ════════════════════════════════════════════════════════════════════════
    // PostSaveHooks (Item 13)
    // Convention: "offer" + STATE_NAME + "PostSaveHook"
    // ════════════════════════════════════════════════════════════════════════

    @Bean
    DRAFTOfferPostSaveHook offerDRAFTPostSaveHook() {
        return new DRAFTOfferPostSaveHook();
    }

    @Bean
    PENDING_APPROVALOfferPostSaveHook offerPENDING_APPROVALPostSaveHook() {
        return new PENDING_APPROVALOfferPostSaveHook();
    }

    @Bean
    APPROVEDOfferPostSaveHook offerAPPROVEDPostSaveHook() {
        return new APPROVEDOfferPostSaveHook();
    }

    @Bean
    LIVEOfferPostSaveHook offerLIVEPostSaveHook() {
        return new LIVEOfferPostSaveHook();
    }

    @Bean
    EXPIREDOfferPostSaveHook offerEXPIREDPostSaveHook() {
        return new EXPIREDOfferPostSaveHook();
    }

    @Bean
    ARCHIVEDOfferPostSaveHook offerARCHIVEDPostSaveHook() {
        return new ARCHIVEDOfferPostSaveHook();
    }

    @Bean
    REJECTEDOfferPostSaveHook offerREJECTEDPostSaveHook() {
        return new REJECTEDOfferPostSaveHook();
    }

    @Bean
    SUSPENDEDOfferPostSaveHook offerSUSPENDEDPostSaveHook() {
        return new SUSPENDEDOfferPostSaveHook();
    }

    // ════════════════════════════════════════════════════════════════════════
    // Policy Validator (Item 3)
    // ════════════════════════════════════════════════════════════════════════

    @Bean
    OfferPolicyValidator offerPolicyValidator() {
        return new OfferPolicyValidator();
    }

    // ════════════════════════════════════════════════════════════════════════
    // Service
    // ════════════════════════════════════════════════════════════════════════

    @Bean
    public OfferService offerService(OfferRepository offerRepository, OfferMapper offerMapper,
                                    @Qualifier("_offerStateEntityService_") StateEntityServiceImpl<Offer> stateEntityService) {
        return new OfferServiceImpl(offerRepository, offerMapper, stateEntityService);
    }

    // ════════════════════════════════════════════════════════════════════════
    // ACL (Item 15)
    // ════════════════════════════════════════════════════════════════════════

    @Bean
    java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> offerEventAuthoritiesSupplier(
            @Qualifier("offerActionsInfoProvider") STMActionsInfoProvider offerInfoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(offerInfoProvider, false);
        return builder.build();
    }

    // ════════════════════════════════════════════════════════════════════════
    // Kafka Event Handler (Items 10, 14)
    // ════════════════════════════════════════════════════════════════════════

    @Bean("offerEventService")
    @org.springframework.boot.autoconfigure.condition.ConditionalOnBean(org.chenile.pubsub.ChenilePub.class)
    OfferEventHandler offerEventService(
            OfferRepository offerRepository,
            @Qualifier("_offerStateEntityService_") StateEntityServiceImpl<Offer> offerStateEntityService,
            tools.jackson.databind.ObjectMapper objectMapper) {
        return new OfferEventHandler(offerRepository, offerStateEntityService, objectMapper);
    }
}
