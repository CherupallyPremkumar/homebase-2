package com.homebase.ecom.order.configuration;

import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.action.scriptsupport.IfAction;
import org.chenile.stm.ognl.OgnlScriptingStrategy;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.chenile.workflow.service.impl.HmStateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.service.cmds.*;
import com.homebase.ecom.order.service.healthcheck.OrderHealthChecker;
import com.homebase.ecom.order.infrastructure.persistence.ChenileOrderEntityStore;
import com.homebase.ecom.order.infrastructure.persistence.adapter.OrderJpaRepository;
import com.homebase.ecom.order.infrastructure.persistence.mapper.OrderMapper;
import org.chenile.workflow.api.WorkflowRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import com.homebase.ecom.order.service.postSaveHooks.*;
import com.homebase.ecom.order.service.event.OrderEventHandler;
import com.homebase.ecom.order.service.validator.OrderPolicyValidator;
import com.homebase.ecom.order.service.impl.OrderServiceImpl;
import com.homebase.ecom.order.service.OrderService;

/**
 * Spring configuration for the Order module — wires STM, entity store, actions,
 * post-save hooks, event handler, and policy validator.
 */
@Configuration
public class OrderConfiguration {
    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/order/order-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Order";
    public static final String PREFIX_FOR_RESOLVER = "order";

    // ════════════════════════════════════════════════════════════════════════
    // STM Core Infrastructure
    // ════════════════════════════════════════════════════════════════════════

    @Bean
    BeanFactoryAdapter orderBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl orderFlowStore(
            @Qualifier("orderBeanFactoryAdapter") BeanFactoryAdapter orderBeanFactoryAdapter) {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(orderBeanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean
    STM<Order> orderEntityStm(@Qualifier("orderFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMImpl<Order> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider orderActionsInfoProvider(@Qualifier("orderFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("order", provider);
        return provider;
    }

    @Bean
    OrderMapper orderMapper() {
        return new OrderMapper();
    }

    @Bean
    EntityStore<Order> orderEntityStore(OrderJpaRepository jpaRepository, OrderMapper mapper) {
        return new ChenileOrderEntityStore(jpaRepository, mapper);
    }

    @Bean
    StateEntityServiceImpl<Order> _orderStateEntityService_(
            @Qualifier("orderEntityStm") STM<Order> stm,
            @Qualifier("orderActionsInfoProvider") STMActionsInfoProvider orderInfoProvider,
            @Qualifier("orderEntityStore") EntityStore<Order> entityStore) {
        return new HmStateEntityServiceImpl<>(stm, orderInfoProvider, entityStore);
    }

    // ════════════════════════════════════════════════════════════════════════
    // STM Wiring: PostSaveHook, EntryAction, ExitAction, FlowReader
    // ════════════════════════════════════════════════════════════════════════

    @Bean
    DefaultPostSaveHook<Order> orderDefaultPostSaveHook(
            @Qualifier("orderTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean
    GenericEntryAction<Order> orderEntryAction(@Qualifier("orderEntityStore") EntityStore<Order> entityStore,
            @Qualifier("orderActionsInfoProvider") STMActionsInfoProvider orderInfoProvider,
            @Qualifier("orderFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("orderDefaultPostSaveHook") DefaultPostSaveHook<Order> postSaveHook) {
        GenericEntryAction<Order> entryAction = new GenericEntryAction<>(entityStore, orderInfoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    DefaultAutomaticStateComputation<Order> orderDefaultAutoState(
            @Qualifier("orderTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("orderFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<Order> autoState = new DefaultAutomaticStateComputation<>(
                stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    GenericExitAction<Order> orderExitAction(@Qualifier("orderFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<Order> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    XmlFlowReader orderFlowReader(@Qualifier("orderFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean
    OrderHealthChecker orderHealthChecker() {
        return new OrderHealthChecker();
    }

    @Bean
    STMTransitionAction<Order> defaultorderSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver orderTransitionActionResolver(
            @Qualifier("defaultorderSTMTransitionAction") STMTransitionAction<Order> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean
    StmBodyTypeSelector orderBodyTypeSelector(
            @Qualifier("orderActionsInfoProvider") STMActionsInfoProvider orderInfoProvider,
            @Qualifier("orderTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(orderInfoProvider, stmTransitionActionResolver);
    }

    @Bean
    STMTransitionAction<Order> orderBaseTransitionAction(
            @Qualifier("orderTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("orderActivityChecker") ActivityChecker activityChecker,
            @Qualifier("orderFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<Order> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean
    ActivityChecker orderActivityChecker(@Qualifier("orderFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete activitiesCompletionCheck(
            @Qualifier("orderActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    @Bean
    ConfigProviderImpl orderConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    ConfigBasedEnablementStrategy orderConfigBasedEnablementStrategy(
            @Qualifier("orderConfigProvider") ConfigProvider configProvider,
            @Qualifier("orderFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider,
                PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // --- OGNL + Auto-state support ---

    @Bean
    OgnlScriptingStrategy ognlScriptingStrategy() {
        return new OgnlScriptingStrategy();
    }

    @Bean
    IfAction<Order> ifAction() {
        return new IfAction<>();
    }

    // ════════════════════════════════════════════════════════════════════════
    // STM Transition Actions — convention: "order" + eventId + "Action"
    // ════════════════════════════════════════════════════════════════════════

    @Bean PaymentSucceededOrderAction orderPaymentSucceededAction() { return new PaymentSucceededOrderAction(); }
    @Bean PaymentFailedOrderAction orderPaymentFailedAction() { return new PaymentFailedOrderAction(); }
    @Bean RequestCancellationOrderAction orderRequestCancellationAction() { return new RequestCancellationOrderAction(); }
    @Bean ConfirmCancellationOrderAction orderConfirmCancellationAction() { return new ConfirmCancellationOrderAction(); }
    @Bean StartProcessingOrderAction orderStartProcessingAction() { return new StartProcessingOrderAction(); }
    @Bean MarkShippedOrderAction orderMarkShippedAction() { return new MarkShippedOrderAction(); }
    @Bean MarkDeliveredOrderAction orderMarkDeliveredAction() { return new MarkDeliveredOrderAction(); }
    @Bean ConfirmDeliveryOrderAction orderConfirmDeliveryAction() { return new ConfirmDeliveryOrderAction(); }
    @Bean RequestRefundOrderAction orderRequestRefundAction() { return new RequestRefundOrderAction(); }
    @Bean CompleteRefundOrderAction orderCompleteRefundAction() { return new CompleteRefundOrderAction(); }
    @Bean HoldForFraudOrderAction orderHoldForFraudAction() { return new HoldForFraudOrderAction(); }
    @Bean ClearFraudOrderAction orderClearFraudAction() { return new ClearFraudOrderAction(); }
    @Bean ConfirmFraudOrderAction orderConfirmFraudAction() { return new ConfirmFraudOrderAction(); }
    @Bean PartialShipOrderAction orderPartialShipAction() { return new PartialShipOrderAction(); }
    @Bean DeliveryFailedOrderAction orderDeliveryFailedAction() { return new DeliveryFailedOrderAction(); }
    @Bean PlaceOnHoldOrderAction orderPlaceOnHoldAction() { return new PlaceOnHoldOrderAction(); }
    @Bean ReleaseHoldOrderAction orderReleaseHoldAction() { return new ReleaseHoldOrderAction(); }
    @Bean RequestReturnOrderAction orderRequestReturnAction() { return new RequestReturnOrderAction(); }

    // ════════════════════════════════════════════════════════════════════════
    // PostSaveHooks — convention: "order" + STATE_ID + "PostSaveHook"
    // ════════════════════════════════════════════════════════════════════════

    @Bean CREATEDOrderPostSaveHook orderCREATEDPostSaveHook() { return new CREATEDOrderPostSaveHook(); }
    @Bean PAIDOrderPostSaveHook orderPAIDPostSaveHook() { return new PAIDOrderPostSaveHook(); }
    @Bean CANCELLEDOrderPostSaveHook orderCANCELLEDPostSaveHook() { return new CANCELLEDOrderPostSaveHook(); }
    @Bean DELIVEREDOrderPostSaveHook orderDELIVEREDPostSaveHook() { return new DELIVEREDOrderPostSaveHook(); }
    @Bean SHIPPEDOrderPostSaveHook orderSHIPPEDPostSaveHook() { return new SHIPPEDOrderPostSaveHook(); }
    @Bean COMPLETEDOrderPostSaveHook orderCOMPLETEDPostSaveHook() { return new COMPLETEDOrderPostSaveHook(); }

    // ════════════════════════════════════════════════════════════════════════
    // Services, Events, Validators
    // ════════════════════════════════════════════════════════════════════════

    @Bean
    @ConditionalOnMissingBean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    OrderService orderService() {
        return new OrderServiceImpl();
    }

    @Bean
    OrderEventHandler orderEventService() {
        return new OrderEventHandler();
    }

    @Bean
    OrderPolicyValidator orderPolicyValidator() {
        return new OrderPolicyValidator();
    }

    @Bean
    java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> orderEventAuthoritiesSupplier(
            @Qualifier("orderActionsInfoProvider") STMActionsInfoProvider orderInfoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(orderInfoProvider, false);
        return builder.build();
    }
}
