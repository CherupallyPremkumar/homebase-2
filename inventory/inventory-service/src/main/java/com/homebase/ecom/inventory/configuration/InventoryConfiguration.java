package com.homebase.ecom.inventory.configuration;

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
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.infrastructure.persistence.mapper.InventoryItemMapper;
import com.homebase.ecom.inventory.infrastructure.persistence.adapter.InventoryItemJpaRepository;
import com.homebase.ecom.inventory.infrastructure.persistence.adapter.InventoryItemRepositoryImpl;
import com.homebase.ecom.inventory.infrastructure.persistence.ChenileInventoryItemEntityStore;
import com.homebase.ecom.inventory.domain.port.InventoryItemRepository;
import com.homebase.ecom.inventory.service.cmds.*;
import com.homebase.ecom.inventory.service.healthcheck.InventoryHealthChecker;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.stm.State;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import com.homebase.ecom.inventory.service.postSaveHooks.*;

/**
 * This is where you will instantiate all the required classes in Spring
 */
@Configuration
public class InventoryConfiguration {
    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/inventory/inventory-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Inventory";
    public static final String PREFIX_FOR_RESOLVER = "inventory";

    @Bean
    BeanFactoryAdapter inventoryBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl inventoryFlowStore(
            @Qualifier("inventoryBeanFactoryAdapter") BeanFactoryAdapter inventoryBeanFactoryAdapter) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(inventoryBeanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean
    STM<InventoryItem> inventoryEntityStm(@Qualifier("inventoryFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<InventoryItem> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider inventoryActionsInfoProvider(
            @Qualifier("inventoryFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("inventory", provider);
        return provider;
    }

    @Bean
    InventoryItemMapper inventoryItemMapper() {
        return new InventoryItemMapper();
    }

    @Bean
    InventoryItemRepositoryImpl inventoryItemRepository(InventoryItemJpaRepository jpaRepository, InventoryItemMapper mapper) {
        return new InventoryItemRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    EntityStore<InventoryItem> inventoryEntityStore(InventoryItemJpaRepository repository, InventoryItemMapper mapper) {
        return new ChenileInventoryItemEntityStore(repository, mapper);
    }

    @Bean
    StateEntityServiceImpl<InventoryItem> _inventoryStateEntityService_(
            @Qualifier("inventoryEntityStm") STM<InventoryItem> stm,
            @Qualifier("inventoryActionsInfoProvider") STMActionsInfoProvider inventoryInfoProvider,
            @Qualifier("inventoryEntityStore") EntityStore<InventoryItem> entityStore) {
        return new StateEntityServiceImpl<>(stm, inventoryInfoProvider, entityStore);
    }

    // Now we start constructing the STM Components

    @Bean
    DefaultPostSaveHook<InventoryItem> inventoryDefaultPostSaveHook(
            @Qualifier("inventoryTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        DefaultPostSaveHook<InventoryItem> postSaveHook = new DefaultPostSaveHook<>(stmTransitionActionResolver);
        return postSaveHook;
    }

    @Bean
    GenericEntryAction<InventoryItem> inventoryEntryAction(
            @Qualifier("inventoryEntityStore") EntityStore<InventoryItem> entityStore,
            @Qualifier("inventoryActionsInfoProvider") STMActionsInfoProvider inventoryInfoProvider,
            @Qualifier("inventoryFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("inventoryDefaultPostSaveHook") DefaultPostSaveHook<InventoryItem> postSaveHook) {
        GenericEntryAction<InventoryItem> entryAction = new GenericEntryAction<InventoryItem>(entityStore,
                inventoryInfoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    DefaultAutomaticStateComputation<InventoryItem> inventoryDefaultAutoState(
            @Qualifier("inventoryTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("inventoryFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<InventoryItem> autoState = new DefaultAutomaticStateComputation<>(
                stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    GenericExitAction<InventoryItem> inventoryExitAction(@Qualifier("inventoryFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<InventoryItem> exitAction = new GenericExitAction<InventoryItem>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    XmlFlowReader inventoryFlowReader(@Qualifier("inventoryFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean
    InventoryHealthChecker inventoryHealthChecker() {
        return new InventoryHealthChecker();
    }

    @Bean
    AdjustSupplierPaymentAction AdjustSupplierPaymentAction() {
        return new AdjustSupplierPaymentAction();
    }

    @Bean
    InventoryDiscardedCleanupAction InventoryDiscardedCleanupAction() {
        return new InventoryDiscardedCleanupAction();
    }

    @Bean
    STMTransitionAction<InventoryItem> defaultinventorySTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver inventoryTransitionActionResolver(
            @Qualifier("defaultinventorySTMTransitionAction") STMTransitionAction<InventoryItem> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean
    StmBodyTypeSelector inventoryBodyTypeSelector(
            @Qualifier("inventoryActionsInfoProvider") STMActionsInfoProvider inventoryInfoProvider,
            @Qualifier("inventoryTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(inventoryInfoProvider, stmTransitionActionResolver);
    }

    @Bean
    STMTransitionAction<InventoryItem> inventoryBaseTransitionAction(
            @Qualifier("inventoryTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("inventoryActivityChecker") ActivityChecker activityChecker,
            @Qualifier("inventoryFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<InventoryItem> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean
    ActivityChecker inventoryActivityChecker(@Qualifier("inventoryFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete activitiesCompletionCheck(
            @Qualifier("inventoryActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    // Create the specific transition actions here. Make sure that these actions are
    // inheriting from
    // AbstractSTMTransitionMachine (The sample classes provide an example of this).
    // To automatically wire
    // them into the STM use the convention of "inventory" + eventId + "Action" for
    // the method name. (inventory is the
    // prefix passed to the TransitionActionResolver above.)
    // This will ensure that these are detected automatically by the Workflow
    // system.
    // The payload types will be detected as well so that there is no need to
    // introduce an <event-information/>
    // segment in src/main/resources/com/homebase/inventory/inventory-states.xml

    @Bean
    ReserveStockInventoryItemAction inventoryReserveStockAction() {
        return new ReserveStockInventoryItemAction();
    }

    @Bean
    SellAllStockInventoryItemAction inventorySellAllStockAction() {
        return new SellAllStockInventoryItemAction();
    }

    @Bean
    ReturnCompletedInventoryItemAction inventoryReturnCompletedAction() {
        return new ReturnCompletedInventoryItemAction();
    }

    @Bean
    ReturnRejectedStockInventoryItemAction inventoryReturnRejectedStockAction() {
        return new ReturnRejectedStockInventoryItemAction();
    }

    @Bean
    RepairDamagedsInventoryItemAction inventoryRepairDamagedsAction() {
        return new RepairDamagedsInventoryItemAction();
    }

    @Bean
    DiscardDamagedInventoryItemAction inventoryDiscardDamagedAction() {
        return new DiscardDamagedInventoryItemAction();
    }

    @Bean
    InspectStockInventoryItemAction inventoryInspectStockAction() {
        return new InspectStockInventoryItemAction();
    }

    @Bean
    AllocateToWarehouseInventoryItemAction inventoryAllocateToWarehouseAction() {
        return new AllocateToWarehouseInventoryItemAction();
    }

    @Bean
    RepairDamagedInventoryItemAction inventoryRepairDamagedAction() {
        return new RepairDamagedInventoryItemAction();
    }

    @Bean
    RestockArriveInventoryItemAction inventoryRestockArriveAction() {
        return new RestockArriveInventoryItemAction();
    }

    @Bean
    ReturnDamagedInventoryItemAction inventoryReturnDamagedAction() {
        return new ReturnDamagedInventoryItemAction();
    }

    @Bean
    ReturnToSupplierInventoryItemAction inventoryReturnToSupplierAction() {
        return new ReturnToSupplierInventoryItemAction();
    }

    @Bean
    ApproveStockInventoryItemAction inventoryApproveStockAction() {
        return new ApproveStockInventoryItemAction();
    }

    @Bean
    RejectStockInventoryItemAction inventoryRejectStockAction() {
        return new RejectStockInventoryItemAction();
    }

    @Bean
    DamageFoundInventoryItemAction inventoryDamageFoundAction() {
        return new DamageFoundInventoryItemAction();
    }

    @Bean
    ReleaseReservedStockInventoryItemAction inventoryReleaseReservedStockAction() {
        return new ReleaseReservedStockInventoryItemAction();
    }

    @Bean
    SoldAllReservedInventoryItemAction inventorySoldAllReservedAction() {
        return new SoldAllReservedInventoryItemAction();
    }

    @Bean
    CheckInventoryItemStatusAction inventoryCheckStatusAction() {
        return new CheckInventoryItemStatusAction();
    }

    @Bean
    ConfigProviderImpl inventoryConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    ConfigBasedEnablementStrategy inventoryConfigBasedEnablementStrategy(
            @Qualifier("inventoryConfigProvider") ConfigProvider configProvider,
            @Qualifier("inventoryFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider,
                PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    @Bean
    DAMAGED_AT_WAREHOUSEInventoryItemPostSaveHook inventoryDAMAGED_AT_WAREHOUSEPostSaveHook() {
        return new DAMAGED_AT_WAREHOUSEInventoryItemPostSaveHook();
    }

    @Bean
    STOCK_PENDINGInventoryItemPostSaveHook inventorySTOCK_PENDINGPostSaveHook() {
        return new STOCK_PENDINGInventoryItemPostSaveHook();
    }

    @Bean
    STOCK_APPROVEDInventoryItemPostSaveHook inventorySTOCK_APPROVEDPostSaveHook() {
        return new STOCK_APPROVEDInventoryItemPostSaveHook();
    }

    @Bean
    IN_WAREHOUSEInventoryItemPostSaveHook inventoryIN_WAREHOUSEPostSaveHook() {
        return new IN_WAREHOUSEInventoryItemPostSaveHook();
    }

    @Bean
    RETURNED_TO_SUPPLIERInventoryItemPostSaveHook inventoryRETURNED_TO_SUPPLIERPostSaveHook() {
        return new RETURNED_TO_SUPPLIERInventoryItemPostSaveHook();
    }

    @Bean
    STOCK_INSPECTIONInventoryItemPostSaveHook inventorySTOCK_INSPECTIONPostSaveHook() {
        return new STOCK_INSPECTIONInventoryItemPostSaveHook();
    }

    @Bean
    OUT_OF_STOCKInventoryItemPostSaveHook inventoryOUT_OF_STOCKPostSaveHook() {
        return new OUT_OF_STOCKInventoryItemPostSaveHook();
    }

    @Bean
    RETURN_TO_SUPPLIERInventoryItemPostSaveHook inventoryRETURN_TO_SUPPLIERPostSaveHook() {
        return new RETURN_TO_SUPPLIERInventoryItemPostSaveHook();
    }

    @Bean
    STOCK_REJECTEDInventoryItemPostSaveHook inventorySTOCK_REJECTEDPostSaveHook() {
        return new STOCK_REJECTEDInventoryItemPostSaveHook();
    }

    @Bean
    DISCARDEDInventoryItemPostSaveHook inventoryDISCARDEDPostSaveHook() {
        return new DISCARDEDInventoryItemPostSaveHook();
    }

    @Bean
    PARTIAL_DAMAGEInventoryItemPostSaveHook inventoryPARTIAL_DAMAGEPostSaveHook() {
        return new PARTIAL_DAMAGEInventoryItemPostSaveHook();
    }

    @Bean
    PARTIALLY_RESERVEDInventoryItemPostSaveHook inventoryPARTIALLY_RESERVEDPostSaveHook() {
        return new PARTIALLY_RESERVEDInventoryItemPostSaveHook();
    }

}
