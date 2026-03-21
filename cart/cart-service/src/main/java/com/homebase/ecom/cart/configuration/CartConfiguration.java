package com.homebase.ecom.cart.configuration;

import org.chenile.core.context.ChenileExchange;
import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.stmcmds.*;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.service.cmds.*;
import com.homebase.ecom.cart.service.impl.CartServiceImpl;
import com.homebase.ecom.cart.service.mapper.CartDtoMapper;
import com.homebase.ecom.cart.service.healthcheck.CartHealthChecker;
import com.homebase.ecom.cart.repository.CartRepository;
import com.homebase.ecom.cart.port.CartEventPublisherPort;
import com.homebase.ecom.cart.port.ConfigPort;
import com.homebase.ecom.cart.service.handler.CartExternalEventHandler;
import com.homebase.ecom.cart.service.validator.CartPolicyValidator;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.cart.service.postSaveHooks.*;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import java.util.function.Function;

/**
 * Cart module Spring configuration.
 * Wires STM, entity store, transition actions, post-save hooks,
 * hexagonal ports/adapters, and chenile-kafka event handler.
 */
@Configuration
public class CartConfiguration {

    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/cart/cart-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Cart";
    public static final String PREFIX_FOR_RESOLVER = "cart";

    // ═══════════════════════════════════════════════════════════════════
    // STM Infrastructure
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    BeanFactoryAdapter cartBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl cartFlowStore(
            @Qualifier("cartBeanFactoryAdapter") BeanFactoryAdapter cartBeanFactoryAdapter) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(cartBeanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean
    STM<Cart> cartEntityStm(@Qualifier("cartFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<Cart> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider cartActionsInfoProvider(@Qualifier("cartFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("cart", provider);
        return provider;
    }

    @Bean
    CartDtoMapper cartDtoMapper() {
        return new CartDtoMapper();
    }

    @Bean
    CartExternalEventHandler cartExternalEventHandler(
            CartExternalEventHandler.CartQueryPort cartQueryPort,
            ObjectMapper objectMapper) {
        return new CartExternalEventHandler(cartQueryPort, objectMapper);
    }

    @Bean
    CartServiceImpl _cartStateEntityService_(
            @Qualifier("cartEntityStm") STM<Cart> stm,
            @Qualifier("cartActionsInfoProvider") STMActionsInfoProvider cartInfoProvider,
            @Qualifier("cartEntityStore") EntityStore<Cart> entityStore,
            CartDtoMapper cartDtoMapper,
            CartExternalEventHandler externalEventHandler) {
        return new CartServiceImpl(stm, cartInfoProvider, entityStore, cartDtoMapper, externalEventHandler);
    }

    // ═══════════════════════════════════════════════════════════════════
    // STM Components: Entry/Exit actions, PostSaveHook, AutoState
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    DefaultPostSaveHook<Cart> cartDefaultPostSaveHook(
            @Qualifier("cartTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean
    GenericEntryAction<Cart> cartEntryAction(
            @Qualifier("cartEntityStore") EntityStore<Cart> entityStore,
            @Qualifier("cartActionsInfoProvider") STMActionsInfoProvider cartInfoProvider,
            @Qualifier("cartFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("cartDefaultPostSaveHook") DefaultPostSaveHook<Cart> postSaveHook) {
        GenericEntryAction<Cart> entryAction = new GenericEntryAction<>(entityStore, cartInfoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    DefaultAutomaticStateComputation<Cart> cartDefaultAutoState(
            @Qualifier("cartTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("cartFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<Cart> autoState = new DefaultAutomaticStateComputation<>(
                stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    GenericExitAction<Cart> cartExitAction(@Qualifier("cartFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<Cart> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    XmlFlowReader cartFlowReader(@Qualifier("cartFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    // ═══════════════════════════════════════════════════════════════════
    // Transition Action Resolver
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    STMTransitionAction<Cart> defaultcartSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver cartTransitionActionResolver(
            @Qualifier("defaultcartSTMTransitionAction") STMTransitionAction<Cart> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean
    StmBodyTypeSelector cartBodyTypeSelector(
            @Qualifier("cartActionsInfoProvider") STMActionsInfoProvider cartInfoProvider,
            @Qualifier("cartTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(cartInfoProvider, stmTransitionActionResolver);
    }

    @Bean
    STMTransitionAction<Cart> cartBaseTransitionAction(
            @Qualifier("cartTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("cartActivityChecker") ActivityChecker activityChecker,
            @Qualifier("cartFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<Cart> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean
    ActivityChecker cartActivityChecker(@Qualifier("cartFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete activitiesCompletionCheck(@Qualifier("cartActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Health checker
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    CartHealthChecker cartHealthChecker() {
        return new CartHealthChecker();
    }

    // ═══════════════════════════════════════════════════════════════════
    // Enablement strategy (cconfig)
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    ConfigProviderImpl cartConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    ConfigBasedEnablementStrategy cartConfigBasedEnablementStrategy(
            @Qualifier("cartConfigProvider") ConfigProvider configProvider,
            @Qualifier("cartFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider,
                PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // ═══════════════════════════════════════════════════════════════════
    // STM Transition Actions (convention: cart + eventId + Action)
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    AddItemCartAction cartAddItemAction() {
        return new AddItemCartAction();
    }

    @Bean
    RemoveItemCartAction cartRemoveItemAction() {
        return new RemoveItemCartAction();
    }

    @Bean
    UpdateQuantityCartAction cartUpdateQuantityAction() {
        return new UpdateQuantityCartAction();
    }

    @Bean
    ApplyCouponCartAction cartApplyCouponAction() {
        return new ApplyCouponCartAction();
    }

    @Bean
    RemoveCouponCartAction cartRemoveCouponAction() {
        return new RemoveCouponCartAction();
    }

    @Bean
    InitiateCheckoutCartAction cartInitiateCheckoutAction() {
        return new InitiateCheckoutCartAction();
    }

    @Bean
    CompleteCheckoutCartAction cartCompleteCheckoutAction() {
        return new CompleteCheckoutCartAction();
    }

    @Bean
    AbandonCartAction cartAbandonAction() {
        return new AbandonCartAction();
    }

    @Bean
    ExpireCartAction cartExpireAction() {
        return new ExpireCartAction();
    }

    @Bean
    CancelCheckoutCartAction cartCancelCheckoutAction() {
        return new CancelCheckoutCartAction();
    }

    @Bean
    ReactivateCartAction cartReactivateAction() {
        return new ReactivateCartAction();
    }

    @Bean
    MergeCartAction cartMergeAction() {
        return new MergeCartAction();
    }

    @Bean
    MarkMergedCartAction cartMarkMergedAction() {
        return new MarkMergedCartAction();
    }

    @Bean
    FlagUnavailableCartAction cartFlagUnavailableAction() {
        return new FlagUnavailableCartAction();
    }

    @Bean
    RefreshPricingCartAction cartRefreshPricingAction() {
        return new RefreshPricingCartAction();
    }

    @Bean
    ExpireCouponCartAction cartExpireCouponAction() {
        return new ExpireCouponCartAction();
    }

    // ═══════════════════════════════════════════════════════════════════
    // Policy validator
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    CartPolicyValidator cartPolicyValidator(ConfigPort configPort) {
        return new CartPolicyValidator(configPort);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Post-Save Hooks (convention: cart + STATE_ID + PostSaveHook)
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    ACTIVECartPostSaveHook cartACTIVEPostSaveHook() {
        return new ACTIVECartPostSaveHook();
    }

    @Bean
    CHECKOUT_INITIATEDCartPostSaveHook cartCHECKOUT_INITIATEDPostSaveHook(CartEventPublisherPort eventPublisher) {
        return new CHECKOUT_INITIATEDCartPostSaveHook(eventPublisher);
    }

    @Bean
    CHECKOUT_COMPLETEDCartPostSaveHook cartCHECKOUT_COMPLETEDPostSaveHook(CartEventPublisherPort eventPublisher) {
        return new CHECKOUT_COMPLETEDCartPostSaveHook(eventPublisher);
    }

    @Bean
    ABANDONEDCartPostSaveHook cartABANDONEDPostSaveHook(CartEventPublisherPort eventPublisher) {
        return new ABANDONEDCartPostSaveHook(eventPublisher);
    }

    @Bean
    EXPIREDCartPostSaveHook cartEXPIREDPostSaveHook(CartEventPublisherPort eventPublisher) {
        return new EXPIREDCartPostSaveHook(eventPublisher);
    }

    @Bean
    MERGEDCartPostSaveHook cartMERGEDPostSaveHook(CartEventPublisherPort eventPublisher) {
        return new MERGEDCartPostSaveHook(eventPublisher);
    }

    // ═══════════════════════════════════════════════════════════════════
    // ACL: STM Authorities Builder
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    Function<ChenileExchange, String[]> cartEventAuthoritiesSupplier(
            @Qualifier("cartActionsInfoProvider") STMActionsInfoProvider cartInfoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(cartInfoProvider, false);
        return builder.build();
    }

}
