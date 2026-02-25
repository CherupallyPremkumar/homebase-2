package com.homebase.ecom.returnrequest.configuration;

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
import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.service.cmds.*;
import com.homebase.ecom.returnrequest.service.healthcheck.ReturnrequestHealthChecker;
import com.homebase.ecom.returnrequest.service.store.ReturnrequestEntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.stm.State;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import com.homebase.ecom.returnrequest.service.postSaveHooks.*;

/**
 This is where you will instantiate all the required classes in Spring
*/
@Configuration
public class ReturnrequestConfiguration {
	private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/returnrequest/returnrequest-states.xml";
	public static final String PREFIX_FOR_PROPERTIES = "Returnrequest";
    public static final String PREFIX_FOR_RESOLVER = "returnrequest";

    @Bean BeanFactoryAdapter returnrequestBeanFactoryAdapter() {
		return new SpringBeanFactoryAdapter();
	}
	
	@Bean STMFlowStoreImpl returnrequestFlowStore(
            @Qualifier("returnrequestBeanFactoryAdapter") BeanFactoryAdapter returnrequestBeanFactoryAdapter
            )throws Exception{
		STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
		stmFlowStore.setBeanFactory(returnrequestBeanFactoryAdapter);
		return stmFlowStore;
	}
	
	@Bean  STM<Returnrequest> returnrequestEntityStm(@Qualifier("returnrequestFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception{
		STMImpl<Returnrequest> stm = new STMImpl<>();		
		stm.setStmFlowStore(stmFlowStore);
		return stm;
	}
	
	@Bean  STMActionsInfoProvider returnrequestActionsInfoProvider(@Qualifier("returnrequestFlowStore") STMFlowStoreImpl stmFlowStore) {
		STMActionsInfoProvider provider =  new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("returnrequest",provider);
        return provider;
	}
	
	@Bean EntityStore<Returnrequest> returnrequestEntityStore() {
		return new ReturnrequestEntityStore();
	}
	
	@Bean  StateEntityServiceImpl<Returnrequest> _returnrequestStateEntityService_(
			@Qualifier("returnrequestEntityStm") STM<Returnrequest> stm,
			@Qualifier("returnrequestActionsInfoProvider") STMActionsInfoProvider returnrequestInfoProvider,
			@Qualifier("returnrequestEntityStore") EntityStore<Returnrequest> entityStore){
		return new StateEntityServiceImpl<>(stm, returnrequestInfoProvider, entityStore);
	}
	
	// Now we start constructing the STM Components 


    @Bean  DefaultPostSaveHook<Returnrequest> returnrequestDefaultPostSaveHook(
    @Qualifier("returnrequestTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver){
    DefaultPostSaveHook<Returnrequest> postSaveHook = new DefaultPostSaveHook<>(stmTransitionActionResolver);
    return postSaveHook;
    }

    @Bean  GenericEntryAction<Returnrequest> returnrequestEntryAction(@Qualifier("returnrequestEntityStore") EntityStore<Returnrequest> entityStore,
    @Qualifier("returnrequestActionsInfoProvider") STMActionsInfoProvider returnrequestInfoProvider,
    @Qualifier("returnrequestFlowStore") STMFlowStoreImpl stmFlowStore,
    @Qualifier("returnrequestDefaultPostSaveHook") DefaultPostSaveHook<Returnrequest> postSaveHook)  {
    GenericEntryAction<Returnrequest> entryAction =  new GenericEntryAction<Returnrequest>(entityStore,returnrequestInfoProvider,postSaveHook);
    stmFlowStore.setEntryAction(entryAction);
    return entryAction;
    }

    @Bean  DefaultAutomaticStateComputation<Returnrequest> returnrequestDefaultAutoState(
    @Qualifier("returnrequestTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
    @Qualifier("returnrequestFlowStore") STMFlowStoreImpl stmFlowStore){
    DefaultAutomaticStateComputation<Returnrequest> autoState = new DefaultAutomaticStateComputation<>(stmTransitionActionResolver);
    stmFlowStore.setDefaultAutomaticStateComputation(autoState);
    return autoState;
    }

	@Bean GenericExitAction<Returnrequest> returnrequestExitAction(@Qualifier("returnrequestFlowStore") STMFlowStoreImpl stmFlowStore){
        GenericExitAction<Returnrequest> exitAction = new GenericExitAction<Returnrequest>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
	}

	@Bean
	XmlFlowReader returnrequestFlowReader(@Qualifier("returnrequestFlowStore") STMFlowStoreImpl flowStore) throws Exception {
		XmlFlowReader flowReader = new XmlFlowReader(flowStore);
		flowReader.setFilename(FLOW_DEFINITION_FILE);
		return flowReader;
	}
	

	@Bean ReturnrequestHealthChecker returnrequestHealthChecker(){
    	return new ReturnrequestHealthChecker();
    }

    @Bean STMTransitionAction<Returnrequest> defaultreturnrequestSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver returnrequestTransitionActionResolver(
    @Qualifier("defaultreturnrequestSTMTransitionAction") STMTransitionAction<Returnrequest> defaultSTMTransitionAction){
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER,defaultSTMTransitionAction,true);
    }

    @Bean  StmBodyTypeSelector returnrequestBodyTypeSelector(
    @Qualifier("returnrequestActionsInfoProvider") STMActionsInfoProvider returnrequestInfoProvider,
    @Qualifier("returnrequestTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(returnrequestInfoProvider,stmTransitionActionResolver);
    }


    @Bean  STMTransitionAction<Returnrequest> returnrequestBaseTransitionAction(
        @Qualifier("returnrequestTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
        @Qualifier("returnrequestActivityChecker") ActivityChecker activityChecker,
        @Qualifier("returnrequestFlowStore") STMFlowStoreImpl stmFlowStore){
        BaseTransitionAction<Returnrequest> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean ActivityChecker returnrequestActivityChecker(@Qualifier("returnrequestFlowStore") STMFlowStoreImpl stmFlowStore){
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete activitiesCompletionCheck(@Qualifier("returnrequestActivityChecker") ActivityChecker activityChecker){
        return new AreActivitiesComplete(activityChecker);
    }

    // Create the specific transition actions here. Make sure that these actions are inheriting from
    // AbstractSTMTransitionMachine (The sample classes provide an example of this). To automatically wire
    // them into the STM use the convention of "returnrequest" + eventId + "Action" for the method name. (returnrequest is the
    // prefix passed to the TransitionActionResolver above.)
    // This will ensure that these are detected automatically by the Workflow system.
    // The payload types will be detected as well so that there is no need to introduce an <event-information/>
    // segment in src/main/resources/com/homebase/returnrequest/returnrequest-states.xml


    @Bean ApproveReturnReturnrequestAction
            returnrequestApproveReturnAction(){
        return new ApproveReturnReturnrequestAction();
    }

    @Bean ItemReceivedReturnrequestAction
            returnrequestItemReceivedAction(){
        return new ItemReceivedReturnrequestAction();
    }

    @Bean RejectReturnReturnrequestAction
            returnrequestRejectReturnAction(){
        return new RejectReturnReturnrequestAction();
    }

    @Bean ProcessRefundReturnrequestAction
            returnrequestProcessRefundAction(){
        return new ProcessRefundReturnrequestAction();
    }

    @Bean PickupInitiatedReturnrequestAction
            returnrequestPickupInitiatedAction(){
        return new PickupInitiatedReturnrequestAction();
    }

    @Bean InspectReturnReturnrequestAction
            returnrequestInspectReturnAction(){
        return new InspectReturnReturnrequestAction();
    }


    @Bean ConfigProviderImpl returnrequestConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean ConfigBasedEnablementStrategy returnrequestConfigBasedEnablementStrategy(
        @Qualifier("returnrequestConfigProvider") ConfigProvider configProvider,
        @Qualifier("returnrequestFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider,PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }



    @Bean RECEIVEDReturnrequestPostSaveHook
        returnrequestRECEIVEDPostSaveHook(){
            return new RECEIVEDReturnrequestPostSaveHook();
    }

    @Bean REFUNDEDReturnrequestPostSaveHook
        returnrequestREFUNDEDPostSaveHook(){
            return new REFUNDEDReturnrequestPostSaveHook();
    }

    @Bean IN_TRANSIT_BACKReturnrequestPostSaveHook
        returnrequestIN_TRANSIT_BACKPostSaveHook(){
            return new IN_TRANSIT_BACKReturnrequestPostSaveHook();
    }

    @Bean REQUESTEDReturnrequestPostSaveHook
        returnrequestREQUESTEDPostSaveHook(){
            return new REQUESTEDReturnrequestPostSaveHook();
    }

    @Bean APPROVEDReturnrequestPostSaveHook
        returnrequestAPPROVEDPostSaveHook(){
            return new APPROVEDReturnrequestPostSaveHook();
    }

    @Bean UNDER_REVIEWReturnrequestPostSaveHook
        returnrequestUNDER_REVIEWPostSaveHook(){
            return new UNDER_REVIEWReturnrequestPostSaveHook();
    }

    @Bean REJECTEDReturnrequestPostSaveHook
        returnrequestREJECTEDPostSaveHook(){
            return new REJECTEDReturnrequestPostSaveHook();
    }

}
