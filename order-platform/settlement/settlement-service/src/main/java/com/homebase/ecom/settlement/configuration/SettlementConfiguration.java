package com.homebase.ecom.settlement.configuration;

import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.chenile.utils.entity.service.EntityStore;
import com.homebase.ecom.core.workflow.HmStateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.service.cmds.*;
import com.homebase.ecom.settlement.service.healthcheck.SettlementHealthChecker;
import com.homebase.ecom.settlement.infrastructure.persistence.ChenileSettlementEntityStore;
import com.homebase.ecom.settlement.infrastructure.persistence.adapter.SettlementJpaRepository;
import com.homebase.ecom.settlement.infrastructure.persistence.mapper.SettlementMapper;
import com.homebase.ecom.settlement.infrastructure.adapter.PayoutAdapter;
import com.homebase.ecom.settlement.infrastructure.adapter.NotificationAdapter;
import com.homebase.ecom.settlement.domain.port.PayoutPort;
import com.homebase.ecom.settlement.domain.port.NotificationPort;
import com.homebase.ecom.settlement.service.event.SettlementEventHandler;
import org.chenile.stm.action.scriptsupport.IfAction;
import org.chenile.stm.ognl.OgnlScriptingStrategy;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import com.homebase.ecom.settlement.domain.port.SettlementEventPublisherPort;
import com.homebase.ecom.settlement.service.postSaveHooks.*;
import com.homebase.ecom.settlement.service.validator.SettlementPolicyValidator;

/**
 * Settlement module Spring configuration.
 * Wires the STM, entity store, transition actions, post-save hooks,
 * hexagonal ports/adapters, and chenile-kafka event handler.
 */
@Configuration
public class SettlementConfiguration {

    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/settlement/settlement-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Settlement";
    public static final String PREFIX_FOR_RESOLVER = "settlement";

    @Bean
    BeanFactoryAdapter settlementBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl settlementFlowStore(
            @Qualifier("settlementBeanFactoryAdapter") BeanFactoryAdapter settlementBeanFactoryAdapter)
            throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(settlementBeanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean
    STM<Settlement> settlementEntityStm(@Qualifier("settlementFlowStore") STMFlowStoreImpl stmFlowStore)
            throws Exception {
        STMImpl<Settlement> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider settlementActionsInfoProvider(
            @Qualifier("settlementFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("settlement", provider);
        return provider;
    }

    @Bean
    SettlementMapper settlementMapper() {
        return new SettlementMapper();
    }

    @Bean
    EntityStore<Settlement> settlementEntityStore(SettlementJpaRepository jpaRepository, SettlementMapper mapper) {
        return new ChenileSettlementEntityStore(jpaRepository, mapper);
    }

    @Bean
    HmStateEntityServiceImpl<Settlement> _settlementStateEntityService_(
            @Qualifier("settlementEntityStm") STM<Settlement> stm,
            @Qualifier("settlementActionsInfoProvider") STMActionsInfoProvider settlementInfoProvider,
            @Qualifier("settlementEntityStore") EntityStore<Settlement> entityStore) {
        return new HmStateEntityServiceImpl<>(stm, settlementInfoProvider, entityStore);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // STM Components
    // ═══════════════════════════════════════════════════════════════════════

    @Bean
    DefaultPostSaveHook<Settlement> settlementDefaultPostSaveHook(
            @Qualifier("settlementTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean
    GenericEntryAction<Settlement> settlementEntryAction(
            @Qualifier("settlementEntityStore") EntityStore<Settlement> entityStore,
            @Qualifier("settlementActionsInfoProvider") STMActionsInfoProvider settlementInfoProvider,
            @Qualifier("settlementFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("settlementDefaultPostSaveHook") DefaultPostSaveHook<Settlement> postSaveHook) {
        GenericEntryAction<Settlement> entryAction = new GenericEntryAction<>(entityStore, settlementInfoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    DefaultAutomaticStateComputation<Settlement> settlementDefaultAutoState(
            @Qualifier("settlementTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("settlementFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<Settlement> autoState = new DefaultAutomaticStateComputation<>(stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    GenericExitAction<Settlement> settlementExitAction(@Qualifier("settlementFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<Settlement> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    XmlFlowReader settlementFlowReader(@Qualifier("settlementFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean
    SettlementHealthChecker settlementHealthChecker() {
        return new SettlementHealthChecker();
    }

    @Bean
    OgnlScriptingStrategy ognlScriptingStrategy() {
        return new OgnlScriptingStrategy();
    }

    @Bean
    IfAction<Settlement> ifAction() {
        return new IfAction<>();
    }

    @Bean
    STMTransitionAction<Settlement> defaultsettlementSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver settlementTransitionActionResolver(
            @Qualifier("defaultsettlementSTMTransitionAction") STMTransitionAction<Settlement> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean
    StmBodyTypeSelector settlementBodyTypeSelector(
            @Qualifier("settlementActionsInfoProvider") STMActionsInfoProvider settlementInfoProvider,
            @Qualifier("settlementTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(settlementInfoProvider, stmTransitionActionResolver);
    }

    @Bean
    STMTransitionAction<Settlement> settlementBaseTransitionAction(
            @Qualifier("settlementTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("settlementActivityChecker") ActivityChecker activityChecker,
            @Qualifier("settlementFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<Settlement> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean
    ActivityChecker settlementActivityChecker(@Qualifier("settlementFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete settlementActivitiesCompletionCheck(
            @Qualifier("settlementActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    @Bean
    ConfigProviderImpl settlementConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    ConfigBasedEnablementStrategy settlementConfigBasedEnablementStrategy(
            @Qualifier("settlementConfigProvider") ConfigProvider configProvider,
            @Qualifier("settlementFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider, PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Transition Actions (convention: "settlement" + eventId + "Action")
    // ═══════════════════════════════════════════════════════════════════════

    @Bean
    CalculateSettlementAction settlementCalculateAction() {
        return new CalculateSettlementAction();
    }

    @Bean
    ApproveSettlementAction settlementApproveAction() {
        return new ApproveSettlementAction();
    }

    @Bean
    DisburseSettlementAction settlementDisburseAction() {
        return new DisburseSettlementAction();
    }

    @Bean
    DisputeSettlementAction settlementDisputeAction() {
        return new DisputeSettlementAction();
    }

    @Bean
    ResolveSettlementAction settlementResolveAction() {
        return new ResolveSettlementAction();
    }

    @Bean
    AdjustSettlementAction settlementAdjustAction() {
        return new AdjustSettlementAction();
    }

    @Bean
    RejectSettlementAction settlementRejectAction() {
        return new RejectSettlementAction();
    }

    @Bean
    RetrySettlementAction settlementRetryAction() {
        return new RetrySettlementAction();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Post-Save Hooks (convention: "settlement" + STATE + "PostSaveHook")
    // ═══════════════════════════════════════════════════════════════════════

    @Bean
    PENDINGSettlementPostSaveHook settlementPENDINGPostSaveHook() {
        return new PENDINGSettlementPostSaveHook();
    }

    @Bean
    CALCULATINGSettlementPostSaveHook settlementCALCULATINGPostSaveHook() {
        return new CALCULATINGSettlementPostSaveHook();
    }

    @Bean
    CALCULATEDSettlementPostSaveHook settlementCALCULATEDPostSaveHook() {
        return new CALCULATEDSettlementPostSaveHook();
    }

    @Bean
    APPROVEDSettlementPostSaveHook settlementAPPROVEDPostSaveHook(
            NotificationPort notificationPort, SettlementEventPublisherPort eventPublisher) {
        return new APPROVEDSettlementPostSaveHook(notificationPort, eventPublisher);
    }

    @Bean
    DISBURSEDSettlementPostSaveHook settlementDISBURSEDPostSaveHook(
            NotificationPort notificationPort, SettlementEventPublisherPort eventPublisher) {
        return new DISBURSEDSettlementPostSaveHook(notificationPort, eventPublisher);
    }

    @Bean
    COMPLETEDSettlementPostSaveHook settlementCOMPLETEDPostSaveHook() {
        return new COMPLETEDSettlementPostSaveHook();
    }

    @Bean
    DISPUTEDSettlementPostSaveHook settlementDISPUTEDPostSaveHook(
            SettlementEventPublisherPort eventPublisher) {
        return new DISPUTEDSettlementPostSaveHook(eventPublisher);
    }

    @Bean
    FAILEDSettlementPostSaveHook settlementFAILEDPostSaveHook() {
        return new FAILEDSettlementPostSaveHook();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // ACL / Security
    // ═══════════════════════════════════════════════════════════════════════

    @Bean
    java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> settlementEventAuthoritiesSupplier(
            @Qualifier("settlementActionsInfoProvider") STMActionsInfoProvider settlementInfoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(settlementInfoProvider, false);
        return builder.build();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Hexagonal Ports / Adapters
    // ═══════════════════════════════════════════════════════════════════════

    @Bean
    PayoutPort settlementPayoutPort() {
        return new PayoutAdapter();
    }

    @Bean
    NotificationPort settlementNotificationPort() {
        return new NotificationAdapter();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Validator
    // ═══════════════════════════════════════════════════════════════════════

    @Bean
    SettlementPolicyValidator settlementPolicyValidator() {
        return new SettlementPolicyValidator();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Chenile-Kafka Event Handler
    // ═══════════════════════════════════════════════════════════════════════

    @Bean("settlementEventService")
    SettlementEventHandler settlementEventService(
            @Qualifier("_settlementStateEntityService_") HmStateEntityServiceImpl<Settlement> settlementStateEntityService,
            SettlementJpaRepository settlementJpaRepository,
            SettlementMapper settlementMapper,
            org.chenile.pubsub.ChenilePub chenilePub,
            tools.jackson.databind.ObjectMapper objectMapper,
            com.homebase.ecom.shared.CurrencyResolver currencyResolver) {
        return new SettlementEventHandler(
                settlementStateEntityService, settlementJpaRepository,
                settlementMapper, chenilePub, objectMapper, currencyResolver);
    }
}
