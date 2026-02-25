package com.homebase.ecom.supplierproduct.configuration;

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
import com.homebase.ecom.supplierproduct.model.Supplierproduct;
import com.homebase.ecom.supplierproduct.service.cmds.*;
import com.homebase.ecom.supplierproduct.service.healthcheck.SupplierproductHealthChecker;
import com.homebase.ecom.supplierproduct.service.store.SupplierproductEntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.stm.State;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import com.homebase.ecom.supplierproduct.service.postSaveHooks.*;

/**
 This is where you will instantiate all the required classes in Spring
*/
@Configuration
public class SupplierproductConfiguration {
	private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/supplierproduct/supplierproduct-states.xml";
	public static final String PREFIX_FOR_PROPERTIES = "Supplierproduct";
    public static final String PREFIX_FOR_RESOLVER = "supplierproduct";

    @Bean BeanFactoryAdapter supplierproductBeanFactoryAdapter() {
		return new SpringBeanFactoryAdapter();
	}
	
	@Bean STMFlowStoreImpl supplierproductFlowStore(
            @Qualifier("supplierproductBeanFactoryAdapter") BeanFactoryAdapter supplierproductBeanFactoryAdapter
            )throws Exception{
		STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
		stmFlowStore.setBeanFactory(supplierproductBeanFactoryAdapter);
		return stmFlowStore;
	}
	
	@Bean  STM<Supplierproduct> supplierproductEntityStm(@Qualifier("supplierproductFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception{
		STMImpl<Supplierproduct> stm = new STMImpl<>();		
		stm.setStmFlowStore(stmFlowStore);
		return stm;
	}
	
	@Bean  STMActionsInfoProvider supplierproductActionsInfoProvider(@Qualifier("supplierproductFlowStore") STMFlowStoreImpl stmFlowStore) {
		STMActionsInfoProvider provider =  new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("supplierproduct",provider);
        return provider;
	}
	
	@Bean EntityStore<Supplierproduct> supplierproductEntityStore() {
		return new SupplierproductEntityStore();
	}
	
	@Bean  StateEntityServiceImpl<Supplierproduct> _supplierproductStateEntityService_(
			@Qualifier("supplierproductEntityStm") STM<Supplierproduct> stm,
			@Qualifier("supplierproductActionsInfoProvider") STMActionsInfoProvider supplierproductInfoProvider,
			@Qualifier("supplierproductEntityStore") EntityStore<Supplierproduct> entityStore){
		return new StateEntityServiceImpl<>(stm, supplierproductInfoProvider, entityStore);
	}
	
	// Now we start constructing the STM Components 


    @Bean  DefaultPostSaveHook<Supplierproduct> supplierproductDefaultPostSaveHook(
    @Qualifier("supplierproductTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver){
    DefaultPostSaveHook<Supplierproduct> postSaveHook = new DefaultPostSaveHook<>(stmTransitionActionResolver);
    return postSaveHook;
    }

    @Bean  GenericEntryAction<Supplierproduct> supplierproductEntryAction(@Qualifier("supplierproductEntityStore") EntityStore<Supplierproduct> entityStore,
    @Qualifier("supplierproductActionsInfoProvider") STMActionsInfoProvider supplierproductInfoProvider,
    @Qualifier("supplierproductFlowStore") STMFlowStoreImpl stmFlowStore,
    @Qualifier("supplierproductDefaultPostSaveHook") DefaultPostSaveHook<Supplierproduct> postSaveHook)  {
    GenericEntryAction<Supplierproduct> entryAction =  new GenericEntryAction<Supplierproduct>(entityStore,supplierproductInfoProvider,postSaveHook);
    stmFlowStore.setEntryAction(entryAction);
    return entryAction;
    }

    @Bean  DefaultAutomaticStateComputation<Supplierproduct> supplierproductDefaultAutoState(
    @Qualifier("supplierproductTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
    @Qualifier("supplierproductFlowStore") STMFlowStoreImpl stmFlowStore){
    DefaultAutomaticStateComputation<Supplierproduct> autoState = new DefaultAutomaticStateComputation<>(stmTransitionActionResolver);
    stmFlowStore.setDefaultAutomaticStateComputation(autoState);
    return autoState;
    }

	@Bean GenericExitAction<Supplierproduct> supplierproductExitAction(@Qualifier("supplierproductFlowStore") STMFlowStoreImpl stmFlowStore){
        GenericExitAction<Supplierproduct> exitAction = new GenericExitAction<Supplierproduct>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
	}

	@Bean
	XmlFlowReader supplierproductFlowReader(@Qualifier("supplierproductFlowStore") STMFlowStoreImpl flowStore) throws Exception {
		XmlFlowReader flowReader = new XmlFlowReader(flowStore);
		flowReader.setFilename(FLOW_DEFINITION_FILE);
		return flowReader;
	}
	

	@Bean SupplierproductHealthChecker supplierproductHealthChecker(){
    	return new SupplierproductHealthChecker();
    }

    @Bean STMTransitionAction<Supplierproduct> defaultsupplierproductSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver supplierproductTransitionActionResolver(
    @Qualifier("defaultsupplierproductSTMTransitionAction") STMTransitionAction<Supplierproduct> defaultSTMTransitionAction){
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER,defaultSTMTransitionAction,true);
    }

    @Bean  StmBodyTypeSelector supplierproductBodyTypeSelector(
    @Qualifier("supplierproductActionsInfoProvider") STMActionsInfoProvider supplierproductInfoProvider,
    @Qualifier("supplierproductTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(supplierproductInfoProvider,stmTransitionActionResolver);
    }


    @Bean  STMTransitionAction<Supplierproduct> supplierproductBaseTransitionAction(
        @Qualifier("supplierproductTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
        @Qualifier("supplierproductActivityChecker") ActivityChecker activityChecker,
        @Qualifier("supplierproductFlowStore") STMFlowStoreImpl stmFlowStore){
        BaseTransitionAction<Supplierproduct> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean ActivityChecker supplierproductActivityChecker(@Qualifier("supplierproductFlowStore") STMFlowStoreImpl stmFlowStore){
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete activitiesCompletionCheck(@Qualifier("supplierproductActivityChecker") ActivityChecker activityChecker){
        return new AreActivitiesComplete(activityChecker);
    }

    // Create the specific transition actions here. Make sure that these actions are inheriting from
    // AbstractSTMTransitionMachine (The sample classes provide an example of this). To automatically wire
    // them into the STM use the convention of "supplierproduct" + eventId + "Action" for the method name. (supplierproduct is the
    // prefix passed to the TransitionActionResolver above.)
    // This will ensure that these are detected automatically by the Workflow system.
    // The payload types will be detected as well so that there is no need to introduce an <event-information/>
    // segment in src/main/resources/com/homebase/supplierproduct/supplierproduct-states.xml


    @Bean RejectQualitySupplierproductAction
            supplierproductRejectQualityAction(){
        return new RejectQualitySupplierproductAction();
    }

    @Bean PublishProductSupplierproductAction
            supplierproductPublishProductAction(){
        return new PublishProductSupplierproductAction();
    }

    @Bean DiscontinueProductSupplierproductAction
            supplierproductDiscontinueProductAction(){
        return new DiscontinueProductSupplierproductAction();
    }

    @Bean NeedsReworkSupplierproductAction
            supplierproductNeedsReworkAction(){
        return new NeedsReworkSupplierproductAction();
    }

    @Bean ListProductSupplierproductAction
            supplierproductListProductAction(){
        return new ListProductSupplierproductAction();
    }

    @Bean MarkDiscontinuedSupplierproductAction
            supplierproductMarkDiscontinuedAction(){
        return new MarkDiscontinuedSupplierproductAction();
    }

    @Bean RestockProductSupplierproductAction
            supplierproductRestockProductAction(){
        return new RestockProductSupplierproductAction();
    }

    @Bean ProductDeliveredSupplierproductAction
            supplierproductProductDeliveredAction(){
        return new ProductDeliveredSupplierproductAction();
    }

    @Bean ApproveQualitySupplierproductAction
            supplierproductApproveQualityAction(){
        return new ApproveQualitySupplierproductAction();
    }

    @Bean CancelProductSupplierproductAction
            supplierproductCancelProductAction(){
        return new CancelProductSupplierproductAction();
    }

    @Bean StockSoldOutSupplierproductAction
            supplierproductStockSoldOutAction(){
        return new StockSoldOutSupplierproductAction();
    }

    @Bean RestockArriveSupplierproductAction
            supplierproductRestockArriveAction(){
        return new RestockArriveSupplierproductAction();
    }


    @Bean ConfigProviderImpl supplierproductConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean ConfigBasedEnablementStrategy supplierproductConfigBasedEnablementStrategy(
        @Qualifier("supplierproductConfigProvider") ConfigProvider configProvider,
        @Qualifier("supplierproductFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider,PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }



    @Bean PENDING_DELIVERYSupplierproductPostSaveHook
        supplierproductPENDING_DELIVERYPostSaveHook(){
            return new PENDING_DELIVERYSupplierproductPostSaveHook();
    }

    @Bean ACTIVESupplierproductPostSaveHook
        supplierproductACTIVEPostSaveHook(){
            return new ACTIVESupplierproductPostSaveHook();
    }

    @Bean QUALITY_CHECKSupplierproductPostSaveHook
        supplierproductQUALITY_CHECKPostSaveHook(){
            return new QUALITY_CHECKSupplierproductPostSaveHook();
    }

    @Bean OUT_OF_STOCKSupplierproductPostSaveHook
        supplierproductOUT_OF_STOCKPostSaveHook(){
            return new OUT_OF_STOCKSupplierproductPostSaveHook();
    }

    @Bean AT_WAREHOUSESupplierproductPostSaveHook
        supplierproductAT_WAREHOUSEPostSaveHook(){
            return new AT_WAREHOUSESupplierproductPostSaveHook();
    }

    @Bean APPROVEDSupplierproductPostSaveHook
        supplierproductAPPROVEDPostSaveHook(){
            return new APPROVEDSupplierproductPostSaveHook();
    }

    @Bean DISCONTINUEDSupplierproductPostSaveHook
        supplierproductDISCONTINUEDPostSaveHook(){
            return new DISCONTINUEDSupplierproductPostSaveHook();
    }

}
