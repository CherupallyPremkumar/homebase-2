package com.homebase.ecom.onboarding.configuration;

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
import org.chenile.workflow.service.stmcmds.*;
import com.homebase.ecom.onboarding.model.OnboardingSaga;
import com.homebase.ecom.onboarding.infrastructure.persistence.ChenileOnboardingSagaEntityStore;
import com.homebase.ecom.onboarding.infrastructure.persistence.adapter.OnboardingSagaJpaRepository;
import com.homebase.ecom.onboarding.infrastructure.persistence.adapter.OnboardingSagaRepositoryImpl;
import com.homebase.ecom.onboarding.infrastructure.persistence.mapper.OnboardingSagaMapper;
import com.homebase.ecom.onboarding.port.OnboardingSagaRepository;
import com.homebase.ecom.onboarding.service.cmds.*;
import com.homebase.ecom.onboarding.service.healthcheck.OnboardingHealthChecker;
import com.homebase.ecom.onboarding.service.postSaveHooks.*;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;

/**
 * Full STM+OWIZ configuration for the Onboarding module.
 * Replaces the previous pure OWIZ configuration.
 */
@Configuration
public class OnboardingConfiguration {
    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/onboarding/onboarding-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Onboarding";
    public static final String PREFIX_FOR_RESOLVER = "onboarding";

    // ========== Core STM Beans ==========

    @Bean
    BeanFactoryAdapter onboardingBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl onboardingFlowStore(
            @Qualifier("onboardingBeanFactoryAdapter") BeanFactoryAdapter onboardingBeanFactoryAdapter) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(onboardingBeanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean
    STM<OnboardingSaga> onboardingEntityStm(@Qualifier("onboardingFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<OnboardingSaga> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider onboardingActionsInfoProvider(
            @Qualifier("onboardingFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("onboarding", provider);
        return provider;
    }

    // ========== Infrastructure Beans ==========

    @Bean
    OnboardingSagaMapper onboardingSagaMapper() {
        return new OnboardingSagaMapper();
    }

    @Bean
    OnboardingSagaRepositoryImpl onboardingSagaRepository(OnboardingSagaJpaRepository jpaRepository, OnboardingSagaMapper mapper) {
        return new OnboardingSagaRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    EntityStore<OnboardingSaga> onboardingEntityStore(OnboardingSagaJpaRepository repository, OnboardingSagaMapper mapper) {
        return new ChenileOnboardingSagaEntityStore(repository, mapper);
    }

    // ========== State Entity Service ==========

    @Bean
    StateEntityServiceImpl<OnboardingSaga> _onboardingStateEntityService_(
            @Qualifier("onboardingEntityStm") STM<OnboardingSaga> stm,
            @Qualifier("onboardingActionsInfoProvider") STMActionsInfoProvider onboardingInfoProvider,
            @Qualifier("onboardingEntityStore") EntityStore<OnboardingSaga> entityStore) {
        return new StateEntityServiceImpl<>(stm, onboardingInfoProvider, entityStore);
    }

    // ========== STM Components ==========

    @Bean
    DefaultPostSaveHook<OnboardingSaga> onboardingDefaultPostSaveHook(
            @Qualifier("onboardingTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean
    GenericEntryAction<OnboardingSaga> onboardingEntryAction(
            @Qualifier("onboardingEntityStore") EntityStore<OnboardingSaga> entityStore,
            @Qualifier("onboardingActionsInfoProvider") STMActionsInfoProvider onboardingInfoProvider,
            @Qualifier("onboardingFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("onboardingDefaultPostSaveHook") DefaultPostSaveHook<OnboardingSaga> postSaveHook) {
        GenericEntryAction<OnboardingSaga> entryAction = new GenericEntryAction<>(entityStore, onboardingInfoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    DefaultAutomaticStateComputation<OnboardingSaga> onboardingDefaultAutoState(
            @Qualifier("onboardingTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("onboardingFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<OnboardingSaga> autoState = new DefaultAutomaticStateComputation<>(stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    GenericExitAction<OnboardingSaga> onboardingExitAction(@Qualifier("onboardingFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<OnboardingSaga> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    XmlFlowReader onboardingFlowReader(@Qualifier("onboardingFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean
    OnboardingHealthChecker onboardingHealthChecker() {
        return new OnboardingHealthChecker();
    }

    @Bean
    STMTransitionAction<OnboardingSaga> defaultonboardingSTMTransitionAction() {
        return new DefaultOnboardingSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver onboardingTransitionActionResolver(
            @Qualifier("defaultonboardingSTMTransitionAction") STMTransitionAction<OnboardingSaga> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean
    StmBodyTypeSelector onboardingBodyTypeSelector(
            @Qualifier("onboardingActionsInfoProvider") STMActionsInfoProvider onboardingInfoProvider,
            @Qualifier("onboardingTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(onboardingInfoProvider, stmTransitionActionResolver);
    }

    @Bean
    STMTransitionAction<OnboardingSaga> onboardingBaseTransitionAction(
            @Qualifier("onboardingTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("onboardingActivityChecker") ActivityChecker activityChecker,
            @Qualifier("onboardingFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<OnboardingSaga> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean
    ActivityChecker onboardingActivityChecker(@Qualifier("onboardingFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete onboardingActivitiesCompletionCheck(
            @Qualifier("onboardingActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    @Bean
    ConfigProviderImpl onboardingConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    ConfigBasedEnablementStrategy onboardingConfigBasedEnablementStrategy(
            @Qualifier("onboardingConfigProvider") ConfigProvider configProvider,
            @Qualifier("onboardingFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider, PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // ========== Transition Actions ==========
    // Convention: "onboarding" + eventId + "Action" for automatic STM wiring

    @Bean
    ValidateDocumentsAction onboardingValidateDocumentsAction() {
        return new ValidateDocumentsAction();
    }

    @Bean
    RejectApplicationAction onboardingRejectApplicationAction() {
        return new RejectApplicationAction();
    }

    @Bean
    SubmitForReviewAction onboardingSubmitForReviewAction() {
        return new SubmitForReviewAction();
    }

    @Bean
    ApproveAction onboardingApproveAction() {
        return new ApproveAction();
    }

    @Bean
    RejectAction onboardingRejectAction() {
        return new RejectAction();
    }

    @Bean
    RequestMoreInfoAction onboardingRequestMoreInfoAction() {
        return new RequestMoreInfoAction();
    }

    @Bean
    SubmitDocumentsAction onboardingSubmitDocumentsAction() {
        return new SubmitDocumentsAction();
    }

    @Bean
    CreateSupplierAction onboardingCreateSupplierAction() {
        return new CreateSupplierAction();
    }

    @Bean
    NotifyDadAction onboardingNotifyDadAction() {
        return new NotifyDadAction();
    }

    @Bean
    NotifySupplierAction onboardingNotifySupplierAction() {
        return new NotifySupplierAction();
    }

    @Bean
    ResubmitAction onboardingResubmitAction() {
        return new ResubmitAction();
    }

    // ========== Post-Save Hooks ==========

    @Bean
    APPLICATION_SUBMITTEDOnboardingSagaPostSaveHook onboardingAPPLICATION_SUBMITTEDPostSaveHook() {
        return new APPLICATION_SUBMITTEDOnboardingSagaPostSaveHook();
    }

    @Bean
    DOCUMENTS_VERIFIEDOnboardingSagaPostSaveHook onboardingDOCUMENTS_VERIFIEDPostSaveHook() {
        return new DOCUMENTS_VERIFIEDOnboardingSagaPostSaveHook();
    }

    @Bean
    UNDER_REVIEWOnboardingSagaPostSaveHook onboardingUNDER_REVIEWPostSaveHook() {
        return new UNDER_REVIEWOnboardingSagaPostSaveHook();
    }

    @Bean
    PENDING_DOCUMENTSOnboardingSagaPostSaveHook onboardingPENDING_DOCUMENTSPostSaveHook() {
        return new PENDING_DOCUMENTSOnboardingSagaPostSaveHook();
    }

    @Bean
    APPROVEDOnboardingSagaPostSaveHook onboardingAPPROVEDPostSaveHook() {
        return new APPROVEDOnboardingSagaPostSaveHook();
    }

    @Bean
    SUPPLIER_CREATEDOnboardingSagaPostSaveHook onboardingSUPPLIER_CREATEDPostSaveHook() {
        return new SUPPLIER_CREATEDOnboardingSagaPostSaveHook();
    }

    @Bean
    ACTIVEOnboardingSagaPostSaveHook onboardingACTIVEPostSaveHook() {
        return new ACTIVEOnboardingSagaPostSaveHook();
    }

    @Bean
    REJECTEDOnboardingSagaPostSaveHook onboardingREJECTEDPostSaveHook() {
        return new REJECTEDOnboardingSagaPostSaveHook();
    }
}
