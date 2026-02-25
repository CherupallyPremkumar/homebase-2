package com.homebase.ecom.shipping.configuration;

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
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.service.cmds.*;
import com.homebase.ecom.shipping.service.healthcheck.ShippingHealthChecker;
import com.homebase.ecom.shipping.service.store.ShippingEntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.stm.State;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import com.homebase.ecom.shipping.service.postSaveHooks.*;

/**
 This is where you will instantiate all the required classes in Spring
*/
@Configuration
public class ShippingConfiguration {
	private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/shipping/shipping-states.xml";
	public static final String PREFIX_FOR_PROPERTIES = "Shipping";
    public static final String PREFIX_FOR_RESOLVER = "shipping";

    @Bean BeanFactoryAdapter shippingBeanFactoryAdapter() {
		return new SpringBeanFactoryAdapter();
	}
	
	@Bean STMFlowStoreImpl shippingFlowStore(
            @Qualifier("shippingBeanFactoryAdapter") BeanFactoryAdapter shippingBeanFactoryAdapter
            )throws Exception{
		STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
		stmFlowStore.setBeanFactory(shippingBeanFactoryAdapter);
		return stmFlowStore;
	}
	
	@Bean  STM<Shipping> shippingEntityStm(@Qualifier("shippingFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception{
		STMImpl<Shipping> stm = new STMImpl<>();		
		stm.setStmFlowStore(stmFlowStore);
		return stm;
	}
	
	@Bean  STMActionsInfoProvider shippingActionsInfoProvider(@Qualifier("shippingFlowStore") STMFlowStoreImpl stmFlowStore) {
		STMActionsInfoProvider provider =  new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("shipping",provider);
        return provider;
	}
	
	@Bean EntityStore<Shipping> shippingEntityStore() {
		return new ShippingEntityStore();
	}
	
	@Bean  StateEntityServiceImpl<Shipping> _shippingStateEntityService_(
			@Qualifier("shippingEntityStm") STM<Shipping> stm,
			@Qualifier("shippingActionsInfoProvider") STMActionsInfoProvider shippingInfoProvider,
			@Qualifier("shippingEntityStore") EntityStore<Shipping> entityStore){
		return new StateEntityServiceImpl<>(stm, shippingInfoProvider, entityStore);
	}
	
	// Now we start constructing the STM Components 


    @Bean  DefaultPostSaveHook<Shipping> shippingDefaultPostSaveHook(
    @Qualifier("shippingTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver){
    DefaultPostSaveHook<Shipping> postSaveHook = new DefaultPostSaveHook<>(stmTransitionActionResolver);
    return postSaveHook;
    }

    @Bean  GenericEntryAction<Shipping> shippingEntryAction(@Qualifier("shippingEntityStore") EntityStore<Shipping> entityStore,
    @Qualifier("shippingActionsInfoProvider") STMActionsInfoProvider shippingInfoProvider,
    @Qualifier("shippingFlowStore") STMFlowStoreImpl stmFlowStore,
    @Qualifier("shippingDefaultPostSaveHook") DefaultPostSaveHook<Shipping> postSaveHook)  {
    GenericEntryAction<Shipping> entryAction =  new GenericEntryAction<Shipping>(entityStore,shippingInfoProvider,postSaveHook);
    stmFlowStore.setEntryAction(entryAction);
    return entryAction;
    }

    @Bean  DefaultAutomaticStateComputation<Shipping> shippingDefaultAutoState(
    @Qualifier("shippingTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
    @Qualifier("shippingFlowStore") STMFlowStoreImpl stmFlowStore){
    DefaultAutomaticStateComputation<Shipping> autoState = new DefaultAutomaticStateComputation<>(stmTransitionActionResolver);
    stmFlowStore.setDefaultAutomaticStateComputation(autoState);
    return autoState;
    }

	@Bean GenericExitAction<Shipping> shippingExitAction(@Qualifier("shippingFlowStore") STMFlowStoreImpl stmFlowStore){
        GenericExitAction<Shipping> exitAction = new GenericExitAction<Shipping>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
	}

	@Bean
	XmlFlowReader shippingFlowReader(@Qualifier("shippingFlowStore") STMFlowStoreImpl flowStore) throws Exception {
		XmlFlowReader flowReader = new XmlFlowReader(flowStore);
		flowReader.setFilename(FLOW_DEFINITION_FILE);
		return flowReader;
	}
	

	@Bean ShippingHealthChecker shippingHealthChecker(){
    	return new ShippingHealthChecker();
    }

    @Bean STMTransitionAction<Shipping> defaultshippingSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver shippingTransitionActionResolver(
    @Qualifier("defaultshippingSTMTransitionAction") STMTransitionAction<Shipping> defaultSTMTransitionAction){
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER,defaultSTMTransitionAction,true);
    }

    @Bean  StmBodyTypeSelector shippingBodyTypeSelector(
    @Qualifier("shippingActionsInfoProvider") STMActionsInfoProvider shippingInfoProvider,
    @Qualifier("shippingTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(shippingInfoProvider,stmTransitionActionResolver);
    }


    @Bean  STMTransitionAction<Shipping> shippingBaseTransitionAction(
        @Qualifier("shippingTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
        @Qualifier("shippingActivityChecker") ActivityChecker activityChecker,
        @Qualifier("shippingFlowStore") STMFlowStoreImpl stmFlowStore){
        BaseTransitionAction<Shipping> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean ActivityChecker shippingActivityChecker(@Qualifier("shippingFlowStore") STMFlowStoreImpl stmFlowStore){
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete activitiesCompletionCheck(@Qualifier("shippingActivityChecker") ActivityChecker activityChecker){
        return new AreActivitiesComplete(activityChecker);
    }

    // Create the specific transition actions here. Make sure that these actions are inheriting from
    // AbstractSTMTransitionMachine (The sample classes provide an example of this). To automatically wire
    // them into the STM use the convention of "shipping" + eventId + "Action" for the method name. (shipping is the
    // prefix passed to the TransitionActionResolver above.)
    // This will ensure that these are detected automatically by the Workflow system.
    // The payload types will be detected as well so that there is no need to introduce an <event-information/>
    // segment in src/main/resources/com/homebase/shipping/shipping-states.xml


    @Bean ReturnRequestedShippingAction
            shippingReturnRequestedAction(){
        return new ReturnRequestedShippingAction();
    }

    @Bean MarkDeliveredShippingAction
            shippingMarkDeliveredAction(){
        return new MarkDeliveredShippingAction();
    }

    @Bean OutForDeliveryShippingAction
            shippingOutForDeliveryAction(){
        return new OutForDeliveryShippingAction();
    }

    @Bean CourierAssignedShippingAction
            shippingCourierAssignedAction(){
        return new CourierAssignedShippingAction();
    }

    @Bean ReturnPickupShippingAction
            shippingReturnPickupAction(){
        return new ReturnPickupShippingAction();
    }

    @Bean InTransitShippingAction
            shippingInTransitAction(){
        return new InTransitShippingAction();
    }


    @Bean ConfigProviderImpl shippingConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean ConfigBasedEnablementStrategy shippingConfigBasedEnablementStrategy(
        @Qualifier("shippingConfigProvider") ConfigProvider configProvider,
        @Qualifier("shippingFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider,PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }



    @Bean RETURN_REQUESTEDShippingPostSaveHook
        shippingRETURN_REQUESTEDPostSaveHook(){
            return new RETURN_REQUESTEDShippingPostSaveHook();
    }

    @Bean DELIVEREDShippingPostSaveHook
        shippingDELIVEREDPostSaveHook(){
            return new DELIVEREDShippingPostSaveHook();
    }

    @Bean IN_TRANSITShippingPostSaveHook
        shippingIN_TRANSITPostSaveHook(){
            return new IN_TRANSITShippingPostSaveHook();
    }

    @Bean RETURNEDShippingPostSaveHook
        shippingRETURNEDPostSaveHook(){
            return new RETURNEDShippingPostSaveHook();
    }

    @Bean PICKED_UPShippingPostSaveHook
        shippingPICKED_UPPostSaveHook(){
            return new PICKED_UPShippingPostSaveHook();
    }

    @Bean OUT_FOR_DELIVERYShippingPostSaveHook
        shippingOUT_FOR_DELIVERYPostSaveHook(){
            return new OUT_FOR_DELIVERYShippingPostSaveHook();
    }

    @Bean AWAITING_PICKUPShippingPostSaveHook
        shippingAWAITING_PICKUPPostSaveHook(){
            return new AWAITING_PICKUPShippingPostSaveHook();
    }

}
