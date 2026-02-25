package com.homebase.ecom.order.configuration;

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
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.service.cmds.*;
import com.homebase.ecom.order.service.healthcheck.OrderHealthChecker;
import com.homebase.ecom.order.service.store.OrderEntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.stm.State;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import com.homebase.ecom.order.service.postSaveHooks.*;

/**
 This is where you will instantiate all the required classes in Spring
*/
@Configuration
public class OrderConfiguration {
	private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/order/order-states.xml";
	public static final String PREFIX_FOR_PROPERTIES = "Order";
    public static final String PREFIX_FOR_RESOLVER = "order";

    @Bean BeanFactoryAdapter orderBeanFactoryAdapter() {
		return new SpringBeanFactoryAdapter();
	}
	
	@Bean STMFlowStoreImpl orderFlowStore(
            @Qualifier("orderBeanFactoryAdapter") BeanFactoryAdapter orderBeanFactoryAdapter
            )throws Exception{
		STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
		stmFlowStore.setBeanFactory(orderBeanFactoryAdapter);
		return stmFlowStore;
	}
	
	@Bean  STM<Order> orderEntityStm(@Qualifier("orderFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception{
		STMImpl<Order> stm = new STMImpl<>();		
		stm.setStmFlowStore(stmFlowStore);
		return stm;
	}
	
	@Bean  STMActionsInfoProvider orderActionsInfoProvider(@Qualifier("orderFlowStore") STMFlowStoreImpl stmFlowStore) {
		STMActionsInfoProvider provider =  new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("order",provider);
        return provider;
	}
	
	@Bean EntityStore<Order> orderEntityStore() {
		return new OrderEntityStore();
	}
	
	@Bean  StateEntityServiceImpl<Order> _orderStateEntityService_(
			@Qualifier("orderEntityStm") STM<Order> stm,
			@Qualifier("orderActionsInfoProvider") STMActionsInfoProvider orderInfoProvider,
			@Qualifier("orderEntityStore") EntityStore<Order> entityStore){
		return new StateEntityServiceImpl<>(stm, orderInfoProvider, entityStore);
	}
	
	// Now we start constructing the STM Components 


    @Bean  DefaultPostSaveHook<Order> orderDefaultPostSaveHook(
    @Qualifier("orderTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver){
    DefaultPostSaveHook<Order> postSaveHook = new DefaultPostSaveHook<>(stmTransitionActionResolver);
    return postSaveHook;
    }

    @Bean  GenericEntryAction<Order> orderEntryAction(@Qualifier("orderEntityStore") EntityStore<Order> entityStore,
    @Qualifier("orderActionsInfoProvider") STMActionsInfoProvider orderInfoProvider,
    @Qualifier("orderFlowStore") STMFlowStoreImpl stmFlowStore,
    @Qualifier("orderDefaultPostSaveHook") DefaultPostSaveHook<Order> postSaveHook)  {
    GenericEntryAction<Order> entryAction =  new GenericEntryAction<Order>(entityStore,orderInfoProvider,postSaveHook);
    stmFlowStore.setEntryAction(entryAction);
    return entryAction;
    }

    @Bean  DefaultAutomaticStateComputation<Order> orderDefaultAutoState(
    @Qualifier("orderTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
    @Qualifier("orderFlowStore") STMFlowStoreImpl stmFlowStore){
    DefaultAutomaticStateComputation<Order> autoState = new DefaultAutomaticStateComputation<>(stmTransitionActionResolver);
    stmFlowStore.setDefaultAutomaticStateComputation(autoState);
    return autoState;
    }

	@Bean GenericExitAction<Order> orderExitAction(@Qualifier("orderFlowStore") STMFlowStoreImpl stmFlowStore){
        GenericExitAction<Order> exitAction = new GenericExitAction<Order>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
	}

	@Bean
	XmlFlowReader orderFlowReader(@Qualifier("orderFlowStore") STMFlowStoreImpl flowStore) throws Exception {
		XmlFlowReader flowReader = new XmlFlowReader(flowStore);
		flowReader.setFilename(FLOW_DEFINITION_FILE);
		return flowReader;
	}
	

	@Bean OrderHealthChecker orderHealthChecker(){
    	return new OrderHealthChecker();
    }

    @Bean STMTransitionAction<Order> defaultorderSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver orderTransitionActionResolver(
    @Qualifier("defaultorderSTMTransitionAction") STMTransitionAction<Order> defaultSTMTransitionAction){
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER,defaultSTMTransitionAction,true);
    }

    @Bean  StmBodyTypeSelector orderBodyTypeSelector(
    @Qualifier("orderActionsInfoProvider") STMActionsInfoProvider orderInfoProvider,
    @Qualifier("orderTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(orderInfoProvider,stmTransitionActionResolver);
    }


    @Bean  STMTransitionAction<Order> orderBaseTransitionAction(
        @Qualifier("orderTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
        @Qualifier("orderActivityChecker") ActivityChecker activityChecker,
        @Qualifier("orderFlowStore") STMFlowStoreImpl stmFlowStore){
        BaseTransitionAction<Order> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean ActivityChecker orderActivityChecker(@Qualifier("orderFlowStore") STMFlowStoreImpl stmFlowStore){
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete activitiesCompletionCheck(@Qualifier("orderActivityChecker") ActivityChecker activityChecker){
        return new AreActivitiesComplete(activityChecker);
    }

    // Create the specific transition actions here. Make sure that these actions are inheriting from
    // AbstractSTMTransitionMachine (The sample classes provide an example of this). To automatically wire
    // them into the STM use the convention of "order" + eventId + "Action" for the method name. (order is the
    // prefix passed to the TransitionActionResolver above.)
    // This will ensure that these are detected automatically by the Workflow system.
    // The payload types will be detected as well so that there is no need to introduce an <event-information/>
    // segment in src/main/resources/com/homebase/order/order-states.xml


    @Bean ApproveReturnOrderAction
            orderApproveReturnAction(){
        return new ApproveReturnOrderAction();
    }

    @Bean CancelOrderOrderAction
            orderCancelOrderAction(){
        return new CancelOrderOrderAction();
    }

    @Bean ReturnRequestedOrderAction
            orderReturnRequestedAction(){
        return new ReturnRequestedOrderAction();
    }

    @Bean RejectReturnOrderAction
            orderRejectReturnAction(){
        return new RejectReturnOrderAction();
    }

    @Bean ConfirmDeliveryOrderAction
            orderConfirmDeliveryAction(){
        return new ConfirmDeliveryOrderAction();
    }

    @Bean InitiateReturnOrderAction
            orderInitiateReturnAction(){
        return new InitiateReturnOrderAction();
    }

    @Bean DeliverOrderOrderAction
            orderDeliverOrderAction(){
        return new DeliverOrderOrderAction();
    }

    @Bean ProcessPaymentOrderAction
            orderProcessPaymentAction(){
        return new ProcessPaymentOrderAction();
    }

    @Bean StartProcessingOrderAction
            orderStartProcessingAction(){
        return new StartProcessingOrderAction();
    }

    @Bean ItemsPickedOrderAction
            orderItemsPickedAction(){
        return new ItemsPickedOrderAction();
    }

    @Bean RefundCompleteOrderAction
            orderRefundCompleteAction(){
        return new RefundCompleteOrderAction();
    }

    @Bean CourierPickupOrderAction
            orderCourierPickupAction(){
        return new CourierPickupOrderAction();
    }


    @Bean ConfigProviderImpl orderConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean ConfigBasedEnablementStrategy orderConfigBasedEnablementStrategy(
        @Qualifier("orderConfigProvider") ConfigProvider configProvider,
        @Qualifier("orderFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider,PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }



    @Bean REFUND_INITIATEDOrderPostSaveHook
        orderREFUND_INITIATEDPostSaveHook(){
            return new REFUND_INITIATEDOrderPostSaveHook();
    }

    @Bean CANCELLEDOrderPostSaveHook
        orderCANCELLEDPostSaveHook(){
            return new CANCELLEDOrderPostSaveHook();
    }

    @Bean CREATEDOrderPostSaveHook
        orderCREATEDPostSaveHook(){
            return new CREATEDOrderPostSaveHook();
    }

    @Bean DELIVEREDOrderPostSaveHook
        orderDELIVEREDPostSaveHook(){
            return new DELIVEREDOrderPostSaveHook();
    }

    @Bean COMPLETEDOrderPostSaveHook
        orderCOMPLETEDPostSaveHook(){
            return new COMPLETEDOrderPostSaveHook();
    }

    @Bean REFUNDEDOrderPostSaveHook
        orderREFUNDEDPostSaveHook(){
            return new REFUNDEDOrderPostSaveHook();
    }

    @Bean PAYMENT_CONFIRMEDOrderPostSaveHook
        orderPAYMENT_CONFIRMEDPostSaveHook(){
            return new PAYMENT_CONFIRMEDOrderPostSaveHook();
    }

    @Bean PROCESSINGOrderPostSaveHook
        orderPROCESSINGPostSaveHook(){
            return new PROCESSINGOrderPostSaveHook();
    }

    @Bean PICKEDOrderPostSaveHook
        orderPICKEDPostSaveHook(){
            return new PICKEDOrderPostSaveHook();
    }

    @Bean RETURN_INITIATEDOrderPostSaveHook
        orderRETURN_INITIATEDPostSaveHook(){
            return new RETURN_INITIATEDOrderPostSaveHook();
    }

    @Bean SHIPPEDOrderPostSaveHook
        orderSHIPPEDPostSaveHook(){
            return new SHIPPEDOrderPostSaveHook();
    }

}
