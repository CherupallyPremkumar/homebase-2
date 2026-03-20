package com.homebase.ecom.support.configuration;

import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.action.scriptsupport.IfAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.ognl.OgnlScriptingStrategy;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.support.service.cmds.*;
import com.homebase.ecom.support.service.healthcheck.SupportHealthChecker;
import com.homebase.ecom.support.service.validator.SupportPolicyValidator;
import com.homebase.ecom.support.service.event.SupportEventHandler;
import com.homebase.ecom.support.infrastructure.persistence.ChenileSupportTicketEntityStore;
import com.homebase.ecom.support.infrastructure.persistence.adapter.SupportTicketJpaRepository;
import com.homebase.ecom.support.infrastructure.persistence.mapper.SupportTicketMapper;
import org.chenile.workflow.api.WorkflowRegistry;
import com.homebase.ecom.support.service.postSaveHooks.*;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;

/**
 * Spring Configuration for the Support STM module.
 */
@Configuration
public class SupportConfiguration {
    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/support/support-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Support";
    public static final String PREFIX_FOR_RESOLVER = "support";

    @Bean
    BeanFactoryAdapter supportBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl supportFlowStore(
            @Qualifier("supportBeanFactoryAdapter") BeanFactoryAdapter supportBeanFactoryAdapter) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(supportBeanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean
    STM<SupportTicket> supportEntityStm(@Qualifier("supportFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<SupportTicket> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider supportActionsInfoProvider(@Qualifier("supportFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("support", provider);
        return provider;
    }

    @Bean
    SupportTicketMapper supportTicketMapper() {
        return new SupportTicketMapper();
    }

    @Bean
    EntityStore<SupportTicket> supportEntityStore(SupportTicketJpaRepository jpaRepository, SupportTicketMapper mapper) {
        return new ChenileSupportTicketEntityStore(jpaRepository, mapper);
    }

    @Bean
    StateEntityServiceImpl<SupportTicket> _supportStateEntityService_(
            @Qualifier("supportEntityStm") STM<SupportTicket> stm,
            @Qualifier("supportActionsInfoProvider") STMActionsInfoProvider supportInfoProvider,
            @Qualifier("supportEntityStore") EntityStore<SupportTicket> entityStore) {
        return new StateEntityServiceImpl<>(stm, supportInfoProvider, entityStore);
    }

    // STM Components

    @Bean
    DefaultPostSaveHook<SupportTicket> supportDefaultPostSaveHook(
            @Qualifier("supportTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean
    GenericEntryAction<SupportTicket> supportEntryAction(
            @Qualifier("supportEntityStore") EntityStore<SupportTicket> entityStore,
            @Qualifier("supportActionsInfoProvider") STMActionsInfoProvider supportInfoProvider,
            @Qualifier("supportFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("supportDefaultPostSaveHook") DefaultPostSaveHook<SupportTicket> postSaveHook) {
        GenericEntryAction<SupportTicket> entryAction = new GenericEntryAction<>(entityStore, supportInfoProvider,
                postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    DefaultAutomaticStateComputation<SupportTicket> supportDefaultAutoState(
            @Qualifier("supportTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("supportFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<SupportTicket> autoState = new DefaultAutomaticStateComputation<>(
                stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    GenericExitAction<SupportTicket> supportExitAction(@Qualifier("supportFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<SupportTicket> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean(name = "ognlScriptingStrategy")
    OgnlScriptingStrategy ognlScriptingStrategy() {
        return new OgnlScriptingStrategy();
    }

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean(name = "ifAction")
    IfAction<SupportTicket> ifAction() {
        return new IfAction<>();
    }

    @Bean
    XmlFlowReader supportFlowReader(@Qualifier("supportFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean
    SupportHealthChecker supportHealthChecker() {
        return new SupportHealthChecker();
    }

    @Bean
    STMTransitionAction<SupportTicket> defaultsupportSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver supportTransitionActionResolver(
            @Qualifier("defaultsupportSTMTransitionAction") STMTransitionAction<SupportTicket> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean
    StmBodyTypeSelector supportBodyTypeSelector(
            @Qualifier("supportActionsInfoProvider") STMActionsInfoProvider supportInfoProvider,
            @Qualifier("supportTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(supportInfoProvider, stmTransitionActionResolver);
    }

    @Bean
    STMTransitionAction<SupportTicket> supportBaseTransitionAction(
            @Qualifier("supportTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("supportActivityChecker") ActivityChecker activityChecker,
            @Qualifier("supportFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<SupportTicket> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean
    ActivityChecker supportActivityChecker(@Qualifier("supportFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete supportActivitiesCompletionCheck(
            @Qualifier("supportActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    @Bean
    ConfigProviderImpl supportConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    ConfigBasedEnablementStrategy supportConfigBasedEnablementStrategy(
            @Qualifier("supportConfigProvider") ConfigProvider configProvider,
            @Qualifier("supportFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider,
                PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // Policy Validator

    @Bean
    SupportPolicyValidator supportPolicyValidator() {
        return new SupportPolicyValidator();
    }

    // Transition Actions

    @Bean
    AssignAgentSupportAction supportAssignAgentAction() {
        return new AssignAgentSupportAction();
    }

    @Bean
    StartWorkSupportAction supportStartWorkAction() {
        return new StartWorkSupportAction();
    }

    @Bean
    WaitOnCustomerSupportAction supportWaitOnCustomerAction() {
        return new WaitOnCustomerSupportAction();
    }

    @Bean
    ReplySupportAction supportReplyAction() {
        return new ReplySupportAction();
    }

    @Bean
    ResolveSupportAction supportResolveAction() {
        return new ResolveSupportAction();
    }

    @Bean
    ReopenSupportAction supportReopenAction() {
        return new ReopenSupportAction();
    }

    @Bean
    EscalateSupportAction supportEscalateAction() {
        return new EscalateSupportAction();
    }

    @Bean
    CloseSupportAction supportCloseAction() {
        return new CloseSupportAction();
    }

    // Post Save Hooks

    @Bean
    OPENSupportPostSaveHook supportOPENPostSaveHook() {
        return new OPENSupportPostSaveHook();
    }

    @Bean
    ASSIGNEDSupportPostSaveHook supportASSIGNEDPostSaveHook() {
        return new ASSIGNEDSupportPostSaveHook();
    }

    @Bean
    IN_PROGRESSSupportPostSaveHook supportIN_PROGRESSPostSaveHook() {
        return new IN_PROGRESSSupportPostSaveHook();
    }

    @Bean
    RESOLVEDSupportPostSaveHook supportRESOLVEDPostSaveHook() {
        return new RESOLVEDSupportPostSaveHook();
    }

    @Bean
    ESCALATEDSupportPostSaveHook supportESCALATEDPostSaveHook() {
        return new ESCALATEDSupportPostSaveHook();
    }

    @Bean
    CLOSEDSupportPostSaveHook supportCLOSEDPostSaveHook() {
        return new CLOSEDSupportPostSaveHook();
    }

    @Bean
    REOPENEDSupportPostSaveHook supportREOPENEDPostSaveHook() {
        return new REOPENEDSupportPostSaveHook();
    }

    // Chenile-Kafka Event Handler

    @Bean("supportEventService")
    @org.springframework.boot.autoconfigure.condition.ConditionalOnBean(org.chenile.pubsub.ChenilePub.class)
    SupportEventHandler supportEventService(
            @Qualifier("_supportStateEntityService_") StateEntityServiceImpl<SupportTicket> supportStateEntityService,
            org.chenile.pubsub.ChenilePub chenilePub,
            tools.jackson.databind.ObjectMapper objectMapper) {
        return new SupportEventHandler(supportStateEntityService, chenilePub, objectMapper);
    }

    @Bean
    java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> supportEventAuthoritiesSupplier(
            @Qualifier("supportActionsInfoProvider") STMActionsInfoProvider supportInfoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(supportInfoProvider, false);
        return builder.build();
    }
}
