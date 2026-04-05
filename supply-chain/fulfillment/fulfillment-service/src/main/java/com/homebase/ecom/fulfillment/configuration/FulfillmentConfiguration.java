package com.homebase.ecom.fulfillment.configuration;

import org.chenile.core.context.ChenileExchange;
import org.chenile.owiz.OrchExecutor;
import org.chenile.owiz.config.impl.XmlOrchConfigurator;
import org.chenile.owiz.impl.OrchExecutorImpl;
import org.chenile.owiz.impl.OwizSpringFactoryAdapter;
import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.ognl.OgnlScriptingStrategy;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import com.homebase.ecom.core.workflow.HmStateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;
import com.homebase.ecom.fulfillment.port.FulfillmentEventPublisherPort;
import com.homebase.ecom.fulfillment.service.cmds.*;
import com.homebase.ecom.fulfillment.service.owiz.*;
import com.homebase.ecom.fulfillment.service.postSaveHooks.*;
import com.homebase.ecom.fulfillment.infrastructure.integration.KafkaFulfillmentEventPublisher;
import com.homebase.ecom.fulfillment.infrastructure.persistence.ChenileFulfillmentSagaEntityStore;
import com.homebase.ecom.fulfillment.infrastructure.persistence.adapter.FulfillmentSagaJpaRepository;
import com.homebase.ecom.fulfillment.infrastructure.persistence.adapter.FulfillmentSagaRepositoryImpl;
import com.homebase.ecom.fulfillment.infrastructure.persistence.mapper.FulfillmentSagaMapper;
import com.homebase.ecom.fulfillment.port.FulfillmentSagaRepository;
import com.homebase.ecom.fulfillment.service.consumer.OrderPaidEventConsumer;
import org.chenile.pubsub.ChenilePub;
import tools.jackson.databind.ObjectMapper;

/**
 * Full Chenile STM configuration for the Fulfillment saga.
 * Replaces the old pure OWIZ chain configuration.
 */
@Configuration
public class FulfillmentConfiguration {
    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/fulfillment/fulfillment-states.xml";
    private static final String OWIZ_FLOW_DEFINITION_FILE = "com/homebase/ecom/fulfillment/fulfillment-owiz-chains.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Fulfillment";
    public static final String PREFIX_FOR_RESOLVER = "fulfillment";

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean(name = "ognlScriptingStrategy")
    OgnlScriptingStrategy ognlScriptingStrategy() {
        return new OgnlScriptingStrategy();
    }

    @Bean
    BeanFactoryAdapter fulfillmentBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl fulfillmentFlowStore(
            @Qualifier("fulfillmentBeanFactoryAdapter") BeanFactoryAdapter fulfillmentBeanFactoryAdapter) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(fulfillmentBeanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean
    STM<FulfillmentSaga> fulfillmentEntityStm(
            @Qualifier("fulfillmentFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<FulfillmentSaga> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider fulfillmentActionsInfoProvider(
            @Qualifier("fulfillmentFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("fulfillment", provider);
        return provider;
    }

    @Bean
    FulfillmentSagaMapper fulfillmentSagaMapper() {
        return new FulfillmentSagaMapper();
    }

    @Bean
    EntityStore<FulfillmentSaga> fulfillmentEntityStore(
            FulfillmentSagaJpaRepository jpaRepository,
            FulfillmentSagaMapper mapper) {
        return new ChenileFulfillmentSagaEntityStore(jpaRepository, mapper);
    }

    @Bean
    StateEntityServiceImpl<FulfillmentSaga> _fulfillmentStateEntityService_(
            @Qualifier("fulfillmentEntityStm") STM<FulfillmentSaga> stm,
            @Qualifier("fulfillmentActionsInfoProvider") STMActionsInfoProvider fulfillmentInfoProvider,
            @Qualifier("fulfillmentEntityStore") EntityStore<FulfillmentSaga> entityStore) {
        return new HmStateEntityServiceImpl<>(stm, fulfillmentInfoProvider, entityStore);
    }

    // --- STM Components ---

    @Bean
    DefaultPostSaveHook<FulfillmentSaga> fulfillmentDefaultPostSaveHook(
            @Qualifier("fulfillmentTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean
    GenericEntryAction<FulfillmentSaga> fulfillmentEntryAction(
            @Qualifier("fulfillmentEntityStore") EntityStore<FulfillmentSaga> entityStore,
            @Qualifier("fulfillmentActionsInfoProvider") STMActionsInfoProvider fulfillmentInfoProvider,
            @Qualifier("fulfillmentFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("fulfillmentDefaultPostSaveHook") DefaultPostSaveHook<FulfillmentSaga> postSaveHook) {
        GenericEntryAction<FulfillmentSaga> entryAction = new GenericEntryAction<>(entityStore, fulfillmentInfoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    DefaultAutomaticStateComputation<FulfillmentSaga> fulfillmentDefaultAutoState(
            @Qualifier("fulfillmentTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("fulfillmentFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<FulfillmentSaga> autoState =
                new DefaultAutomaticStateComputation<>(stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    GenericExitAction<FulfillmentSaga> fulfillmentExitAction(
            @Qualifier("fulfillmentFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<FulfillmentSaga> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    XmlFlowReader fulfillmentFlowReader(
            @Qualifier("fulfillmentFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean
    STMTransitionAction<FulfillmentSaga> defaultfulfillmentSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver fulfillmentTransitionActionResolver(
            @Qualifier("defaultfulfillmentSTMTransitionAction") STMTransitionAction<FulfillmentSaga> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean
    StmBodyTypeSelector fulfillmentBodyTypeSelector(
            @Qualifier("fulfillmentActionsInfoProvider") STMActionsInfoProvider fulfillmentInfoProvider,
            @Qualifier("fulfillmentTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(fulfillmentInfoProvider, stmTransitionActionResolver);
    }

    @Bean
    STMTransitionAction<FulfillmentSaga> fulfillmentBaseTransitionAction(
            @Qualifier("fulfillmentTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("fulfillmentActivityChecker") ActivityChecker activityChecker,
            @Qualifier("fulfillmentFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<FulfillmentSaga> baseTransitionAction =
                new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean
    ActivityChecker fulfillmentActivityChecker(
            @Qualifier("fulfillmentFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete fulfillmentActivitiesCompletionCheck(
            @Qualifier("fulfillmentActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    @Bean
    ConfigProviderImpl fulfillmentConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    ConfigBasedEnablementStrategy fulfillmentConfigBasedEnablementStrategy(
            @Qualifier("fulfillmentConfigProvider") ConfigProvider configProvider,
            @Qualifier("fulfillmentFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy =
                new ConfigBasedEnablementStrategy(configProvider, PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // --- Transition Actions ---

    @Bean
    ReserveInventoryAction fulfillmentReserveInventoryAction() {
        return new ReserveInventoryAction();
    }

    @Bean
    CreateShipmentAction fulfillmentCreateShipmentAction() {
        return new CreateShipmentAction();
    }

    @Bean
    NotifyCustomerAction fulfillmentNotifyCustomerAction() {
        return new NotifyCustomerAction();
    }

    @Bean
    CompleteAction fulfillmentCompleteAction() {
        return new CompleteAction();
    }

    @Bean
    ShipAction fulfillmentShipAction() {
        return new ShipAction();
    }

    @Bean
    ConfirmDeliveryAction fulfillmentConfirmDeliveryAction() {
        return new ConfirmDeliveryAction();
    }

    @Bean
    RetryAction fulfillmentRetryAction() {
        return new RetryAction();
    }

    // --- Post-Save Hooks ---

    @Bean
    INITIATEDFulfillmentPostSaveHook fulfillmentINITIATEDPostSaveHook() {
        return new INITIATEDFulfillmentPostSaveHook();
    }

    @Bean
    INVENTORY_RESERVEDFulfillmentPostSaveHook fulfillmentINVENTORY_RESERVEDPostSaveHook(
            FulfillmentEventPublisherPort eventPublisher) {
        return new INVENTORY_RESERVEDFulfillmentPostSaveHook(eventPublisher);
    }

    @Bean
    SHIPMENT_CREATEDFulfillmentPostSaveHook fulfillmentSHIPMENT_CREATEDPostSaveHook(
            FulfillmentEventPublisherPort eventPublisher) {
        return new SHIPMENT_CREATEDFulfillmentPostSaveHook(eventPublisher);
    }

    @Bean
    CUSTOMER_NOTIFIEDFulfillmentPostSaveHook fulfillmentCUSTOMER_NOTIFIEDPostSaveHook(
            FulfillmentEventPublisherPort eventPublisher) {
        return new CUSTOMER_NOTIFIEDFulfillmentPostSaveHook(eventPublisher);
    }

    @Bean
    SHIPPEDFulfillmentPostSaveHook fulfillmentSHIPPEDPostSaveHook(
            FulfillmentEventPublisherPort eventPublisher) {
        return new SHIPPEDFulfillmentPostSaveHook(eventPublisher);
    }

    @Bean
    COMPLETEDFulfillmentPostSaveHook fulfillmentCOMPLETEDPostSaveHook(
            FulfillmentEventPublisherPort eventPublisher) {
        return new COMPLETEDFulfillmentPostSaveHook(eventPublisher);
    }

    @Bean
    FAILEDFulfillmentPostSaveHook fulfillmentFAILEDPostSaveHook(
            FulfillmentEventPublisherPort eventPublisher) {
        return new FAILEDFulfillmentPostSaveHook(eventPublisher);
    }

    // --- OWIZ Chain Configuration ---

    @Bean
    OwizSpringFactoryAdapter fulfillmentOwizBeanFactory() {
        return new OwizSpringFactoryAdapter();
    }

    @Bean
    XmlOrchConfigurator<ChenileExchange> fulfillmentOwizConfigurator(
            @Qualifier("fulfillmentOwizBeanFactory") OwizSpringFactoryAdapter beanFactory) throws Exception {
        XmlOrchConfigurator<ChenileExchange> config = new XmlOrchConfigurator<>();
        config.setBeanFactoryAdapter(beanFactory);
        config.setFilename(OWIZ_FLOW_DEFINITION_FILE);
        return config;
    }

    @Bean
    OrchExecutor<ChenileExchange> fulfillmentOwizExecutor(
            @Qualifier("fulfillmentOwizConfigurator") XmlOrchConfigurator<ChenileExchange> configurator) {
        OrchExecutorImpl<ChenileExchange> executor = new OrchExecutorImpl<>();
        executor.setOrchConfigurator(configurator);
        return executor;
    }

    // --- Event Publisher ---

    @Bean
    FulfillmentEventPublisherPort fulfillmentEventPublisherPort(
            ChenilePub chenilePub, ObjectMapper objectMapper) {
        return new KafkaFulfillmentEventPublisher(chenilePub, objectMapper);
    }

    // --- Domain Repository Adapter ---

    @Bean
    FulfillmentSagaRepository fulfillmentSagaRepository(
            FulfillmentSagaJpaRepository jpaRepository,
            FulfillmentSagaMapper mapper) {
        return new FulfillmentSagaRepositoryImpl(jpaRepository, mapper);
    }

    // --- Kafka Consumer ---

    @Bean
    OrderPaidEventConsumer orderPaidEventConsumer() {
        return new OrderPaidEventConsumer();
    }

    // --- OWIZ Command Beans (reserve inventory sub-steps) ---

    @Bean
    RouteToWarehouse routeToWarehouse() {
        return new RouteToWarehouse();
    }

    @Bean
    CheckSplitShipment checkSplitShipment() {
        return new CheckSplitShipment();
    }

    @Bean
    ValidateStockAvailability validateStockAvailability() {
        return new ValidateStockAvailability();
    }

    @Bean
    DeductStockFromWarehouse deductStockFromWarehouse() {
        return new DeductStockFromWarehouse();
    }

    @Bean
    ConfirmReservation confirmReservation() {
        return new ConfirmReservation();
    }

    // --- OWIZ Command Beans (create shipment sub-steps) ---

    @Bean
    ValidateWeightDimensions validateWeightDimensions() {
        return new ValidateWeightDimensions();
    }

    @Bean
    SelectCarrier selectCarrier() {
        return new SelectCarrier();
    }

    @Bean
    CheckCarrierSLA checkCarrierSLA() {
        return new CheckCarrierSLA();
    }

    @Bean
    GeneratePickList generatePickList() {
        return new GeneratePickList();
    }

    @Bean
    GeneratePackSlip generatePackSlip() {
        return new GeneratePackSlip();
    }

    @Bean
    GenerateTrackingNumber generateTrackingNumber() {
        return new GenerateTrackingNumber();
    }

    @Bean
    CreateShipmentRecord createShipmentRecord() {
        return new CreateShipmentRecord();
    }

    // --- OWIZ Command Beans (notify customer sub-steps) ---

    @Bean
    ResolveChannelPreference resolveChannelPreference() {
        return new ResolveChannelPreference();
    }

    @Bean
    PrepareShipmentNotification prepareShipmentNotification() {
        return new PrepareShipmentNotification();
    }

    @Bean
    SendCustomerNotification sendCustomerNotification() {
        return new SendCustomerNotification();
    }

    @Bean
    UpdateNotificationStatus updateNotificationStatus() {
        return new UpdateNotificationStatus();
    }

    @Bean
    java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> fulfillmentEventAuthoritiesSupplier(
            @Qualifier("fulfillmentActionsInfoProvider") STMActionsInfoProvider fulfillmentInfoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(fulfillmentInfoProvider, false);
        return builder.build();
    }
}
