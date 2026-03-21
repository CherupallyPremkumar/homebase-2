package com.homebase.ecom.product.configuration;

import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
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
import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.domain.port.*;
import com.homebase.ecom.product.infrastructure.persistence.adapter.*;
import com.homebase.ecom.product.infrastructure.persistence.mapper.*;
import com.homebase.ecom.product.api.TaxonomyService;
import com.homebase.ecom.product.configuration.controller.TaxonomyController;
import com.homebase.ecom.product.service.impl.TaxonomyServiceImpl;
import com.homebase.ecom.product.service.cmds.*;
import com.homebase.ecom.product.service.healthcheck.ProductHealthChecker;
import com.homebase.ecom.product.service.store.ProductEntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import com.homebase.ecom.product.service.postSaveHooks.*;
import com.homebase.ecom.product.infrastructure.adapter.PolicyDecisionAdapter;
import com.homebase.ecom.product.service.validator.ProductPolicyValidator;

/**
 * This is where you will instantiate all the required classes in Spring
 */
@Configuration
public class ProductConfiguration {
    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/product/product-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Product";
    public static final String PREFIX_FOR_RESOLVER = "product";

    @Bean
    BeanFactoryAdapter productBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl productFlowStore(
            @Qualifier("productBeanFactoryAdapter") BeanFactoryAdapter productBeanFactoryAdapter) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(productBeanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean
    STM<Product> productEntityStm(@Qualifier("productFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<Product> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider productActionsInfoProvider(@Qualifier("productFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("product", provider);
        return provider;
    }

    // Mappers
    @Bean ProductMapper productMapper() { return new ProductMapper(); }
    @Bean CategoryMapper categoryMapper() { return new CategoryMapper(); }
    @Bean AttributeMapper attributeMapper() { return new AttributeMapper(); }
    @Bean MediaMapper mediaMapper() { return new MediaMapper(); }
    @Bean VariantMapper variantMapper() { return new VariantMapper(); }

    // Repositories & Adapters
    @Bean ProductRepositoryImpl productRepository(ProductJpaRepository r, ProductMapper m) {
        return new ProductRepositoryImpl(r, m);
    }
    @Bean("productCategoryRepository")
    CategoryRepository productCategoryRepository(CategoryJpaRepository r, CategoryMapper m) {
        return new CategoryRepositoryImpl(r, m);
    }
    @Bean AttributeRepositoryImpl attributeRepository(AttributeJpaRepository r, AttributeMapper m) {
        return new AttributeRepositoryImpl(r, m);
    }
    @Bean MediaRepositoryImpl mediaRepository(MediaJpaRepository r, MediaMapper m) {
        return new MediaRepositoryImpl(r, m);
    }
    @Bean VariantRepositoryImpl variantRepository(VariantJpaRepository r, VariantMapper m) {
        return new VariantRepositoryImpl(r, m);
    }

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnBean(com.homebase.ecom.rulesengine.api.service.DecisionService.class)
    public PimPolicyPort pimPolicyPort(com.homebase.ecom.rulesengine.api.service.DecisionService decisionService) {
        return new PolicyDecisionAdapter(decisionService);
    }

    @Bean
    EntityStore<Product> productEntityStore(@Qualifier("productRepository") ProductRepository repository) {
        return new ProductEntityStore(repository);
    }

    @Bean
    StateEntityServiceImpl<Product> _productStateEntityService_(
            @Qualifier("productEntityStm") STM<Product> stm,
            @Qualifier("productActionsInfoProvider") STMActionsInfoProvider productInfoProvider,
            @Qualifier("productEntityStore") EntityStore<Product> entityStore) {
        return new HmStateEntityServiceImpl<>(stm, productInfoProvider, entityStore);
    }

    // Now we start constructing the STM Components

    @Bean
    DefaultPostSaveHook<Product> productDefaultPostSaveHook(
            @Qualifier("productTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        DefaultPostSaveHook<Product> postSaveHook = new DefaultPostSaveHook<>(stmTransitionActionResolver);
        return postSaveHook;
    }

    @Bean
    GenericEntryAction<Product> productEntryAction(@Qualifier("productEntityStore") EntityStore<Product> entityStore,
            @Qualifier("productActionsInfoProvider") STMActionsInfoProvider productInfoProvider,
            @Qualifier("productFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("productDefaultPostSaveHook") DefaultPostSaveHook<Product> postSaveHook) {
        GenericEntryAction<Product> entryAction = new GenericEntryAction<Product>(entityStore, productInfoProvider,
                postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    DefaultAutomaticStateComputation<Product> productDefaultAutoState(
            @Qualifier("productTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("productFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<Product> autoState = new DefaultAutomaticStateComputation<>(
                stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    GenericExitAction<Product> productExitAction(@Qualifier("productFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<Product> exitAction = new GenericExitAction<Product>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    XmlFlowReader productFlowReader(@Qualifier("productFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean
    ProductHealthChecker productHealthChecker() {
        return new ProductHealthChecker();
    }

    @Bean
    STMTransitionAction<Product> defaultproductSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver productTransitionActionResolver(
            @Qualifier("defaultproductSTMTransitionAction") STMTransitionAction<Product> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean
    StmBodyTypeSelector productBodyTypeSelector(
            @Qualifier("productActionsInfoProvider") STMActionsInfoProvider productInfoProvider,
            @Qualifier("productTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(productInfoProvider, stmTransitionActionResolver);
    }

    @Bean
    STMTransitionAction<Product> productBaseTransitionAction(
            @Qualifier("productTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("productActivityChecker") ActivityChecker activityChecker,
            @Qualifier("productFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<Product> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean
    ActivityChecker productActivityChecker(@Qualifier("productFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete activitiesCompletionCheck(
            @Qualifier("productActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    // Create the specific transition actions here. Make sure that these actions are
    // inheriting from
    // AbstractSTMTransitionMachine (The sample classes provide an example of this).
    // To automatically wire
    // them into the STM use the convention of "product" + eventId + "Action" for
    // the method name. (product is the
    // prefix passed to the TransitionActionResolver above.)
    // This will ensure that these are detected automatically by the Workflow
    // system.
    // The payload types will be detected as well so that there is no need to
    // introduce an <event-information/>
    // segment in src/main/resources/com/homebase/product/product-states.xml

    @Bean
    RejectProductProductAction productRejectProductAction() {
        return new RejectProductProductAction();
    }

    @Bean
    DiscontinueProductProductAction productDiscontinueProductAction() {
        return new DiscontinueProductProductAction();
    }

    @Bean
    SubmitForReviewProductAction productSubmitForReviewAction() {
        return new SubmitForReviewProductAction();
    }

    @Bean
    DisableProductProductAction productDisableProductAction() {
        return new DisableProductProductAction();
    }

    @Bean
    EnableProductProductAction productEnableProductAction() {
        return new EnableProductProductAction();
    }

    @Bean
    DeleteProductProductAction productDeleteProductAction() {
        return new DeleteProductProductAction();
    }

    @Bean
    ApproveProductProductAction productApproveProductAction() {
        return new ApproveProductProductAction();
    }

    @Bean
    RequestUpdateProductAction productRequestUpdateAction() {
        return new RequestUpdateProductAction();
    }

    @Bean
    ApproveUpdateProductAction productApproveUpdateAction() {
        return new ApproveUpdateProductAction();
    }

    @Bean
    RejectUpdateProductAction productRejectUpdateAction() {
        return new RejectUpdateProductAction();
    }

    @Bean
    ArchiveProductProductAction productArchiveProductAction() {
        return new ArchiveProductProductAction();
    }

    @Bean
    UnarchiveProductProductAction productUnarchiveProductAction() {
        return new UnarchiveProductProductAction();
    }

    @Bean
    RecallProductProductAction productRecallProductAction() {
        return new RecallProductProductAction();
    }

    @Bean
    ResolveRecallProductAction productResolveRecallAction() {
        return new ResolveRecallProductAction();
    }

    @Bean("productEventService")
    com.homebase.ecom.product.service.event.ProductEventHandler productEventService(
            @Qualifier("_productStateEntityService_") StateEntityServiceImpl<Product> productStateEntityService,
            com.homebase.ecom.product.domain.port.ProductRepository productRepository,
            org.chenile.pubsub.ChenilePub chenilePub,
            tools.jackson.databind.ObjectMapper objectMapper) {
        return new com.homebase.ecom.product.service.event.ProductEventHandler(
                productStateEntityService, productRepository, chenilePub, objectMapper);
    }

    @Bean
    ConfigProviderImpl productConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    ConfigBasedEnablementStrategy productConfigBasedEnablementStrategy(
            @Qualifier("productConfigProvider") ConfigProvider configProvider,
            @Qualifier("productFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider,
                PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    @Bean
    DISABLEDProductPostSaveHook productDISABLEDPostSaveHook() {
        return new DISABLEDProductPostSaveHook();
    }

    @Bean
    PUBLISHEDProductPostSaveHook productPUBLISHEDPostSaveHook() {
        return new PUBLISHEDProductPostSaveHook();
    }

    @Bean
    DRAFTProductPostSaveHook productDRAFTPostSaveHook() {
        return new DRAFTProductPostSaveHook();
    }

    @Bean
    DISCONTINUEDProductPostSaveHook productDISCONTINUEDPostSaveHook() {
        return new DISCONTINUEDProductPostSaveHook();
    }

    @Bean
    UNDER_REVIEWProductPostSaveHook productUNDER_REVIEWPostSaveHook() {
        return new UNDER_REVIEWProductPostSaveHook();
    }

    @Bean
    PENDING_UPDATEProductPostSaveHook productPENDING_UPDATEPostSaveHook() {
        return new PENDING_UPDATEProductPostSaveHook();
    }

    @Bean
    ARCHIVEDProductPostSaveHook productARCHIVEDPostSaveHook() {
        return new ARCHIVEDProductPostSaveHook();
    }

    @Bean
    RECALLEDProductPostSaveHook productRECALLEDPostSaveHook() {
        return new RECALLEDProductPostSaveHook();
    }

    @Bean
    public TaxonomyService taxonomyService(@Qualifier("productCategoryRepository") CategoryRepository categoryRepository, AttributeRepository attributeRepository) {
        return new TaxonomyServiceImpl(categoryRepository, attributeRepository);
    }

    @Bean
    public TaxonomyController taxonomyController(TaxonomyService taxonomyService) {
        return new TaxonomyController(taxonomyService);
    }

    @Bean
    java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> productEventAuthoritiesSupplier(
            @Qualifier("productActionsInfoProvider") STMActionsInfoProvider productInfoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(productInfoProvider, false);
        return builder.build();
    }

    @Bean
    ProductPolicyValidator productPolicyValidator() {
        return new ProductPolicyValidator();
    }
}
