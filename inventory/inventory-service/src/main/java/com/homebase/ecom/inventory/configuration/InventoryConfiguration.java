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
import com.homebase.ecom.inventory.model.Inventory;
import com.homebase.ecom.inventory.service.cmds.*;
import com.homebase.ecom.inventory.service.healthcheck.InventoryHealthChecker;
import com.homebase.ecom.inventory.service.store.InventoryEntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.stm.State;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import com.homebase.ecom.inventory.service.postSaveHooks.*;

/**
 This is where you will instantiate all the required classes in Spring
*/
@Configuration
public class InventoryConfiguration {
	private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/inventory/inventory-states.xml";
	public static final String PREFIX_FOR_PROPERTIES = "Inventory";
    public static final String PREFIX_FOR_RESOLVER = "inventory";

    @Bean BeanFactoryAdapter inventoryBeanFactoryAdapter() {
		return new SpringBeanFactoryAdapter();
	}
	
	@Bean STMFlowStoreImpl inventoryFlowStore(
            @Qualifier("inventoryBeanFactoryAdapter") BeanFactoryAdapter inventoryBeanFactoryAdapter
            )throws Exception{
		STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
		stmFlowStore.setBeanFactory(inventoryBeanFactoryAdapter);
		return stmFlowStore;
	}
	
	@Bean  STM<Inventory> inventoryEntityStm(@Qualifier("inventoryFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception{
		STMImpl<Inventory> stm = new STMImpl<>();		
		stm.setStmFlowStore(stmFlowStore);
		return stm;
	}
	
	@Bean  STMActionsInfoProvider inventoryActionsInfoProvider(@Qualifier("inventoryFlowStore") STMFlowStoreImpl stmFlowStore) {
		STMActionsInfoProvider provider =  new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("inventory",provider);
        return provider;
	}
	
	@Bean EntityStore<Inventory> inventoryEntityStore() {
		return new InventoryEntityStore();
	}
	
	@Bean  StateEntityServiceImpl<Inventory> _inventoryStateEntityService_(
			@Qualifier("inventoryEntityStm") STM<Inventory> stm,
			@Qualifier("inventoryActionsInfoProvider") STMActionsInfoProvider inventoryInfoProvider,
			@Qualifier("inventoryEntityStore") EntityStore<Inventory> entityStore){
		return new StateEntityServiceImpl<>(stm, inventoryInfoProvider, entityStore);
	}
	
	// Now we start constructing the STM Components 


    @Bean  DefaultPostSaveHook<Inventory> inventoryDefaultPostSaveHook(
    @Qualifier("inventoryTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver){
    DefaultPostSaveHook<Inventory> postSaveHook = new DefaultPostSaveHook<>(stmTransitionActionResolver);
    return postSaveHook;
    }

    @Bean  GenericEntryAction<Inventory> inventoryEntryAction(@Qualifier("inventoryEntityStore") EntityStore<Inventory> entityStore,
    @Qualifier("inventoryActionsInfoProvider") STMActionsInfoProvider inventoryInfoProvider,
    @Qualifier("inventoryFlowStore") STMFlowStoreImpl stmFlowStore,
    @Qualifier("inventoryDefaultPostSaveHook") DefaultPostSaveHook<Inventory> postSaveHook)  {
    GenericEntryAction<Inventory> entryAction =  new GenericEntryAction<Inventory>(entityStore,inventoryInfoProvider,postSaveHook);
    stmFlowStore.setEntryAction(entryAction);
    return entryAction;
    }

    @Bean  DefaultAutomaticStateComputation<Inventory> inventoryDefaultAutoState(
    @Qualifier("inventoryTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
    @Qualifier("inventoryFlowStore") STMFlowStoreImpl stmFlowStore){
    DefaultAutomaticStateComputation<Inventory> autoState = new DefaultAutomaticStateComputation<>(stmTransitionActionResolver);
    stmFlowStore.setDefaultAutomaticStateComputation(autoState);
    return autoState;
    }

	@Bean GenericExitAction<Inventory> inventoryExitAction(@Qualifier("inventoryFlowStore") STMFlowStoreImpl stmFlowStore){
        GenericExitAction<Inventory> exitAction = new GenericExitAction<Inventory>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
	}

	@Bean
	XmlFlowReader inventoryFlowReader(@Qualifier("inventoryFlowStore") STMFlowStoreImpl flowStore) throws Exception {
		XmlFlowReader flowReader = new XmlFlowReader(flowStore);
		flowReader.setFilename(FLOW_DEFINITION_FILE);
		return flowReader;
	}
	

	@Bean InventoryHealthChecker inventoryHealthChecker(){
    	return new InventoryHealthChecker();
    }

    @Bean STMTransitionAction<Inventory> defaultinventorySTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver inventoryTransitionActionResolver(
    @Qualifier("defaultinventorySTMTransitionAction") STMTransitionAction<Inventory> defaultSTMTransitionAction){
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER,defaultSTMTransitionAction,true);
    }

    @Bean  StmBodyTypeSelector inventoryBodyTypeSelector(
    @Qualifier("inventoryActionsInfoProvider") STMActionsInfoProvider inventoryInfoProvider,
    @Qualifier("inventoryTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(inventoryInfoProvider,stmTransitionActionResolver);
    }


    @Bean  STMTransitionAction<Inventory> inventoryBaseTransitionAction(
        @Qualifier("inventoryTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
        @Qualifier("inventoryActivityChecker") ActivityChecker activityChecker,
        @Qualifier("inventoryFlowStore") STMFlowStoreImpl stmFlowStore){
        BaseTransitionAction<Inventory> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean ActivityChecker inventoryActivityChecker(@Qualifier("inventoryFlowStore") STMFlowStoreImpl stmFlowStore){
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete activitiesCompletionCheck(@Qualifier("inventoryActivityChecker") ActivityChecker activityChecker){
        return new AreActivitiesComplete(activityChecker);
    }

    // Create the specific transition actions here. Make sure that these actions are inheriting from
    // AbstractSTMTransitionMachine (The sample classes provide an example of this). To automatically wire
    // them into the STM use the convention of "inventory" + eventId + "Action" for the method name. (inventory is the
    // prefix passed to the TransitionActionResolver above.)
    // This will ensure that these are detected automatically by the Workflow system.
    // The payload types will be detected as well so that there is no need to introduce an <event-information/>
    // segment in src/main/resources/com/homebase/inventory/inventory-states.xml


    @Bean ReserveStockInventoryAction
            inventoryReserveStockAction(){
        return new ReserveStockInventoryAction();
    }

    @Bean SellAllStockInventoryAction
            inventorySellAllStockAction(){
        return new SellAllStockInventoryAction();
    }

    @Bean ReturnCompletedInventoryAction
            inventoryReturnCompletedAction(){
        return new ReturnCompletedInventoryAction();
    }

    @Bean ReturnRejectedStockInventoryAction
            inventoryReturnRejectedStockAction(){
        return new ReturnRejectedStockInventoryAction();
    }

    @Bean RepairDamagedsInventoryAction
            inventoryRepairDamagedsAction(){
        return new RepairDamagedsInventoryAction();
    }

    @Bean DiscardDamagedInventoryAction
            inventoryDiscardDamagedAction(){
        return new DiscardDamagedInventoryAction();
    }

    @Bean InspectStockInventoryAction
            inventoryInspectStockAction(){
        return new InspectStockInventoryAction();
    }

    @Bean AllocateToWarehouseInventoryAction
            inventoryAllocateToWarehouseAction(){
        return new AllocateToWarehouseInventoryAction();
    }

    @Bean RepairDamagedInventoryAction
            inventoryRepairDamagedAction(){
        return new RepairDamagedInventoryAction();
    }

    @Bean RestockArriveInventoryAction
            inventoryRestockArriveAction(){
        return new RestockArriveInventoryAction();
    }

    @Bean ReturnDamagedInventoryAction
            inventoryReturnDamagedAction(){
        return new ReturnDamagedInventoryAction();
    }

    @Bean ReturnToSupplierInventoryAction
            inventoryReturnToSupplierAction(){
        return new ReturnToSupplierInventoryAction();
    }

    @Bean ApproveStockInventoryAction
            inventoryApproveStockAction(){
        return new ApproveStockInventoryAction();
    }

    @Bean RejectStockInventoryAction
            inventoryRejectStockAction(){
        return new RejectStockInventoryAction();
    }

    @Bean DamageFoundInventoryAction
            inventoryDamageFoundAction(){
        return new DamageFoundInventoryAction();
    }

    @Bean ReserveMoreStockInventoryAction
            inventoryReserveMoreStockAction(){
        return new ReserveMoreStockInventoryAction();
    }

    @Bean ReleaseReservedStockInventoryAction
            inventoryReleaseReservedStockAction(){
        return new ReleaseReservedStockInventoryAction();
    }

    @Bean SoldAllReservedInventoryAction
            inventorySoldAllReservedAction(){
        return new SoldAllReservedInventoryAction();
    }


    @Bean ConfigProviderImpl inventoryConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean ConfigBasedEnablementStrategy inventoryConfigBasedEnablementStrategy(
        @Qualifier("inventoryConfigProvider") ConfigProvider configProvider,
        @Qualifier("inventoryFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider,PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }



    @Bean DAMAGED_AT_WAREHOUSEInventoryPostSaveHook
        inventoryDAMAGED_AT_WAREHOUSEPostSaveHook(){
            return new DAMAGED_AT_WAREHOUSEInventoryPostSaveHook();
    }

    @Bean STOCK_PENDINGInventoryPostSaveHook
        inventorySTOCK_PENDINGPostSaveHook(){
            return new STOCK_PENDINGInventoryPostSaveHook();
    }

    @Bean STOCK_APPROVEDInventoryPostSaveHook
        inventorySTOCK_APPROVEDPostSaveHook(){
            return new STOCK_APPROVEDInventoryPostSaveHook();
    }

    @Bean IN_WAREHOUSEInventoryPostSaveHook
        inventoryIN_WAREHOUSEPostSaveHook(){
            return new IN_WAREHOUSEInventoryPostSaveHook();
    }

    @Bean RETURNED_TO_SUPPLIERInventoryPostSaveHook
        inventoryRETURNED_TO_SUPPLIERPostSaveHook(){
            return new RETURNED_TO_SUPPLIERInventoryPostSaveHook();
    }

    @Bean STOCK_INSPECTIONInventoryPostSaveHook
        inventorySTOCK_INSPECTIONPostSaveHook(){
            return new STOCK_INSPECTIONInventoryPostSaveHook();
    }

    @Bean OUT_OF_STOCKInventoryPostSaveHook
        inventoryOUT_OF_STOCKPostSaveHook(){
            return new OUT_OF_STOCKInventoryPostSaveHook();
    }

    @Bean RETURN_TO_SUPPLIERInventoryPostSaveHook
        inventoryRETURN_TO_SUPPLIERPostSaveHook(){
            return new RETURN_TO_SUPPLIERInventoryPostSaveHook();
    }

    @Bean STOCK_REJECTEDInventoryPostSaveHook
        inventorySTOCK_REJECTEDPostSaveHook(){
            return new STOCK_REJECTEDInventoryPostSaveHook();
    }

    @Bean DISCARDEDInventoryPostSaveHook
        inventoryDISCARDEDPostSaveHook(){
            return new DISCARDEDInventoryPostSaveHook();
    }

    @Bean PARTIAL_DAMAGEInventoryPostSaveHook
        inventoryPARTIAL_DAMAGEPostSaveHook(){
            return new PARTIAL_DAMAGEInventoryPostSaveHook();
    }

    @Bean PARTIALLY_RESERVEDInventoryPostSaveHook
        inventoryPARTIALLY_RESERVEDPostSaveHook(){
            return new PARTIALLY_RESERVEDInventoryPostSaveHook();
    }

}
