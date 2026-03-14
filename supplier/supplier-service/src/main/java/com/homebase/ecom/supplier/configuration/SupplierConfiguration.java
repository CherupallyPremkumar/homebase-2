package com.homebase.ecom.supplier.configuration;

import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.service.cmds.*;
import com.homebase.ecom.supplier.service.healthcheck.SupplierHealthChecker;
import com.homebase.ecom.supplier.infrastructure.persistence.ChenileSupplierEntityStore;
import com.homebase.ecom.supplier.infrastructure.persistence.adapter.SupplierJpaRepository;
import com.homebase.ecom.supplier.infrastructure.persistence.mapper.SupplierMapper;
import com.homebase.ecom.supplier.infrastructure.messaging.SupplierEventPublisherImpl;
import com.homebase.ecom.supplier.domain.port.SupplierEventPublisher;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.stm.State;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import com.homebase.ecom.supplier.service.postSaveHooks.*;

/**
 * Spring configuration for the Supplier bounded context.
 * Registers all STM components, transition actions, entry actions,
 * post-save hooks, and the event publisher.
 */
@Configuration
public class SupplierConfiguration {
    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/supplier/supplier-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Supplier";
    public static final String PREFIX_FOR_RESOLVER = "supplier";

    @Bean
    BeanFactoryAdapter supplierBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl supplierFlowStore(
            @Qualifier("supplierBeanFactoryAdapter") BeanFactoryAdapter supplierBeanFactoryAdapter) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(supplierBeanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean
    STM<Supplier> supplierEntityStm(@Qualifier("supplierFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<Supplier> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider supplierActionsInfoProvider(@Qualifier("supplierFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("supplier", provider);
        return provider;
    }

    @Bean
    SupplierMapper supplierMapper() {
        return new SupplierMapper();
    }

    @Bean
    EntityStore<Supplier> supplierEntityStore(SupplierJpaRepository jpaRepository, SupplierMapper mapper) {
        return new ChenileSupplierEntityStore(jpaRepository, mapper);
    }

    @Bean
    StateEntityServiceImpl<Supplier> _supplierStateEntityService_(
            @Qualifier("supplierEntityStm") STM<Supplier> stm,
            @Qualifier("supplierActionsInfoProvider") STMActionsInfoProvider supplierInfoProvider,
            @Qualifier("supplierEntityStore") EntityStore<Supplier> entityStore) {
        return new StateEntityServiceImpl<>(stm, supplierInfoProvider, entityStore);
    }

    // --- Event Publisher ---

    @Bean
    SupplierEventPublisher supplierEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new SupplierEventPublisherImpl(applicationEventPublisher);
    }

    // --- STM Components ---

    @Bean
    DefaultPostSaveHook<Supplier> supplierDefaultPostSaveHook(
            @Qualifier("supplierTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        DefaultPostSaveHook<Supplier> postSaveHook = new DefaultPostSaveHook<>(stmTransitionActionResolver);
        return postSaveHook;
    }

    @Bean
    GenericEntryAction<Supplier> supplierEntryAction(
            @Qualifier("supplierEntityStore") EntityStore<Supplier> entityStore,
            @Qualifier("supplierActionsInfoProvider") STMActionsInfoProvider supplierInfoProvider,
            @Qualifier("supplierFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("supplierDefaultPostSaveHook") DefaultPostSaveHook<Supplier> postSaveHook) {
        GenericEntryAction<Supplier> entryAction = new GenericEntryAction<Supplier>(entityStore, supplierInfoProvider,
                postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    DefaultAutomaticStateComputation<Supplier> supplierDefaultAutoState(
            @Qualifier("supplierTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("supplierFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<Supplier> autoState = new DefaultAutomaticStateComputation<>(
                stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    GenericExitAction<Supplier> supplierExitAction(@Qualifier("supplierFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<Supplier> exitAction = new GenericExitAction<Supplier>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    XmlFlowReader supplierFlowReader(@Qualifier("supplierFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean
    SupplierHealthChecker supplierHealthChecker() {
        return new SupplierHealthChecker();
    }

    @Bean
    STMTransitionAction<Supplier> defaultsupplierSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver supplierTransitionActionResolver(
            @Qualifier("defaultsupplierSTMTransitionAction") STMTransitionAction<Supplier> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean
    StmBodyTypeSelector supplierBodyTypeSelector(
            @Qualifier("supplierActionsInfoProvider") STMActionsInfoProvider supplierInfoProvider,
            @Qualifier("supplierTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(supplierInfoProvider, stmTransitionActionResolver);
    }

    @Bean
    STMTransitionAction<Supplier> supplierBaseTransitionAction(
            @Qualifier("supplierTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("supplierActivityChecker") ActivityChecker activityChecker,
            @Qualifier("supplierFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<Supplier> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean
    ActivityChecker supplierActivityChecker(@Qualifier("supplierFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete activitiesCompletionCheck(
            @Qualifier("supplierActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    // --- Transition Actions ---
    // Bean names follow convention: "supplier" + eventId + "Action"
    // so STMTransitionActionResolver auto-discovers them.

    @Bean
    SupplierFlowEntryAction supplierFlowEntryAction() {
        return new SupplierFlowEntryAction();
    }

    @Bean
    ApproveSupplierAction supplierApproveSupplierAction() {
        return new ApproveSupplierAction();
    }

    @Bean
    RejectSupplierAction supplierRejectSupplierAction() {
        return new RejectSupplierAction();
    }

    @Bean
    ResubmitSupplierAction supplierResubmitSupplierAction() {
        return new ResubmitSupplierAction();
    }

    @Bean
    SupplierActiveAction supplierActiveAction() {
        return new SupplierActiveAction();
    }

    @Bean
    SuspendSupplierSupplierAction supplierSuspendSupplierAction() {
        return new SuspendSupplierSupplierAction();
    }

    @Bean
    BlacklistSupplierSupplierAction supplierBlacklistSupplierAction() {
        return new BlacklistSupplierSupplierAction();
    }

    @Bean
    ReactivateSupplierSupplierAction supplierReactivateSupplierAction() {
        return new ReactivateSupplierSupplierAction();
    }

    @Bean
    PauseSupplierSupplierAction supplierPauseSupplierAction() {
        return new PauseSupplierSupplierAction();
    }

    @Bean
    SupplierInactiveAction supplierInactiveAction() {
        return new SupplierInactiveAction();
    }

    @Bean
    SupplierSuspendedAction supplierSuspendedAction() {
        return new SupplierSuspendedAction();
    }

    @Bean
    SupplierBlacklistedAction supplierBlacklistedAction() {
        return new SupplierBlacklistedAction();
    }

    @Bean
    DisableAllProductsAction supplierDisableAllProductsAction() {
        return new DisableAllProductsAction();
    }

    @Bean
    ConfigProviderImpl supplierConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    ConfigBasedEnablementStrategy supplierConfigBasedEnablementStrategy(
            @Qualifier("supplierConfigProvider") ConfigProvider configProvider,
            @Qualifier("supplierFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider,
                PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // --- Post-Save Hooks ---

    @Bean
    ACTIVESupplierPostSaveHook supplierACTIVEPostSaveHook() {
        return new ACTIVESupplierPostSaveHook();
    }

    @Bean
    INACTIVESupplierPostSaveHook supplierINACTIVEPostSaveHook() {
        return new INACTIVESupplierPostSaveHook();
    }

    @Bean
    SUSPENDEDSupplierPostSaveHook supplierSUSPENDEDPostSaveHook() {
        return new SUSPENDEDSupplierPostSaveHook();
    }

    @Bean
    BLACKLISTEDSupplierPostSaveHook supplierBLACKLISTEDPostSaveHook() {
        return new BLACKLISTEDSupplierPostSaveHook();
    }

}
