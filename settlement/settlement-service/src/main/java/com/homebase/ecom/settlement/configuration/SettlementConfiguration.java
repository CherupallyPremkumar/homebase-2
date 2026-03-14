package com.homebase.ecom.settlement.configuration;

import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.chenile.proxy.builder.ProxyBuilder;

import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.service.cmds.*;
import com.homebase.ecom.settlement.service.healthcheck.SettlementHealthChecker;
import com.homebase.ecom.settlement.infrastructure.persistence.ChenileSettlementEntityStore;
import com.homebase.ecom.settlement.infrastructure.persistence.adapter.SettlementJpaRepository;
import com.homebase.ecom.settlement.infrastructure.persistence.mapper.SettlementMapper;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.stm.State;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import com.homebase.ecom.settlement.service.postSaveHooks.*;

/**
 * This is where you will instantiate all the required classes in Spring
 */
@Configuration
public class SettlementConfiguration {
    @Autowired
    private ProxyBuilder proxyBuilder;

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
    StateEntityServiceImpl<Settlement> _settlementStateEntityService_(
            @Qualifier("settlementEntityStm") STM<Settlement> stm,
            @Qualifier("settlementActionsInfoProvider") STMActionsInfoProvider settlementInfoProvider,
            @Qualifier("settlementEntityStore") EntityStore<Settlement> entityStore) {
        return new StateEntityServiceImpl<>(stm, settlementInfoProvider, entityStore);
    }

    // Now we start constructing the STM Components

    @Bean
    DefaultPostSaveHook<Settlement> settlementDefaultPostSaveHook(
            @Qualifier("settlementTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        DefaultPostSaveHook<Settlement> postSaveHook = new DefaultPostSaveHook<>(stmTransitionActionResolver);
        return postSaveHook;
    }

    @Bean
    GenericEntryAction<Settlement> settlementEntryAction(
            @Qualifier("settlementEntityStore") EntityStore<Settlement> entityStore,
            @Qualifier("settlementActionsInfoProvider") STMActionsInfoProvider settlementInfoProvider,
            @Qualifier("settlementFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("settlementDefaultPostSaveHook") DefaultPostSaveHook<Settlement> postSaveHook) {
        GenericEntryAction<Settlement> entryAction = new GenericEntryAction<Settlement>(entityStore,
                settlementInfoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    DefaultAutomaticStateComputation<Settlement> settlementDefaultAutoState(
            @Qualifier("settlementTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("settlementFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<Settlement> autoState = new DefaultAutomaticStateComputation<>(
                stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    GenericExitAction<Settlement> settlementExitAction(
            @Qualifier("settlementFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<Settlement> exitAction = new GenericExitAction<Settlement>();
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
    AreActivitiesComplete activitiesCompletionCheck(
            @Qualifier("settlementActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    // Create the specific transition actions here. Make sure that these actions are
    // inheriting from
    // AbstractSTMTransitionMachine (The sample classes provide an example of this).
    // To automatically wire
    // them into the STM use the convention of "settlement" + eventId + "Action" for
    // the method name. (settlement is the
    // prefix passed to the TransitionActionResolver above.)
    // This will ensure that these are detected automatically by the Workflow
    // system.
    // The payload types will be detected as well so that there is no need to
    // introduce an <event-information/>
    // segment in src/main/resources/com/homebase/settlement/settlement-states.xml

    @Bean
    RetrySettlementSettlementAction settlementRetrySettlementAction() {
        return new RetrySettlementSettlementAction();
    }

    @Bean
    MonthEndSettlementAction settlementMonthEndAction() {
        return new MonthEndSettlementAction();
    }

    @Bean
    RejectSettlementSettlementAction settlementRejectSettlementAction() {
        return new RejectSettlementSettlementAction();
    }

    @Bean
    CalculateSettlementSettlementAction settlementCalculateSettlementAction() {
        return new CalculateSettlementSettlementAction();
    }

    @Bean
    public ConfirmPayoutAction settlementConfirmPayoutAction() {
        return new ConfirmPayoutAction();
    }

    @Bean
    ConfigProviderImpl settlementConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    ConfigBasedEnablementStrategy settlementConfigBasedEnablementStrategy(
            @Qualifier("settlementConfigProvider") ConfigProvider configProvider,
            @Qualifier("settlementFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider,
                PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    @Bean
    READY_FOR_PAYMENTSettlementPostSaveHook settlementREADY_FOR_PAYMENTPostSaveHook() {
        return new READY_FOR_PAYMENTSettlementPostSaveHook();
    }

    @Bean
    SETTLEDSettlementPostSaveHook settlementSETTLEDPostSaveHook() {
        return new SETTLEDSettlementPostSaveHook();
    }

    @Bean
    FAILEDSettlementPostSaveHook settlementFAILEDPostSaveHook() {
        return new FAILEDSettlementPostSaveHook();
    }

    @Bean
    PROCESSINGSettlementPostSaveHook settlementPROCESSINGPostSaveHook() {
        return new PROCESSINGSettlementPostSaveHook();
    }

    @Bean
    PENDINGSettlementPostSaveHook settlementPENDINGPostSaveHook() {
        return new PENDINGSettlementPostSaveHook();
    }

}
