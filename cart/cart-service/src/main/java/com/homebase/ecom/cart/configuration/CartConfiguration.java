package com.homebase.ecom.cart.configuration;

import com.homebase.ecom.cart.service.validator.CartPolicyValidator;
import com.homebase.ecom.shared.CurrencyResolver;
import org.chenile.cconfig.sdk.CconfigClient;
import org.chenile.proxy.builder.ProxyBuilder;
import org.chenile.service.registry.service.ServiceRegistryService;
import org.chenile.service.registry.service.impl.ServiceRegistryServiceImpl;
import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.service.cmds.*;
import com.homebase.ecom.cart.service.healthcheck.CartHealthChecker;
import com.homebase.ecom.cart.infrastructure.persistence.ChenileCartEntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.stm.State;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import com.homebase.ecom.cart.service.postSaveHooks.*;

/**
 * This is where you will instantiate all the required classes in Spring
 */
@Configuration
public class CartConfiguration {

    @Autowired
    private ProxyBuilder proxyBuilder;
    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/cart/cart-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Cart";
    public static final String PREFIX_FOR_RESOLVER = "cart";

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
    EntityStore<Cart> cartEntityStore() {
        return new ChenileCartEntityStore();
    }

    @Bean
    StateEntityServiceImpl<Cart> _cartStateEntityService_(
            @Qualifier("cartEntityStm") STM<Cart> stm,
            @Qualifier("cartActionsInfoProvider") STMActionsInfoProvider cartInfoProvider,
            @Qualifier("cartEntityStore") EntityStore<Cart> entityStore) {
        return new StateEntityServiceImpl<>(stm, cartInfoProvider, entityStore);
    }

    // Now we start constructing the STM Components

    @Bean
    DefaultPostSaveHook<Cart> cartDefaultPostSaveHook(
            @Qualifier("cartTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        DefaultPostSaveHook<Cart> postSaveHook = new DefaultPostSaveHook<>(stmTransitionActionResolver);
        return postSaveHook;
    }

    @Bean
    GenericEntryAction<Cart> cartEntryAction(@Qualifier("cartEntityStore") EntityStore<Cart> entityStore,
            @Qualifier("cartActionsInfoProvider") STMActionsInfoProvider cartInfoProvider,
            @Qualifier("cartFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("cartDefaultPostSaveHook") DefaultPostSaveHook<Cart> postSaveHook) {
        GenericEntryAction<Cart> entryAction = new GenericEntryAction<Cart>(entityStore, cartInfoProvider,
                postSaveHook);
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
        GenericExitAction<Cart> exitAction = new GenericExitAction<Cart>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    XmlFlowReader cartFlowReader(@Qualifier("cartFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean
    CartHealthChecker cartHealthChecker() {
        return new CartHealthChecker();
    }

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

    // Create the specific transition actions here. Make sure that these actions are
    // inheriting from
    // AbstractSTMTransitionMachine (The sample classes provide an example of this).
    // To automatically wire
    // them into the STM use the convention of "cart" + eventId + "Action" for the
    // method name. (cart is the
    // prefix passed to the TransitionActionResolver above.)
    // This will ensure that these are detected automatically by the Workflow
    // system.
    // The payload types will be detected as well so that there is no need to
    // introduce an <event-information/>
    // segment in src/main/resources/com/homebase/cart/cart-states.xml

    @Bean
    AddItemCartAction cartAddItemAction() {
        return new AddItemCartAction();
    }

    @Bean
    CartPolicyValidator cartPolicyValidator(CconfigClient cconfigClient, CurrencyResolver currencyResolver) {
        return new CartPolicyValidator(cconfigClient, currencyResolver);
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
    CompletePaymentCartAction cartCompletePaymentAction() {
        return new CompletePaymentCartAction();
    }

    @Bean
    AbandonCheckoutCartAction cartAbandonCheckoutAction() {
        return new AbandonCheckoutCartAction();
    }

    @Bean
    InitiateCheckoutCartAction cartInitiateCheckoutAction() {
        return new InitiateCheckoutCartAction();
    }

    @Bean
    AddDeliveryAddressAction cartAddDeliveryAddressAction() {
        return new AddDeliveryAddressAction();
    }

    @Bean
    CreatePaymentSessionAction cartCreatePaymentSessionAction() {
        return new CreatePaymentSessionAction();
    }

    @Bean
    PaymentSuccessCartAction cartPaymentSuccessAction() {
        return new PaymentSuccessCartAction();
    }

    @Bean
    PaymentFailedCartAction cartPaymentFailedAction() {
        return new PaymentFailedCartAction();
    }

    @Bean
    RevertToActiveCartAction cartRevertToActiveAction() {
        return new RevertToActiveCartAction();
    }

    @Bean
    AbandonCartCartAction cartAbandonCartAction() {
        return new AbandonCartCartAction();
    }

    @Bean
    OrderCreatedFromCartAction cartOrderCreatedFromCartAction() {
        return new OrderCreatedFromCartAction();
    }

    @Bean
    ClearCartAction cartClearCartAction() {
        return new ClearCartAction();
    }

    @Bean
    MergeCartAction cartMergeCartAction() {
        return new MergeCartAction();
    }

    @Bean
    ReconciliationPendingCartAction cartReconciliationPendingAction() {
        return new ReconciliationPendingCartAction();
    }

    @Bean
    ExpiredCartAction cartExpiredAction() {
        return new ExpiredCartAction();
    }

    @Bean
    ApplyPromoCodeAction cartApplyPromoCodeAction() {
        return new ApplyPromoCodeAction();
    }

    @Bean
    MoveToWishlistAction cartMoveToWishlistAction() {
        return new MoveToWishlistAction();
    }

    @Bean
    SessionTimeoutAction cartSessionTimeoutAction() {
        return new SessionTimeoutAction();
    }

    @Bean
    MoveToCartAction cartMoveToCartAction() {
        return new MoveToCartAction();
    }

    @Bean
    LockTimeoutAction cartLockTimeoutAction() {
        return new LockTimeoutAction();
    }

    @Bean
    InventoryReservationFailedAction cartInventoryReservationFailedAction() {
        return new InventoryReservationFailedAction();
    }

    @Bean
    ReserveTimeoutAction cartReserveTimeoutAction() {
        return new ReserveTimeoutAction();
    }

    @Bean
    RecoverCartAction cartRecoverCartAction() {
        return new RecoverCartAction();
    }

    @Bean
    InventoryReserveAction inventoryReserveAction() {
        return new InventoryReserveAction();
    }

    @Bean
    CartInventoryReleaseAction cartInventoryReleaseAction() {
        return new CartInventoryReleaseAction();
    }

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

    @Bean
    CHECKOUT_IN_PROGRESSCartPostSaveHook cartCHECKOUT_IN_PROGRESSPostSaveHook() {
        return new CHECKOUT_IN_PROGRESSCartPostSaveHook();
    }

    @Bean
    ORDER_CREATEDCartPostSaveHook cartORDER_CREATEDPostSaveHook() {
        return new ORDER_CREATEDCartPostSaveHook();
    }

    @Bean
    CREATEDCartPostSaveHook cartCREATEDPostSaveHook() {
        return new CREATEDCartPostSaveHook();
    }

    @Bean
    ACTIVECartPostSaveHook cartACTIVEPostSaveHook() {
        return new ACTIVECartPostSaveHook();
    }

    @Bean
    ABANDONEDCartPostSaveHook cartABANDONEDPostSaveHook() {
        return new ABANDONEDCartPostSaveHook();
    }

    @Bean
    EXPIREDCartPostSaveHook cartEXPIREDPostSaveHook() {
        return new EXPIREDCartPostSaveHook();
    }

}
