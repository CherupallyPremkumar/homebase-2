package com.homebase.ecom.cart.configuration;

import org.chenile.core.context.ChenileExchange;
import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.chenile.workflow.service.impl.HmStateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.service.cmds.*;
import com.homebase.ecom.cart.service.impl.CartServiceImpl;
import com.homebase.ecom.cart.infrastructure.mapper.CartDtoMapper;
import com.homebase.ecom.cart.service.healthcheck.CartHealthChecker;
import com.homebase.ecom.cart.infrastructure.persistence.ChenileCartEntityStore;
import com.homebase.ecom.cart.infrastructure.persistence.repository.CartJpaRepository;
import com.homebase.ecom.cart.infrastructure.persistence.mapper.CartMapper;
import com.homebase.ecom.cart.infrastructure.adapter.InventoryCheckAdapter;
import com.homebase.ecom.cart.infrastructure.adapter.PricingAdapter;
import com.homebase.ecom.cart.infrastructure.adapter.ProductCheckAdapter;
import com.homebase.ecom.cart.domain.port.InventoryCheckPort;
import com.homebase.ecom.cart.domain.port.PricingPort;
import com.homebase.ecom.cart.domain.port.ProductCheckPort;
import com.homebase.ecom.pricing.api.service.PricingService;
import org.chenile.query.service.SearchService;
import com.homebase.ecom.cart.service.validator.CartPolicyValidator;
import com.homebase.ecom.cart.service.event.CartEventHandler;
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
    EntityStore<Cart> cartEntityStore(CartJpaRepository repository, CartMapper mapper) {
        return new ChenileCartEntityStore(repository, mapper);
    }

    @Bean
    CartServiceImpl _cartStateEntityService_(
            @Qualifier("cartEntityStm") STM<Cart> stm,
            @Qualifier("cartActionsInfoProvider") STMActionsInfoProvider cartInfoProvider,
            @Qualifier("cartEntityStore") EntityStore<Cart> entityStore,
            CartDtoMapper cartDtoMapper) {
        return new CartServiceImpl(stm, cartInfoProvider, entityStore, cartDtoMapper);
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

    // ═══════════════════════════════════════════════════════════════════
    // Policy validator
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    CartPolicyValidator cartPolicyValidator() {
        return new CartPolicyValidator();
    }

    // ═══════════════════════════════════════════════════════════════════
    // Hexagonal ports — infrastructure adapters
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    InventoryCheckPort inventoryCheckPort(SearchService searchService) {
        return new InventoryCheckAdapter(searchService);
    }

    @Bean
    @ConditionalOnBean(PricingService.class)
    PricingPort pricingPort(PricingService pricingServiceClient) {
        return new PricingAdapter(pricingServiceClient);
    }

    @Bean
    ProductCheckPort productCheckPort(SearchService searchService) {
        return new ProductCheckAdapter(searchService);
    }


    // ═══════════════════════════════════════════════════════════════════
    // Post-Save Hooks (convention: cart + STATE_ID + PostSaveHook)
    // ═══════════════════════════════════════════════════════════════════

    @Bean
    ACTIVECartPostSaveHook cartACTIVEPostSaveHook() {
        return new ACTIVECartPostSaveHook();
    }

    @Bean
    CHECKOUT_INITIATEDCartPostSaveHook cartCHECKOUT_INITIATEDPostSaveHook() {
        return new CHECKOUT_INITIATEDCartPostSaveHook();
    }

    @Bean
    CHECKOUT_COMPLETEDCartPostSaveHook cartCHECKOUT_COMPLETEDPostSaveHook() {
        return new CHECKOUT_COMPLETEDCartPostSaveHook();
    }

    @Bean
    ABANDONEDCartPostSaveHook cartABANDONEDPostSaveHook() {
        return new ABANDONEDCartPostSaveHook();
    }

    @Bean
    EXPIREDCartPostSaveHook cartEXPIREDPostSaveHook() {
        return new EXPIREDCartPostSaveHook();
    }

    @Bean
    MERGEDCartPostSaveHook cartMERGEDPostSaveHook() {
        return new MERGEDCartPostSaveHook();
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

    // ═══════════════════════════════════════════════════════════════════
    // Chenile-Kafka event handler
    // ═══════════════════════════════════════════════════════════════════

    @Bean("cartEventService")
    @ConditionalOnBean(org.chenile.pubsub.ChenilePub.class)
    CartEventHandler cartEventService(org.chenile.pubsub.ChenilePub chenilePub,
                                      tools.jackson.databind.ObjectMapper objectMapper) {
        return new CartEventHandler(chenilePub, objectMapper);
    }
}
