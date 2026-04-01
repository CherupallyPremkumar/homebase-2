package com.homebase.ecom.cms.announcement.configuration;

import com.homebase.ecom.cms.infrastructure.persistence.adapter.AnnouncementJpaRepository;
import com.homebase.ecom.cms.infrastructure.persistence.ChenileAnnouncementEntityStore;
import com.homebase.ecom.cms.infrastructure.persistence.mapper.CmsMapper;
import com.homebase.ecom.cms.model.Announcement;
import com.homebase.ecom.cms.announcement.service.cmds.*;
import com.homebase.ecom.cms.announcement.service.postSaveHooks.*;
import com.homebase.ecom.cms.announcement.service.healthcheck.AnnouncementHealthChecker;
import com.homebase.ecom.cms.announcement.service.validator.CmsAnnouncementPolicyValidator;
import com.homebase.ecom.cms.model.port.CmsConfigPort;
import com.homebase.ecom.cms.infrastructure.integration.CconfigCmsConfigAdapter;
import com.homebase.ecom.cconfig.sdk.CconfigClient;
import org.springframework.beans.factory.annotation.Autowired;
import com.homebase.ecom.core.workflow.HmStateEntityServiceImpl;

import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.ognl.OgnlScriptingStrategy;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CmsAnnouncementConfiguration {

    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/cms/announcement/announcement-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Announcement";
    public static final String PREFIX_FOR_RESOLVER = "cmsAnnouncement";

    // ================================================================
    // STM Infrastructure — Announcement workflow
    // ================================================================

    @Bean BeanFactoryAdapter cmsAnnouncementBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean STMFlowStoreImpl cmsAnnouncementFlowStore(
            @Qualifier("cmsAnnouncementBeanFactoryAdapter") BeanFactoryAdapter cmsAnnouncementBeanFactoryAdapter
    ) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(cmsAnnouncementBeanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean STM<Announcement> cmsAnnouncementEntityStm(@Qualifier("cmsAnnouncementFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<Announcement> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean STMActionsInfoProvider cmsAnnouncementActionsInfoProvider(@Qualifier("cmsAnnouncementFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("cmsAnnouncement", provider);
        return provider;
    }

    @Bean EntityStore<Announcement> cmsAnnouncementEntityStore(AnnouncementJpaRepository jpaRepository, CmsMapper mapper) {
        return new ChenileAnnouncementEntityStore(jpaRepository, mapper);
    }

    @Bean StateEntityServiceImpl<Announcement> _cmsAnnouncementStateEntityService_(
            @Qualifier("cmsAnnouncementEntityStm") STM<Announcement> stm,
            @Qualifier("cmsAnnouncementActionsInfoProvider") STMActionsInfoProvider cmsAnnouncementInfoProvider,
            @Qualifier("cmsAnnouncementEntityStore") EntityStore<Announcement> entityStore) {
        return new HmStateEntityServiceImpl<>(stm, cmsAnnouncementInfoProvider, entityStore);
    }

    // OGNL scripting for auto-states
    @Bean OgnlScriptingStrategy ognlScriptingStrategy() {
        return new OgnlScriptingStrategy();
    }

    // ================================================================
    // PostSaveHook infrastructure
    // ================================================================

    @Bean DefaultPostSaveHook<Announcement> cmsAnnouncementDefaultPostSaveHook(
            @Qualifier("cmsAnnouncementTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean GenericEntryAction<Announcement> cmsAnnouncementEntryAction(
            @Qualifier("cmsAnnouncementEntityStore") EntityStore<Announcement> entityStore,
            @Qualifier("cmsAnnouncementActionsInfoProvider") STMActionsInfoProvider cmsAnnouncementInfoProvider,
            @Qualifier("cmsAnnouncementFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("cmsAnnouncementDefaultPostSaveHook") DefaultPostSaveHook<Announcement> postSaveHook) {
        GenericEntryAction<Announcement> entryAction = new GenericEntryAction<>(entityStore, cmsAnnouncementInfoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean DefaultAutomaticStateComputation<Announcement> cmsAnnouncementDefaultAutoState(
            @Qualifier("cmsAnnouncementTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("cmsAnnouncementFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<Announcement> autoState = new DefaultAutomaticStateComputation<>(stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean GenericExitAction<Announcement> cmsAnnouncementExitAction(@Qualifier("cmsAnnouncementFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<Announcement> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean XmlFlowReader cmsAnnouncementFlowReader(@Qualifier("cmsAnnouncementFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    // ================================================================
    // STM Transition Action Resolver
    // ================================================================

    @Bean STMTransitionAction<Announcement> defaultcmsAnnouncementSTMTransitionAction() {
        return new DefaultAnnouncementTransitionAction<MinimalPayload>();
    }

    @Bean STMTransitionActionResolver cmsAnnouncementTransitionActionResolver(
            @Qualifier("defaultcmsAnnouncementSTMTransitionAction") STMTransitionAction<Announcement> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean StmBodyTypeSelector cmsAnnouncementBodyTypeSelector(
            @Qualifier("cmsAnnouncementActionsInfoProvider") STMActionsInfoProvider cmsAnnouncementInfoProvider,
            @Qualifier("cmsAnnouncementTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(cmsAnnouncementInfoProvider, stmTransitionActionResolver);
    }

    @Bean STMTransitionAction<Announcement> cmsAnnouncementBaseTransitionAction(
            @Qualifier("cmsAnnouncementTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("cmsAnnouncementActivityChecker") ActivityChecker activityChecker,
            @Qualifier("cmsAnnouncementFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<Announcement> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean ActivityChecker cmsAnnouncementActivityChecker(@Qualifier("cmsAnnouncementFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean AreActivitiesComplete cmsAnnouncementActivitiesCompletionCheck(@Qualifier("cmsAnnouncementActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    // ================================================================
    // STM Transition Actions (convention: "cmsAnnouncement" + eventId + "Action")
    // ================================================================

    @Bean ScheduleAnnouncementAction cmsAnnouncementScheduleAction() {
        return new ScheduleAnnouncementAction();
    }

    @Bean ActivateAnnouncementAction cmsAnnouncementActivateAction() {
        return new ActivateAnnouncementAction();
    }

    @Bean ExpireAnnouncementAction cmsAnnouncementExpireAction() {
        return new ExpireAnnouncementAction();
    }

    @Bean DefaultAnnouncementTransitionAction<MinimalPayload> cmsAnnouncementDefaultTransitionAction() {
        return new DefaultAnnouncementTransitionAction<>();
    }

    // ================================================================
    // Config and Enablement
    // ================================================================

    @Bean ConfigProviderImpl cmsAnnouncementConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean ConfigBasedEnablementStrategy cmsAnnouncementConfigBasedEnablementStrategy(
            @Qualifier("cmsAnnouncementConfigProvider") ConfigProvider configProvider,
            @Qualifier("cmsAnnouncementFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider, PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // ================================================================
    // PostSaveHooks (convention: "cmsAnnouncement" + STATE + "PostSaveHook")
    // ================================================================

    @Bean ACTIVEAnnouncementPostSaveHook cmsAnnouncementACTIVEPostSaveHook() {
        return new ACTIVEAnnouncementPostSaveHook();
    }

    // ================================================================
    // Security: STM ACL authorities builder
    // ================================================================

    @Bean java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> cmsAnnouncementEventAuthoritiesSupplier(
            @Qualifier("cmsAnnouncementActionsInfoProvider") STMActionsInfoProvider cmsAnnouncementInfoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(cmsAnnouncementInfoProvider, false);
        return builder.build();
    }

    // ================================================================
    // Health Checker
    // ================================================================

    @Bean AnnouncementHealthChecker cmsAnnouncementHealthChecker() {
        return new AnnouncementHealthChecker();
    }

    // ================================================================
    // Config Port + Policy Validator
    // ================================================================

    @Bean CmsConfigPort cmsConfigPort(@Autowired(required = false) CconfigClient cconfigClient) {
        return new CconfigCmsConfigAdapter(cconfigClient, "cms-announcement");
    }

    @Bean CmsAnnouncementPolicyValidator cmsAnnouncementPolicyValidator(CmsConfigPort cmsConfigPort) {
        return new CmsAnnouncementPolicyValidator(cmsConfigPort);
    }
}
