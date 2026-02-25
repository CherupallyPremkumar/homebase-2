package com.homebase.ecom.supplier.configuration;

import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.service.cmds.*;
import com.homebase.ecom.supplier.service.healthcheck.SupplierHealthChecker;
import com.homebase.ecom.supplier.service.store.SupplierEntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.stm.State;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import com.homebase.ecom.supplier.service.postSaveHooks.*;

/**
 * This is where you will instantiate all the required classes in Spring
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
    EntityStore<Supplier> supplierEntityStore() {
        return new SupplierEntityStore();
    }

    @Bean
    StateEntityServiceImpl<Supplier> _supplierStateEntityService_(
            @Qualifier("supplierEntityStm") STM<Supplier> stm,
            @Qualifier("supplierActionsInfoProvider") STMActionsInfoProvider supplierInfoProvider,
            @Qualifier("supplierEntityStore") EntityStore<Supplier> entityStore) {
        return new StateEntityServiceImpl<>(stm, supplierInfoProvider, entityStore);
    }

    // Now we start constructing the STM Components

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

    // Create the specific transition actions here. Make sure that these actions are
    // inheriting from
    // AbstractSTMTransitionMachine (The sample classes provide an example of this).
    // To automatically wire
    // them into the STM use the convention of "supplier" + eventId + "Action" for
    // the method name. (supplier is the
    // prefix passed to the TransitionActionResolver above.)
    // This will ensure that these are detected automatically by the Workflow
    // system.
    // The payload types will be detected as well so that there is no need to
    // introduce an <event-information/>
    // segment in src/main/resources/com/homebase/supplier/supplier-states.xml

    @Bean
    SupplierFlowEntryAction supplierFlowEntryAction() {
        return new SupplierFlowEntryAction();
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
