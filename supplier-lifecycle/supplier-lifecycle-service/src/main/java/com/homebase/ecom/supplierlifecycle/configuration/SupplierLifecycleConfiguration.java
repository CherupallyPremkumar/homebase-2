package com.homebase.ecom.supplierlifecycle.configuration;

import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;

import com.homebase.ecom.supplierlifecycle.domain.model.SupplierLifecycleSaga;
import com.homebase.ecom.supplierlifecycle.infrastructure.persistence.ChenileSupplierLifecycleSagaEntityStore;
import com.homebase.ecom.supplierlifecycle.infrastructure.persistence.adapter.SupplierLifecycleSagaJpaRepository;
import com.homebase.ecom.supplierlifecycle.infrastructure.persistence.adapter.SupplierLifecycleSagaRepositoryImpl;
import com.homebase.ecom.supplierlifecycle.infrastructure.persistence.mapper.SupplierLifecycleSagaMapper;
import com.homebase.ecom.supplierlifecycle.domain.port.SupplierLifecycleSagaRepository;
import com.homebase.ecom.supplierlifecycle.service.cmds.*;
import com.homebase.ecom.supplierlifecycle.service.postSaveHooks.*;

/**
 * Full STM+OWIZ configuration for the supplier lifecycle saga.
 * Replaces the previous pure-OWIZ chain configuration.
 */
@Configuration
public class SupplierLifecycleConfiguration {
    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/supplierlifecycle/supplier-lifecycle-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "SupplierLifecycle";
    public static final String PREFIX_FOR_RESOLVER = "supplierLifecycle";

    // --- STM Infrastructure ---

    @Bean
    BeanFactoryAdapter supplierLifecycleBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl supplierLifecycleFlowStore(
            @Qualifier("supplierLifecycleBeanFactoryAdapter") BeanFactoryAdapter beanFactoryAdapter) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(beanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean
    STM<SupplierLifecycleSaga> supplierLifecycleEntityStm(
            @Qualifier("supplierLifecycleFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<SupplierLifecycleSaga> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider supplierLifecycleActionsInfoProvider(
            @Qualifier("supplierLifecycleFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("supplierLifecycle", provider);
        return provider;
    }

    // --- Persistence Layer ---

    @Bean
    SupplierLifecycleSagaMapper supplierLifecycleSagaMapper() {
        return new SupplierLifecycleSagaMapper();
    }

    @Bean
    SupplierLifecycleSagaRepositoryImpl supplierLifecycleSagaRepository(
            SupplierLifecycleSagaJpaRepository jpaRepository, SupplierLifecycleSagaMapper mapper) {
        return new SupplierLifecycleSagaRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    EntityStore<SupplierLifecycleSaga> supplierLifecycleEntityStore(
            SupplierLifecycleSagaJpaRepository repository, SupplierLifecycleSagaMapper mapper) {
        return new ChenileSupplierLifecycleSagaEntityStore(repository, mapper);
    }

    // --- State Entity Service ---

    @Bean
    StateEntityServiceImpl<SupplierLifecycleSaga> _supplierLifecycleStateEntityService_(
            @Qualifier("supplierLifecycleEntityStm") STM<SupplierLifecycleSaga> stm,
            @Qualifier("supplierLifecycleActionsInfoProvider") STMActionsInfoProvider infoProvider,
            @Qualifier("supplierLifecycleEntityStore") EntityStore<SupplierLifecycleSaga> entityStore) {
        return new StateEntityServiceImpl<>(stm, infoProvider, entityStore);
    }

    // --- STM Transition Action Resolver ---

    @Bean
    STMTransitionAction<SupplierLifecycleSaga> defaultSupplierLifecycleSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver supplierLifecycleTransitionActionResolver(
            @Qualifier("defaultSupplierLifecycleSTMTransitionAction") STMTransitionAction<SupplierLifecycleSaga> defaultAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultAction, true);
    }

    // --- STM Hooks ---

    @Bean
    DefaultPostSaveHook<SupplierLifecycleSaga> supplierLifecycleDefaultPostSaveHook(
            @Qualifier("supplierLifecycleTransitionActionResolver") STMTransitionActionResolver resolver) {
        return new DefaultPostSaveHook<>(resolver);
    }

    @Bean
    GenericEntryAction<SupplierLifecycleSaga> supplierLifecycleEntryAction(
            @Qualifier("supplierLifecycleEntityStore") EntityStore<SupplierLifecycleSaga> entityStore,
            @Qualifier("supplierLifecycleActionsInfoProvider") STMActionsInfoProvider infoProvider,
            @Qualifier("supplierLifecycleFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("supplierLifecycleDefaultPostSaveHook") DefaultPostSaveHook<SupplierLifecycleSaga> postSaveHook) {
        GenericEntryAction<SupplierLifecycleSaga> entryAction = new GenericEntryAction<>(entityStore, infoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    DefaultAutomaticStateComputation<SupplierLifecycleSaga> supplierLifecycleDefaultAutoState(
            @Qualifier("supplierLifecycleTransitionActionResolver") STMTransitionActionResolver resolver,
            @Qualifier("supplierLifecycleFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<SupplierLifecycleSaga> autoState = new DefaultAutomaticStateComputation<>(resolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    GenericExitAction<SupplierLifecycleSaga> supplierLifecycleExitAction(
            @Qualifier("supplierLifecycleFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<SupplierLifecycleSaga> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    XmlFlowReader supplierLifecycleFlowReader(
            @Qualifier("supplierLifecycleFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean
    STMTransitionAction<SupplierLifecycleSaga> supplierLifecycleBaseTransitionAction(
            @Qualifier("supplierLifecycleTransitionActionResolver") STMTransitionActionResolver resolver,
            @Qualifier("supplierLifecycleActivityChecker") ActivityChecker activityChecker,
            @Qualifier("supplierLifecycleFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<SupplierLifecycleSaga> baseAction = new BaseTransitionAction<>(resolver);
        baseAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseAction);
        return baseAction;
    }

    @Bean
    ActivityChecker supplierLifecycleActivityChecker(
            @Qualifier("supplierLifecycleFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete supplierLifecycleActivitiesCompletionCheck(
            @Qualifier("supplierLifecycleActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    @Bean
    StmBodyTypeSelector supplierLifecycleBodyTypeSelector(
            @Qualifier("supplierLifecycleActionsInfoProvider") STMActionsInfoProvider infoProvider,
            @Qualifier("supplierLifecycleTransitionActionResolver") STMTransitionActionResolver resolver) {
        return new StmBodyTypeSelector(infoProvider, resolver);
    }

    @Bean
    ConfigProviderImpl supplierLifecycleConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    ConfigBasedEnablementStrategy supplierLifecycleConfigBasedEnablementStrategy(
            @Qualifier("supplierLifecycleConfigProvider") ConfigProvider configProvider,
            @Qualifier("supplierLifecycleFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider, PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // --- Suspend-flow Transition Actions ---
    // Convention: PREFIX_FOR_RESOLVER + eventId + "Action"

    @Bean
    DisableProductsAction supplierLifecycleDisableProductsAction() {
        return new DisableProductsAction();
    }

    @Bean
    RemoveCatalogAction supplierLifecycleRemoveCatalogAction() {
        return new RemoveCatalogAction();
    }

    @Bean
    FreezeInventoryAction supplierLifecycleFreezeInventoryAction() {
        return new FreezeInventoryAction();
    }

    @Bean
    CancelOrdersAction supplierLifecycleCancelOrdersAction() {
        return new CancelOrdersAction();
    }

    @Bean
    NotifyCustomersAction supplierLifecycleNotifyCustomersAction() {
        return new NotifyCustomersAction();
    }

    // --- Reactivate-flow Transition Actions ---

    @Bean
    EnableProductsAction supplierLifecycleEnableProductsAction() {
        return new EnableProductsAction();
    }

    @Bean
    RestoreCatalogAction supplierLifecycleRestoreCatalogAction() {
        return new RestoreCatalogAction();
    }

    @Bean
    UnfreezeInventoryAction supplierLifecycleUnfreezeInventoryAction() {
        return new UnfreezeInventoryAction();
    }

    @Bean
    NotifySupplierAction supplierLifecycleNotifySupplierAction() {
        return new NotifySupplierAction();
    }

    // --- Shared Transition Actions ---

    @Bean
    RetryAction supplierLifecycleRetryAction() {
        return new RetryAction();
    }

    // --- PostSaveHooks (one per state) ---

    @Bean
    INITIATEDSupplierLifecycleSagaPostSaveHook supplierLifecycleINITIATEDPostSaveHook() {
        return new INITIATEDSupplierLifecycleSagaPostSaveHook();
    }

    @Bean
    PRODUCTS_DISABLEDSupplierLifecycleSagaPostSaveHook supplierLifecyclePRODUCTS_DISABLEDPostSaveHook() {
        return new PRODUCTS_DISABLEDSupplierLifecycleSagaPostSaveHook();
    }

    @Bean
    CATALOG_CLEAREDSupplierLifecycleSagaPostSaveHook supplierLifecycleCATALOG_CLEAREDPostSaveHook() {
        return new CATALOG_CLEAREDSupplierLifecycleSagaPostSaveHook();
    }

    @Bean
    INVENTORY_FROZENSupplierLifecycleSagaPostSaveHook supplierLifecycleINVENTORY_FROZENPostSaveHook() {
        return new INVENTORY_FROZENSupplierLifecycleSagaPostSaveHook();
    }

    @Bean
    ORDERS_CANCELLEDSupplierLifecycleSagaPostSaveHook supplierLifecycleORDERS_CANCELLEDPostSaveHook() {
        return new ORDERS_CANCELLEDSupplierLifecycleSagaPostSaveHook();
    }

    @Bean
    COMPLETEDSupplierLifecycleSagaPostSaveHook supplierLifecycleCOMPLETEDPostSaveHook() {
        return new COMPLETEDSupplierLifecycleSagaPostSaveHook();
    }

    @Bean
    FAILEDSupplierLifecycleSagaPostSaveHook supplierLifecycleFAILEDPostSaveHook() {
        return new FAILEDSupplierLifecycleSagaPostSaveHook();
    }

    @Bean
    PRODUCTS_ENABLEDSupplierLifecycleSagaPostSaveHook supplierLifecyclePRODUCTS_ENABLEDPostSaveHook() {
        return new PRODUCTS_ENABLEDSupplierLifecycleSagaPostSaveHook();
    }

    @Bean
    CATALOG_RESTOREDSupplierLifecycleSagaPostSaveHook supplierLifecycleCATALOG_RESTOREDPostSaveHook() {
        return new CATALOG_RESTOREDSupplierLifecycleSagaPostSaveHook();
    }

    @Bean
    INVENTORY_UNFROZENSupplierLifecycleSagaPostSaveHook supplierLifecycleINVENTORY_UNFROZENPostSaveHook() {
        return new INVENTORY_UNFROZENSupplierLifecycleSagaPostSaveHook();
    }

    @Bean
    java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> supplierLifecycleEventAuthoritiesSupplier(
            @Qualifier("supplierLifecycleActionsInfoProvider") STMActionsInfoProvider supplierLifecycleInfoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(supplierLifecycleInfoProvider, true);
        return builder.build();
    }
}
