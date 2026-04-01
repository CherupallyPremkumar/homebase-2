package com.homebase.ecom.disputes.configuration;

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
import com.homebase.ecom.disputes.model.Dispute;
import com.homebase.ecom.disputes.service.cmds.*;
import com.homebase.ecom.disputes.service.healthcheck.DisputesHealthChecker;
import com.homebase.ecom.disputes.infrastructure.persistence.ChenileDisputesEntityStore;
import com.homebase.ecom.disputes.infrastructure.persistence.adapter.DisputesJpaRepository;
import com.homebase.ecom.disputes.infrastructure.persistence.mapper.DisputesMapper;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.stm.State;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import com.homebase.ecom.disputes.service.postSaveHooks.*;
import com.homebase.ecom.disputes.service.validator.DisputePolicyValidator;

@Configuration
public class DisputesConfiguration {
    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/disputes/disputes-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Disputes";
    public static final String PREFIX_FOR_RESOLVER = "disputes";

    @Bean BeanFactoryAdapter disputesBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean STMFlowStoreImpl disputesFlowStore(
            @Qualifier("disputesBeanFactoryAdapter") BeanFactoryAdapter disputesBeanFactoryAdapter
            ) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(disputesBeanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean STM<Dispute> disputesEntityStm(@Qualifier("disputesFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<Dispute> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean STMActionsInfoProvider disputesActionsInfoProvider(@Qualifier("disputesFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("disputes", provider);
        return provider;
    }

    @Bean DisputesMapper disputesMapper() {
        return new DisputesMapper();
    }

    @Bean EntityStore<Dispute> disputesEntityStore(DisputesJpaRepository jpaRepository, DisputesMapper mapper) {
        return new ChenileDisputesEntityStore(jpaRepository, mapper);
    }

    @Bean StateEntityServiceImpl<Dispute> _disputesStateEntityService_(
            @Qualifier("disputesEntityStm") STM<Dispute> stm,
            @Qualifier("disputesActionsInfoProvider") STMActionsInfoProvider disputesInfoProvider,
            @Qualifier("disputesEntityStore") EntityStore<Dispute> entityStore) {
        return new HmStateEntityServiceImpl<>(stm, disputesInfoProvider, entityStore);
    }

    // OGNL scripting for auto-states
    @Bean OgnlScriptingStrategy disputesOgnlScriptingStrategy() {
        return new OgnlScriptingStrategy();
    }

    // PostSaveHook infrastructure
    @Bean DefaultPostSaveHook<Dispute> disputesDefaultPostSaveHook(
            @Qualifier("disputesTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean GenericEntryAction<Dispute> disputesEntryAction(
            @Qualifier("disputesEntityStore") EntityStore<Dispute> entityStore,
            @Qualifier("disputesActionsInfoProvider") STMActionsInfoProvider disputesInfoProvider,
            @Qualifier("disputesFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("disputesDefaultPostSaveHook") DefaultPostSaveHook<Dispute> postSaveHook) {
        GenericEntryAction<Dispute> entryAction = new GenericEntryAction<>(entityStore, disputesInfoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean DefaultAutomaticStateComputation<Dispute> disputesDefaultAutoState(
            @Qualifier("disputesTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("disputesFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<Dispute> autoState = new DefaultAutomaticStateComputation<>(stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean GenericExitAction<Dispute> disputesExitAction(@Qualifier("disputesFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<Dispute> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean XmlFlowReader disputesFlowReader(@Qualifier("disputesFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean DisputesHealthChecker disputesHealthChecker() {
        return new DisputesHealthChecker();
    }

    @Bean STMTransitionAction<Dispute> defaultdisputesSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean STMTransitionActionResolver disputesTransitionActionResolver(
            @Qualifier("defaultdisputesSTMTransitionAction") STMTransitionAction<Dispute> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean StmBodyTypeSelector disputesBodyTypeSelector(
            @Qualifier("disputesActionsInfoProvider") STMActionsInfoProvider disputesInfoProvider,
            @Qualifier("disputesTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(disputesInfoProvider, stmTransitionActionResolver);
    }

    @Bean STMTransitionAction<Dispute> disputesBaseTransitionAction(
            @Qualifier("disputesTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("disputesActivityChecker") ActivityChecker activityChecker,
            @Qualifier("disputesFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<Dispute> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean ActivityChecker disputesActivityChecker(@Qualifier("disputesFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean AreActivitiesComplete disputesActivitiesCompletionCheck(@Qualifier("disputesActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    // ================================================================
    // STM Transition Actions (convention: "disputes" + eventId + "Action")
    // ================================================================

    @Bean ReviewDisputesAction disputesReviewAction() {
        return new ReviewDisputesAction();
    }

    @Bean EscalateDisputesAction disputesEscalateAction() {
        return new EscalateDisputesAction();
    }

    @Bean ResolveDisputesAction disputesResolveAction() {
        return new ResolveDisputesAction();
    }

    @Bean AddNoteDisputesAction disputesAddNoteAction() {
        return new AddNoteDisputesAction();
    }

    // ================================================================
    // Config and Enablement
    // ================================================================

    @Bean ConfigProviderImpl disputesConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean ConfigBasedEnablementStrategy disputesConfigBasedEnablementStrategy(
            @Qualifier("disputesConfigProvider") ConfigProvider configProvider,
            @Qualifier("disputesFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider, PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // ================================================================
    // PostSaveHooks (convention: "disputes" + STATE + "PostSaveHook")
    // ================================================================

    @Bean UNDER_REVIEWDisputesPostSaveHook disputesUNDER_REVIEWPostSaveHook() {
        return new UNDER_REVIEWDisputesPostSaveHook();
    }

    @Bean ESCALATEDDisputesPostSaveHook disputesESCALATEDPostSaveHook() {
        return new ESCALATEDDisputesPostSaveHook();
    }

    @Bean RESOLVEDDisputesPostSaveHook disputesRESOLVEDPostSaveHook() {
        return new RESOLVEDDisputesPostSaveHook();
    }

    // ================================================================
    // Security: STM ACL authorities builder
    // ================================================================

    @Bean java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> disputesEventAuthoritiesSupplier(
            @Qualifier("disputesActionsInfoProvider") STMActionsInfoProvider disputesInfoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(disputesInfoProvider, false);
        return builder.build();
    }

    // ================================================================
    // Policy Validator
    // ================================================================

    @Bean DisputePolicyValidator disputePolicyValidator() {
        return new DisputePolicyValidator();
    }
}
