package com.homebase.ecom.review.configuration;

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
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.service.cmds.*;
import com.homebase.ecom.review.service.healthcheck.ReviewHealthChecker;
import com.homebase.ecom.review.service.event.ReviewEventHandler;
import com.homebase.ecom.review.service.validator.ReviewPolicyValidator;
import com.homebase.ecom.review.service.postSaveHooks.*;
import com.homebase.ecom.review.infrastructure.persistence.ChenileReviewEntityStore;
import com.homebase.ecom.review.infrastructure.persistence.adapter.ReviewJpaRepository;
import com.homebase.ecom.review.infrastructure.persistence.mapper.ReviewMapper;
import com.homebase.ecom.review.domain.port.ModerationPort;
import com.homebase.ecom.review.domain.port.NotificationPort;
import com.homebase.ecom.review.infrastructure.adapter.LoggingModerationAdapter;
import com.homebase.ecom.review.infrastructure.adapter.LoggingNotificationAdapter;

/**
 * Full Chenile STM configuration for the Review module.
 *
 * States: SUBMITTED -> CHECK_AUTO_PUBLISH -> PUBLISHED/UNDER_MODERATION
 *         UNDER_MODERATION -> PUBLISHED/REJECTED/EDIT_REQUESTED
 *         PUBLISHED -> FLAGGED/ARCHIVED
 *         FLAGGED -> UNDER_MODERATION/PUBLISHED/REJECTED
 *         EDIT_REQUESTED -> UNDER_MODERATION
 */
@Configuration
public class ReviewConfiguration {
    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/review/review-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Review";
    public static final String PREFIX_FOR_RESOLVER = "review";

    @Bean
    BeanFactoryAdapter reviewBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl reviewFlowStore(
            @Qualifier("reviewBeanFactoryAdapter") BeanFactoryAdapter reviewBeanFactoryAdapter) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(reviewBeanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean
    STM<Review> reviewEntityStm(@Qualifier("reviewFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<Review> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider reviewActionsInfoProvider(@Qualifier("reviewFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("review", provider);
        return provider;
    }

    @Bean
    ReviewMapper reviewMapper() {
        return new ReviewMapper();
    }

    @Bean
    EntityStore<Review> reviewEntityStore(ReviewJpaRepository jpaRepository, ReviewMapper mapper) {
        return new ChenileReviewEntityStore(jpaRepository, mapper);
    }

    @Bean
    StateEntityServiceImpl<Review> _reviewStateEntityService_(
            @Qualifier("reviewEntityStm") STM<Review> stm,
            @Qualifier("reviewActionsInfoProvider") STMActionsInfoProvider reviewInfoProvider,
            @Qualifier("reviewEntityStore") EntityStore<Review> entityStore) {
        return new HmStateEntityServiceImpl<>(stm, reviewInfoProvider, entityStore);
    }

    // ================================================================
    // STM Infrastructure
    // ================================================================

    @Bean
    DefaultPostSaveHook<Review> reviewDefaultPostSaveHook(
            @Qualifier("reviewTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean
    GenericEntryAction<Review> reviewEntryAction(@Qualifier("reviewEntityStore") EntityStore<Review> entityStore,
            @Qualifier("reviewActionsInfoProvider") STMActionsInfoProvider reviewInfoProvider,
            @Qualifier("reviewFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("reviewDefaultPostSaveHook") DefaultPostSaveHook<Review> postSaveHook) {
        GenericEntryAction<Review> entryAction = new GenericEntryAction<>(entityStore, reviewInfoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    DefaultAutomaticStateComputation<Review> reviewDefaultAutoState(
            @Qualifier("reviewTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("reviewFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<Review> autoState = new DefaultAutomaticStateComputation<>(
                stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    GenericExitAction<Review> reviewExitAction(@Qualifier("reviewFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<Review> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    XmlFlowReader reviewFlowReader(@Qualifier("reviewFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean
    ReviewHealthChecker reviewHealthChecker() {
        return new ReviewHealthChecker();
    }

    // OGNL scripting for auto-states (CHECK_AUTO_PUBLISH)
    @Bean
    OgnlScriptingStrategy ognlScriptingStrategy() {
        return new OgnlScriptingStrategy();
    }

    @Bean
    IfAction<Review> ifAction() {
        return new IfAction<>();
    }

    @Bean
    STMTransitionAction<Review> defaultreviewSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver reviewTransitionActionResolver(
            @Qualifier("defaultreviewSTMTransitionAction") STMTransitionAction<Review> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean
    StmBodyTypeSelector reviewBodyTypeSelector(
            @Qualifier("reviewActionsInfoProvider") STMActionsInfoProvider reviewInfoProvider,
            @Qualifier("reviewTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(reviewInfoProvider, stmTransitionActionResolver);
    }

    @Bean
    STMTransitionAction<Review> reviewBaseTransitionAction(
            @Qualifier("reviewTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("reviewActivityChecker") ActivityChecker activityChecker,
            @Qualifier("reviewFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<Review> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean
    ActivityChecker reviewActivityChecker(@Qualifier("reviewFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete reviewActivitiesCompletionCheck(
            @Qualifier("reviewActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    @Bean
    ConfigProviderImpl reviewConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    ConfigBasedEnablementStrategy reviewConfigBasedEnablementStrategy(
            @Qualifier("reviewConfigProvider") ConfigProvider configProvider,
            @Qualifier("reviewFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider,
                PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // ================================================================
    // Transition Actions (naming: "review" + eventId + "Action")
    // ================================================================

    @Bean
    SubmitReviewAction reviewSubmitReviewAction() {
        return new SubmitReviewAction();
    }

    @Bean
    PublishReviewAction reviewPublishReviewAction() {
        return new PublishReviewAction();
    }

    @Bean
    RejectReviewAction reviewRejectReviewAction() {
        return new RejectReviewAction();
    }

    @Bean
    FlagReviewAction reviewFlagReviewAction() {
        return new FlagReviewAction();
    }

    @Bean
    ModerateReviewAction reviewModerateReviewAction() {
        return new ModerateReviewAction();
    }

    @Bean
    RequestEditAction reviewRequestEditAction() {
        return new RequestEditAction();
    }

    @Bean
    ResubmitReviewAction reviewResubmitReviewAction() {
        return new ResubmitReviewAction();
    }

    @Bean
    ArchiveReviewAction reviewArchiveReviewAction() {
        return new ArchiveReviewAction();
    }

    @Bean
    MarkHelpfulAction reviewMarkHelpfulAction() {
        return new MarkHelpfulAction();
    }

    @Bean
    ReportReviewAction reviewReportReviewAction() {
        return new ReportReviewAction();
    }

    // ================================================================
    // Post Save Hooks (naming: "review" + STATE + "PostSaveHook")
    // ================================================================

    @Bean
    SUBMITTEDReviewPostSaveHook reviewSUBMITTEDPostSaveHook() {
        return new SUBMITTEDReviewPostSaveHook();
    }

    @Bean
    UNDER_MODERATIONReviewPostSaveHook reviewUNDER_MODERATIONPostSaveHook() {
        return new UNDER_MODERATIONReviewPostSaveHook();
    }

    @Bean
    PUBLISHEDReviewPostSaveHook reviewPUBLISHEDPostSaveHook() {
        return new PUBLISHEDReviewPostSaveHook();
    }

    @Bean
    FLAGGEDReviewPostSaveHook reviewFLAGGEDPostSaveHook() {
        return new FLAGGEDReviewPostSaveHook();
    }

    @Bean
    REJECTEDReviewPostSaveHook reviewREJECTEDPostSaveHook() {
        return new REJECTEDReviewPostSaveHook();
    }

    @Bean
    EDIT_REQUESTEDReviewPostSaveHook reviewEDIT_REQUESTEDPostSaveHook() {
        return new EDIT_REQUESTEDReviewPostSaveHook();
    }

    @Bean
    ARCHIVEDReviewPostSaveHook reviewARCHIVEDPostSaveHook() {
        return new ARCHIVEDReviewPostSaveHook();
    }

    // ================================================================
    // Security: ACL authorities supplier for STM events
    // ================================================================

    @Bean
    java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> reviewEventAuthoritiesSupplier(
            @Qualifier("reviewActionsInfoProvider") STMActionsInfoProvider reviewInfoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(reviewInfoProvider, false);
        return builder.build();
    }

    // ================================================================
    // Policy Validator
    // ================================================================

    @Bean
    ReviewPolicyValidator reviewPolicyValidator() {
        return new ReviewPolicyValidator();
    }

    // ================================================================
    // Hexagonal Ports (default adapters)
    // ================================================================

    @Bean
    ModerationPort moderationPort() {
        return new LoggingModerationAdapter();
    }

    @Bean
    NotificationPort notificationPort() {
        return new LoggingNotificationAdapter();
    }

    // ================================================================
    // Kafka Event Handler (conditional on Kafka being available)
    // ================================================================

    @Bean("reviewEventService")
    ReviewEventHandler reviewEventService(
            org.chenile.pubsub.ChenilePub chenilePub,
            tools.jackson.databind.ObjectMapper objectMapper) {
        return new ReviewEventHandler(chenilePub, objectMapper);
    }
}
