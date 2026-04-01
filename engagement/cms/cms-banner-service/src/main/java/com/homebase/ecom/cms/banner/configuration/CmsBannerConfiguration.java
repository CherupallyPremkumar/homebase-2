package com.homebase.ecom.cms.banner.configuration;

import com.homebase.ecom.cms.infrastructure.persistence.adapter.BannerJpaRepository;
import com.homebase.ecom.cms.infrastructure.persistence.ChenileBannerEntityStore;
import com.homebase.ecom.cms.infrastructure.persistence.mapper.CmsMapper;
import com.homebase.ecom.cms.model.Banner;
import com.homebase.ecom.cms.banner.service.cmds.*;
import com.homebase.ecom.cms.banner.service.postSaveHooks.*;
import com.homebase.ecom.cms.banner.service.healthcheck.BannerHealthChecker;
import com.homebase.ecom.cms.banner.service.validator.CmsBannerPolicyValidator;
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
public class CmsBannerConfiguration {

    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/cms/banner/banner-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Banner";
    public static final String PREFIX_FOR_RESOLVER = "cmsBanner";

    // ================================================================
    // STM Infrastructure — Banner workflow
    // ================================================================

    @Bean BeanFactoryAdapter cmsBannerBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean STMFlowStoreImpl cmsBannerFlowStore(
            @Qualifier("cmsBannerBeanFactoryAdapter") BeanFactoryAdapter cmsBannerBeanFactoryAdapter
    ) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(cmsBannerBeanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean STM<Banner> cmsBannerEntityStm(@Qualifier("cmsBannerFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<Banner> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean STMActionsInfoProvider cmsBannerActionsInfoProvider(@Qualifier("cmsBannerFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("cmsBanner", provider);
        return provider;
    }

    @Bean EntityStore<Banner> cmsBannerEntityStore(BannerJpaRepository jpaRepository, CmsMapper mapper) {
        return new ChenileBannerEntityStore(jpaRepository, mapper);
    }

    @Bean StateEntityServiceImpl<Banner> _cmsBannerStateEntityService_(
            @Qualifier("cmsBannerEntityStm") STM<Banner> stm,
            @Qualifier("cmsBannerActionsInfoProvider") STMActionsInfoProvider cmsBannerInfoProvider,
            @Qualifier("cmsBannerEntityStore") EntityStore<Banner> entityStore) {
        return new HmStateEntityServiceImpl<>(stm, cmsBannerInfoProvider, entityStore);
    }

    // OGNL scripting for auto-states
    @Bean OgnlScriptingStrategy ognlScriptingStrategy() {
        return new OgnlScriptingStrategy();
    }

    // ================================================================
    // PostSaveHook infrastructure
    // ================================================================

    @Bean DefaultPostSaveHook<Banner> cmsBannerDefaultPostSaveHook(
            @Qualifier("cmsBannerTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean GenericEntryAction<Banner> cmsBannerEntryAction(
            @Qualifier("cmsBannerEntityStore") EntityStore<Banner> entityStore,
            @Qualifier("cmsBannerActionsInfoProvider") STMActionsInfoProvider cmsBannerInfoProvider,
            @Qualifier("cmsBannerFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("cmsBannerDefaultPostSaveHook") DefaultPostSaveHook<Banner> postSaveHook) {
        GenericEntryAction<Banner> entryAction = new GenericEntryAction<>(entityStore, cmsBannerInfoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean DefaultAutomaticStateComputation<Banner> cmsBannerDefaultAutoState(
            @Qualifier("cmsBannerTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("cmsBannerFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<Banner> autoState = new DefaultAutomaticStateComputation<>(stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean GenericExitAction<Banner> cmsBannerExitAction(@Qualifier("cmsBannerFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<Banner> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean XmlFlowReader cmsBannerFlowReader(@Qualifier("cmsBannerFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    // ================================================================
    // STM Transition Action Resolver
    // ================================================================

    @Bean STMTransitionAction<Banner> defaultcmsBannerSTMTransitionAction() {
        return new DefaultBannerTransitionAction<MinimalPayload>();
    }

    @Bean STMTransitionActionResolver cmsBannerTransitionActionResolver(
            @Qualifier("defaultcmsBannerSTMTransitionAction") STMTransitionAction<Banner> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean StmBodyTypeSelector cmsBannerBodyTypeSelector(
            @Qualifier("cmsBannerActionsInfoProvider") STMActionsInfoProvider cmsBannerInfoProvider,
            @Qualifier("cmsBannerTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(cmsBannerInfoProvider, stmTransitionActionResolver);
    }

    @Bean STMTransitionAction<Banner> cmsBannerBaseTransitionAction(
            @Qualifier("cmsBannerTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("cmsBannerActivityChecker") ActivityChecker activityChecker,
            @Qualifier("cmsBannerFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<Banner> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean ActivityChecker cmsBannerActivityChecker(@Qualifier("cmsBannerFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean AreActivitiesComplete cmsBannerActivitiesCompletionCheck(@Qualifier("cmsBannerActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    // ================================================================
    // STM Transition Actions (convention: "cmsBanner" + eventId + "Action")
    // ================================================================

    @Bean ActivateBannerAction cmsBannerActivateAction() {
        return new ActivateBannerAction();
    }

    @Bean DeactivateBannerAction cmsBannerDeactivateAction() {
        return new DeactivateBannerAction();
    }

    @Bean DefaultBannerTransitionAction<MinimalPayload> cmsBannerDefaultTransitionAction() {
        return new DefaultBannerTransitionAction<>();
    }

    // ================================================================
    // Config and Enablement
    // ================================================================

    @Bean ConfigProviderImpl cmsBannerConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean ConfigBasedEnablementStrategy cmsBannerConfigBasedEnablementStrategy(
            @Qualifier("cmsBannerConfigProvider") ConfigProvider configProvider,
            @Qualifier("cmsBannerFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider, PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // ================================================================
    // PostSaveHooks (convention: "cmsBanner" + STATE + "PostSaveHook")
    // ================================================================

    @Bean ACTIVEBannerPostSaveHook cmsBannerACTIVEPostSaveHook() {
        return new ACTIVEBannerPostSaveHook();
    }

    // ================================================================
    // Security: STM ACL authorities builder
    // ================================================================

    @Bean java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> cmsBannerEventAuthoritiesSupplier(
            @Qualifier("cmsBannerActionsInfoProvider") STMActionsInfoProvider cmsBannerInfoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(cmsBannerInfoProvider, false);
        return builder.build();
    }

    // ================================================================
    // Health Checker
    // ================================================================

    @Bean BannerHealthChecker cmsBannerHealthChecker() {
        return new BannerHealthChecker();
    }

    // ================================================================
    // Config Port + Policy Validator
    // ================================================================

    @Bean CmsConfigPort cmsConfigPort(@Autowired(required = false) CconfigClient cconfigClient) {
        return new CconfigCmsConfigAdapter(cconfigClient, "cms-banner");
    }

    @Bean CmsBannerPolicyValidator cmsBannerPolicyValidator(CmsConfigPort cmsConfigPort) {
        return new CmsBannerPolicyValidator(cmsConfigPort);
    }
}
