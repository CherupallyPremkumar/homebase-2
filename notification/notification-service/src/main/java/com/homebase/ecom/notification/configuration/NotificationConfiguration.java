package com.homebase.ecom.notification.configuration;

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
import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.infrastructure.persistence.mapper.NotificationMapper;
import com.homebase.ecom.notification.infrastructure.persistence.adapter.NotificationJpaRepository;
import com.homebase.ecom.notification.infrastructure.persistence.adapter.NotificationRepositoryImpl;
import com.homebase.ecom.notification.infrastructure.persistence.ChenileNotificationEntityStore;
import com.homebase.ecom.notification.domain.port.NotificationRepository;
import com.homebase.ecom.notification.service.cmds.*;
import com.homebase.ecom.notification.service.healthcheck.NotificationHealthChecker;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import com.homebase.ecom.notification.service.postSaveHooks.*;

/**
 * Spring configuration for the Notification STM module.
 */
@Configuration
public class NotificationConfiguration {
    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/notification/notification-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Notification";
    public static final String PREFIX_FOR_RESOLVER = "notification";

    @Bean
    BeanFactoryAdapter notificationBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl notificationFlowStore(
            @Qualifier("notificationBeanFactoryAdapter") BeanFactoryAdapter notificationBeanFactoryAdapter) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(notificationBeanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean
    STM<Notification> notificationEntityStm(@Qualifier("notificationFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<Notification> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider notificationActionsInfoProvider(
            @Qualifier("notificationFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("notification", provider);
        return provider;
    }

    @Bean
    NotificationMapper notificationMapper() {
        return new NotificationMapper();
    }

    @Bean
    NotificationRepositoryImpl notificationRepository(NotificationJpaRepository jpaRepository, NotificationMapper mapper) {
        return new NotificationRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    EntityStore<Notification> notificationEntityStore(NotificationJpaRepository repository, NotificationMapper mapper) {
        return new ChenileNotificationEntityStore(repository, mapper);
    }

    @Bean
    StateEntityServiceImpl<Notification> _notificationStateEntityService_(
            @Qualifier("notificationEntityStm") STM<Notification> stm,
            @Qualifier("notificationActionsInfoProvider") STMActionsInfoProvider notificationInfoProvider,
            @Qualifier("notificationEntityStore") EntityStore<Notification> entityStore) {
        return new StateEntityServiceImpl<>(stm, notificationInfoProvider, entityStore);
    }

    // STM Components

    @Bean
    DefaultPostSaveHook<Notification> notificationDefaultPostSaveHook(
            @Qualifier("notificationTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean
    GenericEntryAction<Notification> notificationEntryAction(
            @Qualifier("notificationEntityStore") EntityStore<Notification> entityStore,
            @Qualifier("notificationActionsInfoProvider") STMActionsInfoProvider notificationInfoProvider,
            @Qualifier("notificationFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("notificationDefaultPostSaveHook") DefaultPostSaveHook<Notification> postSaveHook) {
        GenericEntryAction<Notification> entryAction = new GenericEntryAction<>(entityStore,
                notificationInfoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    DefaultAutomaticStateComputation<Notification> notificationDefaultAutoState(
            @Qualifier("notificationTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("notificationFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<Notification> autoState = new DefaultAutomaticStateComputation<>(
                stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    GenericExitAction<Notification> notificationExitAction(@Qualifier("notificationFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<Notification> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    XmlFlowReader notificationFlowReader(@Qualifier("notificationFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean
    NotificationHealthChecker notificationHealthChecker() {
        return new NotificationHealthChecker();
    }

    @Bean
    STMTransitionAction<Notification> defaultnotificationSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver notificationTransitionActionResolver(
            @Qualifier("defaultnotificationSTMTransitionAction") STMTransitionAction<Notification> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean
    StmBodyTypeSelector notificationBodyTypeSelector(
            @Qualifier("notificationActionsInfoProvider") STMActionsInfoProvider notificationInfoProvider,
            @Qualifier("notificationTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(notificationInfoProvider, stmTransitionActionResolver);
    }

    @Bean
    STMTransitionAction<Notification> notificationBaseTransitionAction(
            @Qualifier("notificationTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("notificationActivityChecker") ActivityChecker activityChecker,
            @Qualifier("notificationFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<Notification> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean
    ActivityChecker notificationActivityChecker(@Qualifier("notificationFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete notificationActivitiesCompletionCheck(
            @Qualifier("notificationActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    @Bean
    ConfigProviderImpl notificationConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    ConfigBasedEnablementStrategy notificationConfigBasedEnablementStrategy(
            @Qualifier("notificationConfigProvider") ConfigProvider configProvider,
            @Qualifier("notificationFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider,
                PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // Transition Actions

    @Bean
    SendNotificationAction notificationSendAction() {
        return new SendNotificationAction();
    }

    @Bean
    MarkReadAction notificationMarkReadAction() {
        return new MarkReadAction();
    }

    @Bean
    RetryNotificationAction notificationRetryAction() {
        return new RetryNotificationAction();
    }

    @Bean
    FailNotificationAction notificationFailAction() {
        return new FailNotificationAction();
    }

    // Post Save Hooks

    @Bean
    CREATEDNotificationPostSaveHook notificationCREATEDPostSaveHook() {
        return new CREATEDNotificationPostSaveHook();
    }

    @Bean
    SENTNotificationPostSaveHook notificationSENTPostSaveHook() {
        return new SENTNotificationPostSaveHook();
    }

    @Bean
    FAILEDNotificationPostSaveHook notificationFAILEDPostSaveHook() {
        return new FAILEDNotificationPostSaveHook();
    }

    @Bean
    READNotificationPostSaveHook notificationREADPostSaveHook() {
        return new READNotificationPostSaveHook();
    }
}
