package com.homebase.ecom.shipping.configuration;

import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.action.scriptsupport.IfAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.ognl.OgnlScriptingStrategy;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.chenile.workflow.service.impl.HmStateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.service.cmds.*;
import com.homebase.ecom.shipping.service.healthcheck.ShippingHealthChecker;
import com.homebase.ecom.shipping.infrastructure.persistence.ChenileShippingEntityStore;
import com.homebase.ecom.shipping.infrastructure.persistence.adapter.ShippingJpaRepository;
import com.homebase.ecom.shipping.infrastructure.persistence.mapper.ShippingMapper;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import com.homebase.ecom.shipping.service.postSaveHooks.*;
import com.homebase.ecom.shipping.service.event.ShippingEventPublisher;
import com.homebase.ecom.shipping.service.event.ShippingEventHandler;
import com.homebase.ecom.shipping.service.validator.ShippingPolicyValidator;

/**
 * Spring configuration for the Shipping bounded context.
 * Wires STM, entity store, transition actions, post-save hooks,
 * OGNL auto-states, policy validator, and chenile-kafka event handler.
 */
@Configuration
public class ShippingConfiguration {
    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/shipping/shipping-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Shipping";
    public static final String PREFIX_FOR_RESOLVER = "shipping";

    // ── STM Core ──

    @Bean
    BeanFactoryAdapter shippingBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl shippingFlowStore(
            @Qualifier("shippingBeanFactoryAdapter") BeanFactoryAdapter shippingBeanFactoryAdapter) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(shippingBeanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean
    STM<Shipping> shippingEntityStm(@Qualifier("shippingFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<Shipping> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider shippingActionsInfoProvider(@Qualifier("shippingFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("shipping", provider);
        return provider;
    }

    @Bean
    ShippingMapper shippingMapper() {
        return new ShippingMapper();
    }

    @Bean
    EntityStore<Shipping> shippingEntityStore(ShippingJpaRepository jpaRepository, ShippingMapper mapper) {
        return new ChenileShippingEntityStore(jpaRepository, mapper);
    }

    @Bean
    StateEntityServiceImpl<Shipping> _shippingStateEntityService_(
            @Qualifier("shippingEntityStm") STM<Shipping> stm,
            @Qualifier("shippingActionsInfoProvider") STMActionsInfoProvider shippingInfoProvider,
            @Qualifier("shippingEntityStore") EntityStore<Shipping> entityStore) {
        return new HmStateEntityServiceImpl<>(stm, shippingInfoProvider, entityStore);
    }

    // ── STM Infrastructure ──

    @Bean
    DefaultPostSaveHook<Shipping> shippingDefaultPostSaveHook(
            @Qualifier("shippingTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean
    GenericEntryAction<Shipping> shippingEntryAction(
            @Qualifier("shippingEntityStore") EntityStore<Shipping> entityStore,
            @Qualifier("shippingActionsInfoProvider") STMActionsInfoProvider shippingInfoProvider,
            @Qualifier("shippingFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("shippingDefaultPostSaveHook") DefaultPostSaveHook<Shipping> postSaveHook) {
        GenericEntryAction<Shipping> entryAction = new GenericEntryAction<>(entityStore, shippingInfoProvider,
                postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    DefaultAutomaticStateComputation<Shipping> shippingDefaultAutoState(
            @Qualifier("shippingTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("shippingFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<Shipping> autoState = new DefaultAutomaticStateComputation<>(
                stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    GenericExitAction<Shipping> shippingExitAction(@Qualifier("shippingFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<Shipping> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    XmlFlowReader shippingFlowReader(@Qualifier("shippingFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean
    ShippingHealthChecker shippingHealthChecker() {
        return new ShippingHealthChecker();
    }

    // ── OGNL support for auto-states ──

    @Bean
    OgnlScriptingStrategy ognlScriptingStrategy() {
        return new OgnlScriptingStrategy();
    }

    @Bean
    IfAction<Shipping> ifAction() {
        return new IfAction<>();
    }

    // ── STM Transition resolution ──

    @Bean
    STMTransitionAction<Shipping> defaultshippingSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver shippingTransitionActionResolver(
            @Qualifier("defaultshippingSTMTransitionAction") STMTransitionAction<Shipping> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean
    StmBodyTypeSelector shippingBodyTypeSelector(
            @Qualifier("shippingActionsInfoProvider") STMActionsInfoProvider shippingInfoProvider,
            @Qualifier("shippingTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(shippingInfoProvider, stmTransitionActionResolver);
    }

    @Bean
    STMTransitionAction<Shipping> shippingBaseTransitionAction(
            @Qualifier("shippingTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("shippingActivityChecker") ActivityChecker activityChecker,
            @Qualifier("shippingFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<Shipping> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean
    ActivityChecker shippingActivityChecker(@Qualifier("shippingFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete activitiesCompletionCheck(
            @Qualifier("shippingActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    @Bean
    ConfigProviderImpl shippingConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    ConfigBasedEnablementStrategy shippingConfigBasedEnablementStrategy(
            @Qualifier("shippingConfigProvider") ConfigProvider configProvider,
            @Qualifier("shippingFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider,
                PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // ── Transition Actions (convention: "shipping" + eventId + "Action") ──

    @Bean
    CreateLabelShippingAction shippingCreateLabelAction() {
        return new CreateLabelShippingAction();
    }

    @Bean
    PickUpShippingAction shippingPickUpAction() {
        return new PickUpShippingAction();
    }

    @Bean
    UpdateTransitShippingAction shippingUpdateTransitAction() {
        return new UpdateTransitShippingAction();
    }

    @Bean
    OutForDeliveryShippingAction shippingOutForDeliveryAction() {
        return new OutForDeliveryShippingAction();
    }

    @Bean
    DeliverShippingAction shippingDeliverAction() {
        return new DeliverShippingAction();
    }

    @Bean
    FailDeliveryShippingAction shippingFailDeliveryAction() {
        return new FailDeliveryShippingAction();
    }

    @Bean
    RetryDeliveryShippingAction shippingRetryDeliveryAction() {
        return new RetryDeliveryShippingAction();
    }

    @Bean
    CancelShipmentShippingAction shippingCancelShipmentAction() {
        return new CancelShipmentShippingAction();
    }

    @Bean
    ReturnShipmentShippingAction shippingReturnShipmentAction() {
        return new ReturnShipmentShippingAction();
    }

    // ── PostSaveHooks (convention: "shipping" + STATE + "PostSaveHook") ──

    @Bean
    PENDINGShippingPostSaveHook shippingPENDINGPostSaveHook() {
        return new PENDINGShippingPostSaveHook();
    }

    @Bean
    LABEL_CREATEDShippingPostSaveHook shippingLABEL_CREATEDPostSaveHook() {
        return new LABEL_CREATEDShippingPostSaveHook();
    }

    @Bean
    PICKED_UPShippingPostSaveHook shippingPICKED_UPPostSaveHook() {
        return new PICKED_UPShippingPostSaveHook();
    }

    @Bean
    IN_TRANSITShippingPostSaveHook shippingIN_TRANSITPostSaveHook() {
        return new IN_TRANSITShippingPostSaveHook();
    }

    @Bean
    OUT_FOR_DELIVERYShippingPostSaveHook shippingOUT_FOR_DELIVERYPostSaveHook() {
        return new OUT_FOR_DELIVERYShippingPostSaveHook();
    }

    @Bean
    DELIVEREDShippingPostSaveHook shippingDELIVEREDPostSaveHook() {
        return new DELIVEREDShippingPostSaveHook();
    }

    @Bean
    DELIVERY_FAILEDShippingPostSaveHook shippingDELIVERY_FAILEDPostSaveHook() {
        return new DELIVERY_FAILEDShippingPostSaveHook();
    }

    @Bean
    RETURNEDShippingPostSaveHook shippingRETURNEDPostSaveHook() {
        return new RETURNEDShippingPostSaveHook();
    }

    // ── Event Publisher ──

    @Bean
    ShippingEventPublisher shippingEventPublisher() {
        return new ShippingEventPublisher();
    }

    // ── Chenile-Kafka Event Handler ──

    @Bean("shippingEventService")
    @org.springframework.boot.autoconfigure.condition.ConditionalOnBean(org.chenile.pubsub.ChenilePub.class)
    ShippingEventHandler shippingEventService(
            @Qualifier("_shippingStateEntityService_") StateEntityServiceImpl<Shipping> shippingStateEntityService,
            org.chenile.pubsub.ChenilePub chenilePub,
            tools.jackson.databind.ObjectMapper objectMapper) {
        return new ShippingEventHandler(shippingStateEntityService, chenilePub, objectMapper);
    }

    // ── Policy Validator ──

    @Bean
    ShippingPolicyValidator shippingPolicyValidator() {
        return new ShippingPolicyValidator();
    }

    // ── Security ACL ──

    @Bean
    java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> shippingEventAuthoritiesSupplier(
            @Qualifier("shippingActionsInfoProvider") STMActionsInfoProvider shippingInfoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(shippingInfoProvider, false);
        return builder.build();
    }
}
