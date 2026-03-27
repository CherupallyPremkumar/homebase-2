package com.homebase.ecom.returnprocessing.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebase.ecom.returnprocessing.event.ReturnApprovedEventConsumer;
import com.homebase.ecom.returnprocessing.infrastructure.persistence.ChenileReturnProcessingEntityStore;
import com.homebase.ecom.returnprocessing.infrastructure.persistence.adapter.ReturnProcessingSagaRepositoryImpl;
import com.homebase.ecom.returnprocessing.infrastructure.persistence.mapper.ReturnProcessingSagaMapper;
import com.homebase.ecom.returnprocessing.infrastructure.persistence.repository.ReturnProcessingSagaJpaRepository;
import com.homebase.ecom.returnprocessing.port.ReturnProcessingSagaRepository;
import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import com.homebase.ecom.returnprocessing.port.ReturnProcessingEventPublisherPort;
import com.homebase.ecom.returnprocessing.service.cmds.*;
import com.homebase.ecom.returnprocessing.service.postSaveHooks.*;
import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.action.scriptsupport.IfAction;
import org.chenile.stm.ognl.OgnlScriptingStrategy;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.param.MinimalPayload;
import com.homebase.ecom.core.workflow.HmStateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Full Chenile STM configuration for the return processing saga.
 * All beans are wired explicitly here -- no @Component/@Service/@Repository on classes.
 */
@Configuration
public class ReturnProcessingConfiguration {

    private static final String FLOW_DEFINITION_FILE =
            "com/homebase/ecom/returnprocessing/return-processing-states.xml";
    public static final String PREFIX_FOR_RESOLVER = "returnProcessing";

    // ==================== OGNL / Scripting ====================

    @Bean
    OgnlScriptingStrategy ognlScriptingStrategy() {
        return new OgnlScriptingStrategy();
    }

    @Bean
    IfAction<ReturnProcessingSaga> ifAction() {
        return new IfAction<>();
    }

    // ==================== Kafka Consumer ====================

    @Bean
    ReturnApprovedEventConsumer returnApprovedEventConsumer(
            ReturnProcessingSagaRepository sagaRepository,
            ObjectMapper objectMapper) {
        return new ReturnApprovedEventConsumer(sagaRepository, objectMapper);
    }

    // ==================== Infrastructure Beans ====================

    @Bean
    ReturnProcessingSagaMapper returnProcessingSagaMapper() {
        return new ReturnProcessingSagaMapper();
    }

    @Bean
    ReturnProcessingSagaRepository returnProcessingSagaRepository(
            ReturnProcessingSagaJpaRepository jpaRepository,
            ReturnProcessingSagaMapper mapper) {
        return new ReturnProcessingSagaRepositoryImpl(jpaRepository, mapper);
    }

    // ==================== STM Core Wiring ====================

    @Bean
    BeanFactoryAdapter returnProcessingBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl returnProcessingFlowStore(
            @Qualifier("returnProcessingBeanFactoryAdapter") BeanFactoryAdapter beanFactoryAdapter) {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(beanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean
    STM<ReturnProcessingSaga> returnProcessingEntityStm(
            @Qualifier("returnProcessingFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMImpl<ReturnProcessingSaga> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider returnProcessingActionsInfoProvider(
            @Qualifier("returnProcessingFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("returnProcessing", provider);
        return provider;
    }

    @Bean
    EntityStore<ReturnProcessingSaga> returnProcessingEntityStore(
            ReturnProcessingSagaJpaRepository jpaRepository,
            ReturnProcessingSagaMapper mapper) {
        return new ChenileReturnProcessingEntityStore(jpaRepository, mapper);
    }

    @Bean
    HmStateEntityServiceImpl<ReturnProcessingSaga> _returnProcessingStateEntityService_(
            @Qualifier("returnProcessingEntityStm") STM<ReturnProcessingSaga> stm,
            @Qualifier("returnProcessingActionsInfoProvider") STMActionsInfoProvider infoProvider,
            @Qualifier("returnProcessingEntityStore") EntityStore<ReturnProcessingSaga> entityStore) {
        return new HmStateEntityServiceImpl<>(stm, infoProvider, entityStore);
    }

    // ==================== STM Transition Action Resolution ====================

    @Bean
    STMTransitionAction<ReturnProcessingSaga> defaultReturnProcessingSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver returnProcessingTransitionActionResolver(
            @Qualifier("defaultReturnProcessingSTMTransitionAction") STMTransitionAction<ReturnProcessingSaga> defaultAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultAction, true);
    }

    // ==================== Entry / Exit / PostSave / AutoState ====================

    @Bean
    DefaultPostSaveHook<ReturnProcessingSaga> returnProcessingDefaultPostSaveHook(
            @Qualifier("returnProcessingTransitionActionResolver") STMTransitionActionResolver resolver) {
        return new DefaultPostSaveHook<>(resolver);
    }

    @Bean
    GenericEntryAction<ReturnProcessingSaga> returnProcessingEntryAction(
            @Qualifier("returnProcessingEntityStore") EntityStore<ReturnProcessingSaga> entityStore,
            @Qualifier("returnProcessingActionsInfoProvider") STMActionsInfoProvider infoProvider,
            @Qualifier("returnProcessingFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("returnProcessingDefaultPostSaveHook") DefaultPostSaveHook<ReturnProcessingSaga> postSaveHook) {
        GenericEntryAction<ReturnProcessingSaga> entryAction = new GenericEntryAction<>(entityStore, infoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    DefaultAutomaticStateComputation<ReturnProcessingSaga> returnProcessingDefaultAutoState(
            @Qualifier("returnProcessingTransitionActionResolver") STMTransitionActionResolver resolver,
            @Qualifier("returnProcessingFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<ReturnProcessingSaga> autoState = new DefaultAutomaticStateComputation<>(resolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    GenericExitAction<ReturnProcessingSaga> returnProcessingExitAction(
            @Qualifier("returnProcessingFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<ReturnProcessingSaga> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    XmlFlowReader returnProcessingFlowReader(
            @Qualifier("returnProcessingFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean
    STMTransitionAction<ReturnProcessingSaga> returnProcessingBaseTransitionAction(
            @Qualifier("returnProcessingTransitionActionResolver") STMTransitionActionResolver resolver,
            @Qualifier("returnProcessingFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<ReturnProcessingSaga> baseTransitionAction = new BaseTransitionAction<>(resolver);
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean
    StmBodyTypeSelector returnProcessingBodyTypeSelector(
            @Qualifier("returnProcessingActionsInfoProvider") STMActionsInfoProvider infoProvider,
            @Qualifier("returnProcessingTransitionActionResolver") STMTransitionActionResolver resolver) {
        return new StmBodyTypeSelector(infoProvider, resolver);
    }

    // ==================== Transition Actions ====================
    // Bean names follow convention: PREFIX_FOR_RESOLVER + eventId + "Action"

    @Bean
    SchedulePickupAction returnProcessingSchedulePickupAction(
            ReturnProcessingEventPublisherPort eventPublisher) {
        return new SchedulePickupAction(eventPublisher);
    }

    @Bean
    ReceiveItemAction returnProcessingReceiveItemAction() {
        return new ReceiveItemAction();
    }

    @Bean
    InspectItemAction returnProcessingInspectItemAction() {
        return new InspectItemAction();
    }

    @Bean
    RestockInventoryAction returnProcessingRestockInventoryAction(
            ReturnProcessingEventPublisherPort eventPublisher) {
        return new RestockInventoryAction(eventPublisher);
    }

    @Bean
    AdjustSettlementAction returnProcessingAdjustSettlementAction(
            ReturnProcessingEventPublisherPort eventPublisher) {
        return new AdjustSettlementAction(eventPublisher);
    }

    @Bean
    ProcessRefundAction returnProcessingProcessRefundAction(
            ReturnProcessingEventPublisherPort eventPublisher) {
        return new ProcessRefundAction(eventPublisher);
    }

    @Bean
    NotifyReturnCompleteAction returnProcessingNotifyCustomerAction() {
        return new NotifyReturnCompleteAction();
    }

    @Bean
    RetryAction returnProcessingRetryAction() {
        return new RetryAction();
    }

    @Bean
    FailAction returnProcessingFailAction() {
        return new FailAction();
    }

    @Bean
    CancelAction returnProcessingCancelAction() {
        return new CancelAction();
    }

    @Bean
    CompensateAction returnProcessingCompensateAction() {
        return new CompensateAction();
    }

    // ==================== Post-Save Hooks ====================
    // Bean names follow convention: PREFIX_FOR_RESOLVER + STATE_ID + "PostSaveHook"

    @Bean
    PICKUP_SCHEDULEDReturnProcessingPostSaveHook returnProcessingPICKUP_SCHEDULEDPostSaveHook() {
        return new PICKUP_SCHEDULEDReturnProcessingPostSaveHook();
    }

    @Bean
    ITEM_RECEIVEDReturnProcessingPostSaveHook returnProcessingITEM_RECEIVEDPostSaveHook() {
        return new ITEM_RECEIVEDReturnProcessingPostSaveHook();
    }

    @Bean
    INVENTORY_RESTOCKEDReturnProcessingPostSaveHook returnProcessingINVENTORY_RESTOCKEDPostSaveHook() {
        return new INVENTORY_RESTOCKEDReturnProcessingPostSaveHook();
    }

    @Bean
    SETTLEMENT_ADJUSTEDReturnProcessingPostSaveHook returnProcessingSETTLEMENT_ADJUSTEDPostSaveHook() {
        return new SETTLEMENT_ADJUSTEDReturnProcessingPostSaveHook();
    }

    @Bean
    REFUNDEDReturnProcessingPostSaveHook returnProcessingREFUNDEDPostSaveHook() {
        return new REFUNDEDReturnProcessingPostSaveHook();
    }

    @Bean
    COMPLETEDReturnProcessingPostSaveHook returnProcessingCOMPLETEDPostSaveHook() {
        return new COMPLETEDReturnProcessingPostSaveHook();
    }

    @Bean
    FAILEDReturnProcessingPostSaveHook returnProcessingFAILEDPostSaveHook() {
        return new FAILEDReturnProcessingPostSaveHook();
    }

    @Bean
    java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> returnProcessingEventAuthoritiesSupplier(
            @Qualifier("returnProcessingActionsInfoProvider") STMActionsInfoProvider returnProcessingInfoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(returnProcessingInfoProvider, false);
        return builder.build();
    }
}
