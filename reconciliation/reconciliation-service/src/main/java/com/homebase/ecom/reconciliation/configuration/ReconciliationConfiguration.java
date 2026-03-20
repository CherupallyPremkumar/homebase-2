package com.homebase.ecom.reconciliation.configuration;

import com.homebase.ecom.reconciliation.model.ReconciliationBatch;
import com.homebase.ecom.reconciliation.service.cmds.*;
import com.homebase.ecom.reconciliation.service.healthcheck.ReconciliationHealthChecker;
import com.homebase.ecom.reconciliation.infrastructure.persistence.ChenileReconciliationBatchEntityStore;
import com.homebase.ecom.reconciliation.infrastructure.persistence.adapter.ReconciliationBatchJpaRepository;
import com.homebase.ecom.reconciliation.infrastructure.persistence.adapter.ReconciliationBatchRepositoryImpl;
import com.homebase.ecom.reconciliation.infrastructure.persistence.mapper.ReconciliationBatchMapper;
import com.homebase.ecom.reconciliation.port.ReconciliationBatchRepository;

import org.chenile.owiz.OrchExecutor;
import org.chenile.owiz.config.impl.XmlOrchConfigurator;
import org.chenile.owiz.impl.OrchExecutorImpl;
import org.chenile.owiz.impl.OwizSpringFactoryAdapter;

import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.action.scriptsupport.IfAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.ognl.OgnlScriptingStrategy;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import org.chenile.workflow.service.impl.HmStateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Reconciliation module Spring configuration.
 * Wires both:
 *   1) STM — ReconciliationBatch entity lifecycle (CREATED -> PROCESSING -> CHECK_MISMATCHES -> ...)
 *   2) OWIZ — Stateless reconciliation processing pipeline (fetch -> match -> flag -> report)
 */
@Configuration
public class ReconciliationConfiguration {

    // ═══════════════════════════════════════════════════════════════════════
    // OWIZ Pipeline (stateless reconciliation processing chain)
    // ═══════════════════════════════════════════════════════════════════════

    private static final String OWIZ_FLOW_DEFINITION_FILE =
            "com/homebase/ecom/reconciliation/reconciliation-flow.xml";

    @Bean
    OwizSpringFactoryAdapter reconciliationOwizBeanFactory() {
        return new OwizSpringFactoryAdapter();
    }

    @Bean
    XmlOrchConfigurator<ReconciliationContext> reconciliationOrchConfigurator(
            OwizSpringFactoryAdapter reconciliationOwizBeanFactory) throws Exception {
        XmlOrchConfigurator<ReconciliationContext> configurator = new XmlOrchConfigurator<>();
        configurator.setBeanFactoryAdapter(reconciliationOwizBeanFactory);
        configurator.setFilename(OWIZ_FLOW_DEFINITION_FILE);
        return configurator;
    }

    @Bean
    OrchExecutor<ReconciliationContext> reconciliationOrchExecutor(
            XmlOrchConfigurator<ReconciliationContext> reconciliationOrchConfigurator) {
        OrchExecutorImpl<ReconciliationContext> executor = new OrchExecutorImpl<>();
        executor.setOrchConfigurator(reconciliationOrchConfigurator);
        return executor;
    }

    // OWIZ chain command beans — names match componentName in reconciliation-flow.xml
    @Bean FetchGatewayTransactions fetchGatewayTransactionsCommand() { return new FetchGatewayTransactions(); }
    @Bean FetchSystemTransactions fetchSystemTransactionsCommand() { return new FetchSystemTransactions(); }
    @Bean MatchTransactions matchTransactionsCommand() { return new MatchTransactions(); }
    @Bean FlagMismatches flagMismatchesCommand() { return new FlagMismatches(); }
    @Bean AutoResolveCommand autoResolveCommand() { return new AutoResolveCommand(); }
    @Bean GenerateReconciliationReport generateReconciliationReportCommand() { return new GenerateReconciliationReport(); }

    // Placeholder commands for OWIZ steps 3 and 5 (detectDuplicates, applyToleranceThreshold)
    @Bean org.chenile.owiz.Command<ReconciliationContext> detectDuplicatesCommand() {
        return context -> { /* no-op placeholder */ };
    }
    @Bean org.chenile.owiz.Command<ReconciliationContext> applyToleranceThresholdCommand() {
        return context -> { /* no-op placeholder */ };
    }

    // ═══════════════════════════════════════════════════════════════════════
    // STM — ReconciliationBatch Lifecycle
    // ═══════════════════════════════════════════════════════════════════════

    private static final String STM_FLOW_DEFINITION_FILE =
            "com/homebase/ecom/reconciliation/reconciliation-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Reconciliation";
    public static final String PREFIX_FOR_RESOLVER = "reconciliation";

    // 1. Mapper
    @Bean
    ReconciliationBatchMapper reconciliationBatchMapper() {
        return new ReconciliationBatchMapper();
    }

    // 2. Repository adapter
    @Bean
    ReconciliationBatchRepository reconciliationBatchRepository(
            ReconciliationBatchJpaRepository jpaRepository, ReconciliationBatchMapper mapper) {
        return new ReconciliationBatchRepositoryImpl(jpaRepository, mapper);
    }

    // 3. Entity store
    @Bean
    EntityStore<ReconciliationBatch> reconciliationEntityStore(
            ReconciliationBatchJpaRepository jpaRepository, ReconciliationBatchMapper mapper) {
        return new ChenileReconciliationBatchEntityStore(jpaRepository, mapper);
    }

    // 4. Bean factory adapter
    @Bean
    BeanFactoryAdapter reconciliationBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    // 5. Flow store (loads STM XML)
    @Bean
    STMFlowStoreImpl reconciliationFlowStore(
            @Qualifier("reconciliationBeanFactoryAdapter") BeanFactoryAdapter reconciliationBeanFactoryAdapter)
            throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(reconciliationBeanFactoryAdapter);
        return stmFlowStore;
    }

    // 6. STM engine
    @Bean
    STM<ReconciliationBatch> reconciliationEntityStm(
            @Qualifier("reconciliationFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<ReconciliationBatch> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    // 7. Actions info provider
    @Bean
    STMActionsInfoProvider reconciliationActionsInfoProvider(
            @Qualifier("reconciliationFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("reconciliation", provider);
        return provider;
    }

    // 8. Transition action resolver
    @Bean
    STMTransitionAction<ReconciliationBatch> defaultreconciliationSTMTransitionAction() {
        return new DefaultReconciliationSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver reconciliationTransitionActionResolver(
            @Qualifier("defaultreconciliationSTMTransitionAction") STMTransitionAction<ReconciliationBatch> defaultAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultAction, true);
    }

    // 9. Post-save hook
    @Bean
    DefaultPostSaveHook<ReconciliationBatch> reconciliationDefaultPostSaveHook(
            @Qualifier("reconciliationTransitionActionResolver") STMTransitionActionResolver resolver) {
        return new DefaultPostSaveHook<>(resolver);
    }

    // 10. Entry/exit actions
    @Bean
    GenericEntryAction<ReconciliationBatch> reconciliationEntryAction(
            @Qualifier("reconciliationEntityStore") EntityStore<ReconciliationBatch> entityStore,
            @Qualifier("reconciliationActionsInfoProvider") STMActionsInfoProvider infoProvider,
            @Qualifier("reconciliationFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("reconciliationDefaultPostSaveHook") DefaultPostSaveHook<ReconciliationBatch> postSaveHook) {
        GenericEntryAction<ReconciliationBatch> entryAction = new GenericEntryAction<>(entityStore, infoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    GenericExitAction<ReconciliationBatch> reconciliationExitAction(
            @Qualifier("reconciliationFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<ReconciliationBatch> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    DefaultAutomaticStateComputation<ReconciliationBatch> reconciliationDefaultAutoState(
            @Qualifier("reconciliationTransitionActionResolver") STMTransitionActionResolver resolver,
            @Qualifier("reconciliationFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<ReconciliationBatch> autoState = new DefaultAutomaticStateComputation<>(resolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    // 11. Flow reader (parses STM XML)
    @Bean
    XmlFlowReader reconciliationFlowReader(
            @Qualifier("reconciliationFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(STM_FLOW_DEFINITION_FILE);
        return flowReader;
    }

    // 12. Base transition action
    @Bean
    STMTransitionAction<ReconciliationBatch> reconciliationBaseTransitionAction(
            @Qualifier("reconciliationTransitionActionResolver") STMTransitionActionResolver resolver,
            @Qualifier("reconciliationActivityChecker") ActivityChecker activityChecker,
            @Qualifier("reconciliationFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<ReconciliationBatch> baseTransitionAction = new BaseTransitionAction<>(resolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean
    ActivityChecker reconciliationActivityChecker(
            @Qualifier("reconciliationFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete reconciliationActivitiesCompletionCheck(
            @Qualifier("reconciliationActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    @Bean
    ConfigProviderImpl reconciliationConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    ConfigBasedEnablementStrategy reconciliationConfigBasedEnablementStrategy(
            @Qualifier("reconciliationConfigProvider") ConfigProvider configProvider,
            @Qualifier("reconciliationFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider, PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // 13. State entity service (main service bean)
    @Bean
    HmStateEntityServiceImpl<ReconciliationBatch> _reconciliationStateEntityService_(
            @Qualifier("reconciliationEntityStm") STM<ReconciliationBatch> stm,
            @Qualifier("reconciliationActionsInfoProvider") STMActionsInfoProvider infoProvider,
            @Qualifier("reconciliationEntityStore") EntityStore<ReconciliationBatch> entityStore) {
        return new HmStateEntityServiceImpl<>(stm, infoProvider, entityStore);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // OGNL + ifAction
    // ═══════════════════════════════════════════════════════════════════════

    @Bean
    OgnlScriptingStrategy ognlScriptingStrategy() {
        return new OgnlScriptingStrategy();
    }

    @Bean
    IfAction<ReconciliationBatch> ifAction() {
        return new IfAction<>();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // STM Transition Actions (named: reconciliation{EventId}Action)
    // ═══════════════════════════════════════════════════════════════════════

    @Bean
    StartProcessingAction reconciliationStartProcessingAction() {
        return new StartProcessingAction();
    }

    @Bean
    CompleteMatchingAction reconciliationCompleteMatchingAction() {
        return new CompleteMatchingAction();
    }

    @Bean
    ResolveMismatchesAction reconciliationResolveMismatchesAction() {
        return new ResolveMismatchesAction();
    }

    @Bean
    ApproveReconciliationAction reconciliationApproveAction() {
        return new ApproveReconciliationAction();
    }

    @Bean
    RejectReconciliationAction reconciliationRejectAction() {
        return new RejectReconciliationAction();
    }

    @Bean
    RetryReconciliationAction reconciliationRetryAction() {
        return new RetryReconciliationAction();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // ACL / Security
    // ═══════════════════════════════════════════════════════════════════════

    @Bean
    java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> reconciliationEventAuthoritiesSupplier(
            @Qualifier("reconciliationActionsInfoProvider") STMActionsInfoProvider infoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(infoProvider, false);
        return builder.build();
    }

    @Bean
    StmBodyTypeSelector reconciliationBodyTypeSelector(
            @Qualifier("reconciliationActionsInfoProvider") STMActionsInfoProvider infoProvider,
            @Qualifier("reconciliationTransitionActionResolver") STMTransitionActionResolver resolver) {
        return new StmBodyTypeSelector(infoProvider, resolver);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Health Checker
    // ═══════════════════════════════════════════════════════════════════════

    @Bean
    ReconciliationHealthChecker reconciliationHealthChecker() {
        return new ReconciliationHealthChecker();
    }
}
