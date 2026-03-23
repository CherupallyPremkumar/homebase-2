package com.homebase.ecom.onboarding.configuration;

import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.stm.action.scriptsupport.IfAction;
import org.chenile.stm.ognl.OgnlScriptingStrategy;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import com.homebase.ecom.core.workflow.HmStateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import com.homebase.ecom.onboarding.model.OnboardingSaga;
import com.homebase.ecom.onboarding.infrastructure.persistence.ChenileOnboardingSagaEntityStore;
import com.homebase.ecom.onboarding.infrastructure.persistence.adapter.OnboardingSagaJpaRepository;
import com.homebase.ecom.onboarding.infrastructure.persistence.adapter.OnboardingSagaRepositoryImpl;
import com.homebase.ecom.onboarding.infrastructure.persistence.mapper.OnboardingSagaMapper;
import com.homebase.ecom.onboarding.infrastructure.adapter.DocumentVerificationAdapter;
import com.homebase.ecom.onboarding.infrastructure.adapter.TrainingAdapter;
import com.homebase.ecom.onboarding.infrastructure.adapter.NotificationAdapter;
import com.homebase.ecom.onboarding.port.DocumentVerificationPort;
import com.homebase.ecom.onboarding.port.TrainingPort;
import com.homebase.ecom.onboarding.port.NotificationPort;
import com.homebase.ecom.onboarding.service.cmds.*;
import com.homebase.ecom.onboarding.service.event.OnboardingEventHandler;
import com.homebase.ecom.onboarding.service.healthcheck.OnboardingHealthChecker;
import com.homebase.ecom.onboarding.service.postSaveHooks.*;
import com.homebase.ecom.onboarding.service.validator.OnboardingPolicyValidator;

/**
 * Full STM+OWIZ configuration for the Onboarding module.
 * States: APPLICATION_SUBMITTED -> DOCUMENT_VERIFICATION -> BUSINESS_VERIFICATION -> TRAINING -> ONBOARDED -> COMPLETED
 * With REJECTED and DOCUMENTS_REQUESTED side states.
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

    // ========== Hexagonal Port Adapters (Item 12) ==========

    @Bean
    DocumentVerificationPort documentVerificationPort() {
        return new DocumentVerificationAdapter();
    }

    @Bean
    TrainingPort trainingPort() {
        return new TrainingAdapter();
    }

    @Bean
    NotificationPort onboardingNotificationPort() {
        return new NotificationAdapter();
    }

    // ========== State Entity Service ==========

    @Bean
    StateEntityServiceImpl<OnboardingSaga> _onboardingStateEntityService_(
            @Qualifier("onboardingEntityStm") STM<OnboardingSaga> stm,
            @Qualifier("onboardingActionsInfoProvider") STMActionsInfoProvider onboardingInfoProvider,
            @Qualifier("onboardingEntityStore") EntityStore<OnboardingSaga> entityStore) {
        return new HmStateEntityServiceImpl<>(stm, onboardingInfoProvider, entityStore);
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

    // ========== OGNL + Auto-state support ==========

    @Bean
    OgnlScriptingStrategy ognlScriptingStrategy() {
        return new OgnlScriptingStrategy();
    }

    @Bean
    IfAction<OnboardingSaga> ifAction() {
        return new IfAction<>();
    }

    // ========== Default + Resolver ==========

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

    // ========== Transition Actions (Item 4) ==========
    // Convention: "onboarding" + eventId + "Action"

    @Bean
    VerifyDocumentsAction onboardingVerifyDocumentsAction() {
        return new VerifyDocumentsAction();
    }

    @Bean
    RequestDocumentsAction onboardingRequestDocumentsAction() {
        return new RequestDocumentsAction();
    }

    @Bean
    ResubmitDocumentsAction onboardingResubmitDocumentsAction() {
        return new ResubmitDocumentsAction();
    }

    @Bean
    VerifyBusinessAction onboardingVerifyBusinessAction() {
        return new VerifyBusinessAction();
    }

    @Bean
    StartTrainingAction onboardingStartTrainingAction() {
        return new StartTrainingAction();
    }

    @Bean
    CompleteTrainingAction onboardingCompleteTrainingAction() {
        return new CompleteTrainingAction();
    }

    @Bean
    CompleteOnboardingAction onboardingCompleteOnboardingAction() {
        return new CompleteOnboardingAction();
    }

    @Bean
    RejectAction onboardingRejectAction() {
        return new RejectAction();
    }

    // ========== Policy Validator (Item 3) ==========

    @Bean
    OnboardingPolicyValidator onboardingPolicyValidator() {
        return new OnboardingPolicyValidator();
    }

    // ========== Post-Save Hooks (Item 13) ==========

    @Bean
    APPLICATION_SUBMITTEDOnboardingSagaPostSaveHook onboardingAPPLICATION_SUBMITTEDPostSaveHook() {
        return new APPLICATION_SUBMITTEDOnboardingSagaPostSaveHook();
    }

    @Bean
    DOCUMENT_VERIFICATIONOnboardingSagaPostSaveHook onboardingDOCUMENT_VERIFICATIONPostSaveHook() {
        return new DOCUMENT_VERIFICATIONOnboardingSagaPostSaveHook();
    }

    @Bean
    DOCUMENTS_REQUESTEDOnboardingSagaPostSaveHook onboardingDOCUMENTS_REQUESTEDPostSaveHook() {
        return new DOCUMENTS_REQUESTEDOnboardingSagaPostSaveHook();
    }

    @Bean
    BUSINESS_VERIFICATIONOnboardingSagaPostSaveHook onboardingBUSINESS_VERIFICATIONPostSaveHook() {
        return new BUSINESS_VERIFICATIONOnboardingSagaPostSaveHook();
    }

    @Bean
    TRAININGOnboardingSagaPostSaveHook onboardingTRAININGPostSaveHook() {
        return new TRAININGOnboardingSagaPostSaveHook();
    }

    @Bean
    ONBOARDEDOnboardingSagaPostSaveHook onboardingONBOARDEDPostSaveHook() {
        return new ONBOARDEDOnboardingSagaPostSaveHook();
    }

    @Bean
    COMPLETEDOnboardingSagaPostSaveHook onboardingCOMPLETEDPostSaveHook() {
        return new COMPLETEDOnboardingSagaPostSaveHook();
    }

    @Bean
    REJECTEDOnboardingSagaPostSaveHook onboardingREJECTEDPostSaveHook() {
        return new REJECTEDOnboardingSagaPostSaveHook();
    }

    // ========== ACL / Security (Item 15) ==========

    @Bean
    java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> onboardingEventAuthoritiesSupplier(
            @Qualifier("onboardingActionsInfoProvider") STMActionsInfoProvider onboardingInfoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(onboardingInfoProvider, false);
        return builder.build();
    }

    // ========== Kafka Event Handler (Item 10+14) ==========

    @Bean("onboardingEventService")
    OnboardingEventHandler onboardingEventService(
            @Qualifier("_onboardingStateEntityService_") StateEntityServiceImpl<OnboardingSaga> onboardingStateEntityService,
            tools.jackson.databind.ObjectMapper objectMapper) {
        return new OnboardingEventHandler(onboardingStateEntityService, objectMapper);
    }
}
