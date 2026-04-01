package com.homebase.ecom.payment.configuration;

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
import com.homebase.ecom.core.workflow.HmStateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.infrastructure.persistence.mapper.PaymentMapper;
import com.homebase.ecom.payment.infrastructure.persistence.repository.PaymentJpaRepository;
import com.homebase.ecom.payment.infrastructure.persistence.adapter.PaymentQueryAdapter;
import com.homebase.ecom.payment.infrastructure.persistence.ChenilePaymentEntityStore;
import com.homebase.ecom.payment.service.cmds.*;
import com.homebase.ecom.payment.service.event.PaymentEventHandler;
import com.homebase.ecom.payment.service.postSaveHooks.*;
import com.homebase.ecom.payment.service.validator.PaymentPolicyValidator;
import org.chenile.stm.action.scriptsupport.IfAction;
import org.chenile.stm.ognl.OgnlScriptingStrategy;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;

/**
 * Full Chenile STM configuration for the Payment bounded context.
 * Wires up state machine, entity store, transition actions, post-save hooks,
 * event handler, and policy validator.
 */
@Configuration
public class PaymentConfiguration {
    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/payment/payment-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Payment";
    public static final String PREFIX_FOR_RESOLVER = "payment";

    @Bean
    BeanFactoryAdapter paymentBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl paymentFlowStore(
            @Qualifier("paymentBeanFactoryAdapter") BeanFactoryAdapter paymentBeanFactoryAdapter) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(paymentBeanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean
    STM<Payment> paymentEntityStm(@Qualifier("paymentFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<Payment> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider paymentActionsInfoProvider(
            @Qualifier("paymentFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("payment", provider);
        return provider;
    }

    @Bean
    PaymentMapper paymentMapper() {
        return new PaymentMapper();
    }

    @Bean
    PaymentQueryAdapter paymentQueryAdapter(PaymentJpaRepository jpaRepository, PaymentMapper mapper) {
        return new PaymentQueryAdapter(jpaRepository, mapper);
    }

    @Bean
    EntityStore<Payment> paymentEntityStore(PaymentJpaRepository repository, PaymentMapper mapper) {
        return new ChenilePaymentEntityStore(repository, mapper);
    }

    @Bean
    StateEntityServiceImpl<Payment> _paymentStateEntityService_(
            @Qualifier("paymentEntityStm") STM<Payment> stm,
            @Qualifier("paymentActionsInfoProvider") STMActionsInfoProvider paymentInfoProvider,
            @Qualifier("paymentEntityStore") EntityStore<Payment> entityStore) {
        return new HmStateEntityServiceImpl<>(stm, paymentInfoProvider, entityStore);
    }

    // ── STM Components ─────────────────────────────────────────────────────

    @Bean
    DefaultPostSaveHook<Payment> paymentDefaultPostSaveHook(
            @Qualifier("paymentTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean
    GenericEntryAction<Payment> paymentEntryAction(
            @Qualifier("paymentEntityStore") EntityStore<Payment> entityStore,
            @Qualifier("paymentActionsInfoProvider") STMActionsInfoProvider paymentInfoProvider,
            @Qualifier("paymentFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("paymentDefaultPostSaveHook") DefaultPostSaveHook<Payment> postSaveHook) {
        GenericEntryAction<Payment> entryAction = new GenericEntryAction<>(entityStore, paymentInfoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    DefaultAutomaticStateComputation<Payment> paymentDefaultAutoState(
            @Qualifier("paymentTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("paymentFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<Payment> autoState = new DefaultAutomaticStateComputation<>(
                stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    GenericExitAction<Payment> paymentExitAction(@Qualifier("paymentFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<Payment> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    XmlFlowReader paymentFlowReader(@Qualifier("paymentFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean(name = "ognlScriptingStrategy")
    OgnlScriptingStrategy ognlScriptingStrategy() {
        return new OgnlScriptingStrategy();
    }

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean(name = "ifAction")
    IfAction<Payment> ifAction() {
        return new IfAction<>();
    }

    @Bean
    STMTransitionAction<Payment> defaultpaymentSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver paymentTransitionActionResolver(
            @Qualifier("defaultpaymentSTMTransitionAction") STMTransitionAction<Payment> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean
    StmBodyTypeSelector paymentBodyTypeSelector(
            @Qualifier("paymentActionsInfoProvider") STMActionsInfoProvider paymentInfoProvider,
            @Qualifier("paymentTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(paymentInfoProvider, stmTransitionActionResolver);
    }

    @Bean
    STMTransitionAction<Payment> paymentBaseTransitionAction(
            @Qualifier("paymentTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("paymentActivityChecker") ActivityChecker activityChecker,
            @Qualifier("paymentFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<Payment> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean
    ActivityChecker paymentActivityChecker(@Qualifier("paymentFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete paymentActivitiesCompletionCheck(
            @Qualifier("paymentActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    @Bean
    ConfigProviderImpl paymentConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    ConfigBasedEnablementStrategy paymentConfigBasedEnablementStrategy(
            @Qualifier("paymentConfigProvider") ConfigProvider configProvider,
            @Qualifier("paymentFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider,
                PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // ── Transition Actions ─────────────────────────────────────────────────
    // Convention: "payment" + eventId + "Action"

    @Bean
    InitiatePaymentAction paymentCreateAction(PaymentPolicyValidator paymentPolicyValidator) {
        return new InitiatePaymentAction(paymentPolicyValidator);
    }

    @Bean
    ProcessPaymentAction paymentProcessAction() {
        return new ProcessPaymentAction();
    }

    @Bean
    SucceedPaymentAction paymentSucceedAction() {
        return new SucceedPaymentAction();
    }

    @Bean
    FailPaymentAction paymentFailAction() {
        return new FailPaymentAction();
    }

    @Bean
    RetryPaymentAction paymentRetryAction(PaymentPolicyValidator paymentPolicyValidator) {
        return new RetryPaymentAction(paymentPolicyValidator);
    }

    @Bean
    AbandonPaymentAction paymentAbandonAction() {
        return new AbandonPaymentAction();
    }

    @Bean
    SettlePaymentAction paymentSettleAction() {
        return new SettlePaymentAction();
    }

    @Bean
    InitiateRefundPaymentAction paymentInitiateRefundAction() {
        return new InitiateRefundPaymentAction();
    }

    @Bean
    ProcessRefundPaymentAction paymentProcessRefundAction() {
        return new ProcessRefundPaymentAction();
    }

    @Bean
    CompleteRefundPaymentAction paymentCompleteRefundAction() {
        return new CompleteRefundPaymentAction();
    }

    @Bean
    RequestAuthenticationAction paymentRequestAuthenticationAction() {
        return new RequestAuthenticationAction();
    }

    @Bean
    CompleteAuthenticationAction paymentCompleteAuthenticationAction() {
        return new CompleteAuthenticationAction();
    }

    @Bean
    FailAuthenticationAction paymentFailAuthenticationAction() {
        return new FailAuthenticationAction();
    }

    @Bean
    TimeoutPaymentAction paymentTimeoutAction() {
        return new TimeoutPaymentAction();
    }

    @Bean
    InitiateChargebackAction paymentInitiateChargebackAction() {
        return new InitiateChargebackAction();
    }

    @Bean
    ResolveChargebackAction paymentResolveChargebackAction() {
        return new ResolveChargebackAction();
    }

    @Bean
    FailRefundAction paymentFailRefundAction() {
        return new FailRefundAction();
    }

    @Bean
    CollectCodAction paymentCollectCodAction() {
        return new CollectCodAction();
    }

    @Bean
    InitiateCodAction paymentInitiateCodAction() {
        return new InitiateCodAction();
    }

    // ── PostSaveHooks ──────────────────────────────────────────────────────
    // Convention: "payment" + STATE_ID + "PostSaveHook"

    @Bean
    SUCCEEDEDPaymentPostSaveHook paymentSUCCEEDEDPostSaveHook(
            com.homebase.ecom.payment.domain.port.PaymentEventPublisherPort eventPublisher) {
        return new SUCCEEDEDPaymentPostSaveHook(eventPublisher);
    }

    @Bean
    FAILEDPaymentPostSaveHook paymentFAILEDPostSaveHook(
            com.homebase.ecom.payment.domain.port.PaymentEventPublisherPort eventPublisher,
            @Autowired(required = false) com.homebase.ecom.payment.domain.port.NotificationPort notificationPort) {
        return new FAILEDPaymentPostSaveHook(eventPublisher, notificationPort);
    }

    @Bean
    REFUNDEDPaymentPostSaveHook paymentREFUNDEDPostSaveHook(
            com.homebase.ecom.payment.domain.port.PaymentEventPublisherPort eventPublisher,
            @Autowired(required = false) com.homebase.ecom.payment.domain.port.NotificationPort notificationPort) {
        return new REFUNDEDPaymentPostSaveHook(eventPublisher, notificationPort);
    }

    @Bean
    ABANDONEDPaymentPostSaveHook paymentABANDONEDPostSaveHook(
            com.homebase.ecom.payment.domain.port.PaymentEventPublisherPort eventPublisher) {
        return new ABANDONEDPaymentPostSaveHook(eventPublisher);
    }

    // ── Hexagonal Port Adapters ───────────────────────────────────────────

    @Bean("paymentNotificationPort")
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean(com.homebase.ecom.payment.domain.port.NotificationPort.class)
    com.homebase.ecom.payment.domain.port.NotificationPort paymentNotificationPort() {
        return new com.homebase.ecom.payment.infrastructure.adapter.LoggingNotificationAdapter();
    }

    // ── Policy Validator ───────────────────────────────────────────────────

    @Bean
    PaymentPolicyValidator paymentPolicyValidator(
            @Autowired(required = false) com.homebase.ecom.cconfig.sdk.CconfigClient cconfigClient) {
        return new PaymentPolicyValidator(cconfigClient);
    }

    // ── ACL: Event authorities supplier ────────────────────────────────────

    @Bean
    java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> paymentEventAuthoritiesSupplier(
            @Qualifier("paymentActionsInfoProvider") STMActionsInfoProvider paymentInfoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(paymentInfoProvider, false);
        return builder.build();
    }

    // ── Kafka Event Handler ────────────────────────────────────────────────

    @Bean("paymentEventService")
    PaymentEventHandler paymentEventService(
            PaymentQueryAdapter paymentQueryAdapter,
            @Qualifier("_paymentStateEntityService_") StateEntityServiceImpl<Payment> paymentStateEntityService,
            org.chenile.pubsub.ChenilePub chenilePub,
            tools.jackson.databind.ObjectMapper objectMapper) {
        return new PaymentEventHandler(
                paymentQueryAdapter, paymentStateEntityService, chenilePub, objectMapper);
    }
}
