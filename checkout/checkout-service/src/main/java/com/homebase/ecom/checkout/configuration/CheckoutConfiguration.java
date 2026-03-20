package com.homebase.ecom.checkout.configuration;

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
import org.chenile.workflow.api.WorkflowRegistry;

import com.homebase.ecom.checkout.domain.port.*;
import com.homebase.ecom.checkout.model.Checkout;
import com.homebase.ecom.checkout.service.cmds.*;
import com.homebase.ecom.checkout.service.saga.*;
import com.homebase.ecom.checkout.infrastructure.persistence.ChenileCheckoutEntityStore;
import com.homebase.ecom.checkout.infrastructure.persistence.repository.CheckoutJpaRepository;
import com.homebase.ecom.checkout.infrastructure.persistence.mapper.CheckoutMapper;


/**
 * Checkout module Spring configuration.
 * Wires STM, entity store, OWIZ saga commands (with hexagonal ports),
 * transition actions, and stub adapters for testing.
 */
@Configuration
public class CheckoutConfiguration {

    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/checkout/checkout-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Checkout";
    public static final String PREFIX_FOR_RESOLVER = "checkout";

    // ═══════════════════════════════════════════════════════════════════
    // STM Infrastructure
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    BeanFactoryAdapter checkoutBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    OgnlScriptingStrategy ognlScriptingStrategy() {
        return new OgnlScriptingStrategy();
    }

    @Bean
    IfAction<Checkout> ifAction() {
        return new IfAction<>();
    }

    @Bean
    STMFlowStoreImpl checkoutFlowStore(
            @Qualifier("checkoutBeanFactoryAdapter") BeanFactoryAdapter checkoutBeanFactoryAdapter) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(checkoutBeanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean
    STM<Checkout> checkoutEntityStm(@Qualifier("checkoutFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<Checkout> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider checkoutActionsInfoProvider(@Qualifier("checkoutFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("checkout", provider);
        return provider;
    }

    @Bean
    CheckoutMapper checkoutMapper() {
        return new CheckoutMapper();
    }

    @Bean
    EntityStore<Checkout> checkoutEntityStore(CheckoutJpaRepository repository, CheckoutMapper mapper) {
        return new ChenileCheckoutEntityStore(repository, mapper);
    }

    @Bean
    StateEntityServiceImpl<Checkout> _checkoutStateEntityService_(
            @Qualifier("checkoutEntityStm") STM<Checkout> stm,
            @Qualifier("checkoutActionsInfoProvider") STMActionsInfoProvider checkoutInfoProvider,
            @Qualifier("checkoutEntityStore") EntityStore<Checkout> entityStore) {
        return new HmStateEntityServiceImpl<>(stm, checkoutInfoProvider, entityStore);
    }

    // ═══════════════════════════════════════════════════════════════════
    // STM Components: Entry/Exit actions, PostSaveHook, AutoState
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    DefaultPostSaveHook<Checkout> checkoutDefaultPostSaveHook(
            @Qualifier("checkoutTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean
    GenericEntryAction<Checkout> checkoutEntryAction(
            @Qualifier("checkoutEntityStore") EntityStore<Checkout> entityStore,
            @Qualifier("checkoutActionsInfoProvider") STMActionsInfoProvider checkoutInfoProvider,
            @Qualifier("checkoutFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("checkoutDefaultPostSaveHook") DefaultPostSaveHook<Checkout> postSaveHook) {
        GenericEntryAction<Checkout> entryAction = new GenericEntryAction<>(entityStore, checkoutInfoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    DefaultAutomaticStateComputation<Checkout> checkoutDefaultAutoState(
            @Qualifier("checkoutTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("checkoutFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<Checkout> autoState = new DefaultAutomaticStateComputation<>(
                stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    GenericExitAction<Checkout> checkoutExitAction(@Qualifier("checkoutFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<Checkout> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    XmlFlowReader checkoutFlowReader(@Qualifier("checkoutFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    // ═══════════════════════════════════════════════════════════════════
    // Transition Action Resolver
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    STMTransitionAction<Checkout> defaultcheckoutSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver checkoutTransitionActionResolver(
            @Qualifier("defaultcheckoutSTMTransitionAction") STMTransitionAction<Checkout> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean
    StmBodyTypeSelector checkoutBodyTypeSelector(
            @Qualifier("checkoutActionsInfoProvider") STMActionsInfoProvider checkoutInfoProvider,
            @Qualifier("checkoutTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(checkoutInfoProvider, stmTransitionActionResolver);
    }

    @Bean
    STMTransitionAction<Checkout> checkoutBaseTransitionAction(
            @Qualifier("checkoutTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("checkoutFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<Checkout> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    // ═══════════════════════════════════════════════════════════════════
    // Enablement strategy (cconfig)
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    ConfigProviderImpl checkoutConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    ConfigBasedEnablementStrategy checkoutConfigBasedEnablementStrategy(
            @Qualifier("checkoutConfigProvider") ConfigProvider configProvider,
            @Qualifier("checkoutFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider,
                PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // ═══════════════════════════════════════════════════════════════════
    // STM Transition Actions
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    ProcessCheckoutAction checkoutProcessAction() {
        return new ProcessCheckoutAction();
    }

    @Bean
    PaymentSuccessAction checkoutPaymentSuccessAction() {
        return new PaymentSuccessAction();
    }

    @Bean
    PaymentFailedAction checkoutPaymentFailedAction() {
        return new PaymentFailedAction();
    }

    @Bean
    CancelCheckoutAction checkoutCancelAction() {
        return new CancelCheckoutAction();
    }

    @Bean
    ExpireCheckoutAction checkoutExpireAction() {
        return new ExpireCheckoutAction();
    }

    @Bean
    RetryPaymentAction checkoutRetryPaymentAction() {
        return new RetryPaymentAction();
    }

    @Bean
    CompensateCheckoutAction checkoutCompensateAction(
            CartLockPort cartLockPort,
            InventoryReservePort inventoryReservePort,
            OrderCreationPort orderCreationPort,
            PromoCommitPort promoCommitPort,
            PaymentInitiationPort paymentInitiationPort) {
        return new CompensateCheckoutAction(cartLockPort, inventoryReservePort,
                orderCreationPort, promoCommitPort, paymentInitiationPort);
    }

    // ═══════════════════════════════════════════════════════════════════
    // OWIZ Saga Commands (wired into checkout-saga.xml chain)
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    ValidateAddressCommand validateAddressCommand() {
        return new ValidateAddressCommand();
    }

    @Bean
    LockCartCommand lockCartCommand(CartLockPort cartLockPort) {
        return new LockCartCommand(cartLockPort);
    }

    @Bean
    LockPriceCommand lockPriceCommand(PriceLockPort priceLockPort) {
        return new LockPriceCommand(priceLockPort);
    }

    @Bean
    ReserveInventoryCommand reserveInventoryCommand(InventoryReservePort inventoryReservePort) {
        return new ReserveInventoryCommand(inventoryReservePort);
    }

    @Bean
    ValidateShippingCommand validateShippingCommand(ShippingValidationPort shippingValidationPort) {
        return new ValidateShippingCommand(shippingValidationPort);
    }

    @Bean
    CreateOrderCommand createOrderCommand(OrderCreationPort orderCreationPort) {
        return new CreateOrderCommand(orderCreationPort);
    }

    @Bean
    EstimateDeliveryCommand estimateDeliveryCommand() {
        return new EstimateDeliveryCommand();
    }

    @Bean
    ScreenFraudCommand screenFraudCommand() {
        return new ScreenFraudCommand();
    }

    @Bean
    CommitPromoCommand commitPromoCommand(PromoCommitPort promoCommitPort) {
        return new CommitPromoCommand(promoCommitPort);
    }

    @Bean
    InitiatePaymentCommand initiatePaymentCommand(PaymentInitiationPort paymentInitiationPort) {
        return new InitiatePaymentCommand(paymentInitiationPort);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Port beans are NOT provided here — they MUST come from:
    //   - checkout-infrastructure (real adapters calling other services)
    //   - SpringTestConfig (test stubs)
    // No @ConditionalOnMissingBean stubs — production must fail-fast
    // if a real adapter is not wired.
    // ═══════════════════════════════════════════════════════════════════

    // ═══════════════════════════════════════════════════════════════════
    // ACL: STM Authorities Builder
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> checkoutEventAuthoritiesSupplier(
            @Qualifier("checkoutActionsInfoProvider") STMActionsInfoProvider checkoutInfoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(checkoutInfoProvider, false);
        return builder.build();
    }
}
