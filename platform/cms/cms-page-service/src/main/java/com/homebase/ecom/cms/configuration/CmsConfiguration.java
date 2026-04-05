package com.homebase.ecom.cms.configuration;

import com.homebase.ecom.cms.infrastructure.persistence.adapter.*;
import com.homebase.ecom.cms.infrastructure.persistence.ChenileCmsPageEntityStore;
import com.homebase.ecom.cms.infrastructure.persistence.mapper.CmsMapper;
import com.homebase.ecom.cms.model.CmsPage;
import com.homebase.ecom.cms.model.port.CmsBlockRepository;
import com.homebase.ecom.cms.model.port.CmsPageRepository;
import com.homebase.ecom.cms.model.port.CmsPageVersionRepository;
import com.homebase.ecom.cms.model.port.CmsScheduleRepository;
import com.homebase.ecom.cms.model.port.CmsSeoMetaRepository;
import com.homebase.ecom.cms.model.port.BannerRepository;
import com.homebase.ecom.cms.service.CmsService;
import com.homebase.ecom.cms.service.impl.CmsBlockServiceImpl;
import com.homebase.ecom.cms.service.impl.CmsSeoMetaServiceImpl;
import com.homebase.ecom.cms.service.impl.CmsServiceImpl;
import com.homebase.ecom.cms.service.cmds.*;
import com.homebase.ecom.cms.service.postSaveHooks.*;
import com.homebase.ecom.cms.service.healthcheck.CmsPageHealthChecker;
import com.homebase.ecom.cms.service.validator.CmsPagePolicyValidator;
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
public class CmsConfiguration {

    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/cms/cms-page-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "CmsPage";
    public static final String PREFIX_FOR_RESOLVER = "cmsPage";

    // ================================================================
    // Repositories
    // ================================================================

    @Bean
    public CmsPageRepository cmsPageRepository(CmsPageJpaRepository jpaRepository, CmsMapper mapper) {
        return new CmsPageRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public BannerRepository bannerRepository(BannerJpaRepository jpaRepository, CmsMapper mapper) {
        return new BannerRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public CmsService cmsService(CmsPageRepository pageRepository, BannerRepository bannerRepository, CmsMapper mapper) {
        return new CmsServiceImpl(pageRepository, bannerRepository, mapper);
    }

    // ================================================================
    // CmsPage-related repository and service beans
    // ================================================================

    @Bean
    public CmsBlockRepository cmsBlockRepository(CmsBlockJpaRepository jpaRepository, CmsMapper mapper) {
        return new CmsBlockRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public CmsPageVersionRepository cmsPageVersionRepository(CmsPageVersionJpaRepository jpaRepository, CmsMapper mapper) {
        return new CmsPageVersionRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public CmsScheduleRepository cmsScheduleRepository(CmsScheduleJpaRepository jpaRepository, CmsMapper mapper) {
        return new CmsScheduleRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public CmsBlockServiceImpl cmsBlockServiceImpl(CmsBlockRepository cmsBlockRepository) {
        return new CmsBlockServiceImpl(cmsBlockRepository);
    }

    @Bean
    public CmsSeoMetaRepository cmsSeoMetaRepository(CmsSeoMetaJpaRepository jpaRepository, CmsMapper mapper) {
        return new CmsSeoMetaRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public CmsSeoMetaServiceImpl cmsSeoMetaServiceImpl(CmsSeoMetaRepository cmsSeoMetaRepository) {
        return new CmsSeoMetaServiceImpl(cmsSeoMetaRepository);
    }

    // ================================================================
    // STM Infrastructure — CmsPage workflow
    // ================================================================

    @Bean BeanFactoryAdapter cmsPageBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean STMFlowStoreImpl cmsPageFlowStore(
            @Qualifier("cmsPageBeanFactoryAdapter") BeanFactoryAdapter cmsPageBeanFactoryAdapter
    ) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(cmsPageBeanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean STM<CmsPage> cmsPageEntityStm(@Qualifier("cmsPageFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<CmsPage> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean STMActionsInfoProvider cmsPageActionsInfoProvider(@Qualifier("cmsPageFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("cmsPage", provider);
        return provider;
    }

    @Bean EntityStore<CmsPage> cmsPageEntityStore(CmsPageJpaRepository jpaRepository, CmsMapper mapper, CmsConfigPort cmsConfigPort) {
        return new ChenileCmsPageEntityStore(jpaRepository, mapper, cmsConfigPort);
    }

    @Bean StateEntityServiceImpl<CmsPage> _cmsPageStateEntityService_(
            @Qualifier("cmsPageEntityStm") STM<CmsPage> stm,
            @Qualifier("cmsPageActionsInfoProvider") STMActionsInfoProvider cmsPageInfoProvider,
            @Qualifier("cmsPageEntityStore") EntityStore<CmsPage> entityStore) {
        return new HmStateEntityServiceImpl<>(stm, cmsPageInfoProvider, entityStore);
    }

    // OGNL scripting for auto-states
    @Bean OgnlScriptingStrategy ognlScriptingStrategy() {
        return new OgnlScriptingStrategy();
    }

    // ================================================================
    // PostSaveHook infrastructure
    // ================================================================

    @Bean DefaultPostSaveHook<CmsPage> cmsPageDefaultPostSaveHook(
            @Qualifier("cmsPageTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean GenericEntryAction<CmsPage> cmsPageEntryAction(
            @Qualifier("cmsPageEntityStore") EntityStore<CmsPage> entityStore,
            @Qualifier("cmsPageActionsInfoProvider") STMActionsInfoProvider cmsPageInfoProvider,
            @Qualifier("cmsPageFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("cmsPageDefaultPostSaveHook") DefaultPostSaveHook<CmsPage> postSaveHook) {
        GenericEntryAction<CmsPage> entryAction = new GenericEntryAction<>(entityStore, cmsPageInfoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean DefaultAutomaticStateComputation<CmsPage> cmsPageDefaultAutoState(
            @Qualifier("cmsPageTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("cmsPageFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<CmsPage> autoState = new DefaultAutomaticStateComputation<>(stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean GenericExitAction<CmsPage> cmsPageExitAction(@Qualifier("cmsPageFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<CmsPage> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean XmlFlowReader cmsPageFlowReader(@Qualifier("cmsPageFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    // ================================================================
    // STM Transition Action Resolver
    // ================================================================

    @Bean STMTransitionAction<CmsPage> defaultcmsPageSTMTransitionAction() {
        return new DefaultCmsPageTransitionAction<MinimalPayload>();
    }

    @Bean STMTransitionActionResolver cmsPageTransitionActionResolver(
            @Qualifier("defaultcmsPageSTMTransitionAction") STMTransitionAction<CmsPage> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean StmBodyTypeSelector cmsPageBodyTypeSelector(
            @Qualifier("cmsPageActionsInfoProvider") STMActionsInfoProvider cmsPageInfoProvider,
            @Qualifier("cmsPageTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(cmsPageInfoProvider, stmTransitionActionResolver);
    }

    @Bean STMTransitionAction<CmsPage> cmsPageBaseTransitionAction(
            @Qualifier("cmsPageTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("cmsPageActivityChecker") ActivityChecker activityChecker,
            @Qualifier("cmsPageFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<CmsPage> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean ActivityChecker cmsPageActivityChecker(@Qualifier("cmsPageFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean AreActivitiesComplete cmsPageActivitiesCompletionCheck(@Qualifier("cmsPageActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    // ================================================================
    // STM Transition Actions (convention: "cmsPage" + eventId + "Action")
    // ================================================================

    @Bean PublishCmsPageAction cmsPagePublishAction() {
        return new PublishCmsPageAction();
    }

    @Bean UnpublishCmsPageAction cmsPageUnpublishAction() {
        return new UnpublishCmsPageAction();
    }

    @Bean ArchiveCmsPageAction cmsPageArchiveAction() {
        return new ArchiveCmsPageAction();
    }

    @Bean ApproveCmsPageAction cmsPageApproveAction() {
        return new ApproveCmsPageAction();
    }

    @Bean RejectCmsPageAction cmsPageRejectAction() {
        return new RejectCmsPageAction();
    }

    @Bean DefaultCmsPageTransitionAction<MinimalPayload> cmsPageDefaultTransitionAction() {
        return new DefaultCmsPageTransitionAction<>();
    }

    // ================================================================
    // Config and Enablement
    // ================================================================

    @Bean ConfigProviderImpl cmsPageConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean ConfigBasedEnablementStrategy cmsPageConfigBasedEnablementStrategy(
            @Qualifier("cmsPageConfigProvider") ConfigProvider configProvider,
            @Qualifier("cmsPageFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider, PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // ================================================================
    // PostSaveHooks (convention: "cmsPage" + STATE + "PostSaveHook")
    // ================================================================

    @Bean PUBLISHEDCmsPagePostSaveHook cmsPagePUBLISHEDPostSaveHook() {
        return new PUBLISHEDCmsPagePostSaveHook();
    }

    // ================================================================
    // Security: STM ACL authorities builder
    // ================================================================

    @Bean java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> cmsPageEventAuthoritiesSupplier(
            @Qualifier("cmsPageActionsInfoProvider") STMActionsInfoProvider cmsPageInfoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(cmsPageInfoProvider, false);
        return builder.build();
    }

    // ================================================================
    // Health Checker
    // ================================================================

    @Bean CmsPageHealthChecker cmsPageHealthChecker() {
        return new CmsPageHealthChecker();
    }

    // ================================================================
    // Config Port + Policy Validator
    // ================================================================

    @Bean CmsConfigPort cmsConfigPort(@Autowired(required = false) CconfigClient cconfigClient) {
        return new CconfigCmsConfigAdapter(cconfigClient, "cms-page");
    }

    @Bean CmsPagePolicyValidator cmsPagePolicyValidator(CmsConfigPort cmsConfigPort) {
        return new CmsPagePolicyValidator(cmsConfigPort);
    }
}
