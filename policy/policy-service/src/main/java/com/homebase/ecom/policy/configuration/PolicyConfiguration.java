package com.homebase.ecom.policy.configuration;

import com.homebase.ecom.policy.domain.model.Policy;
import com.homebase.ecom.policy.service.cmds.*;
import com.homebase.ecom.policy.service.impl.DecisionServiceImpl;
import com.homebase.ecom.policy.service.impl.PolicyServiceImpl;
import com.homebase.ecom.policy.service.loader.PolicyDataLoader;
import com.homebase.ecom.policy.service.postSaveHooks.ACTIVEPolicyPostSaveHook;
import com.homebase.ecom.policy.service.postSaveHooks.DEPRECATEDPolicyPostSaveHook;
import com.homebase.ecom.policy.service.postSaveHooks.DRAFTPolicyPostSaveHook;
import com.homebase.ecom.policy.service.postSaveHooks.REVIEWPolicyPostSaveHook;
import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.impl.HmStateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import org.chenile.stm.impl.ConfigBasedEnablementStrategy;

import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import org.chenile.workflow.service.stmcmds.DefaultPostSaveHook;
import org.chenile.workflow.service.stmcmds.DefaultAutomaticStateComputation;
import org.chenile.workflow.param.MinimalPayload;
import com.homebase.ecom.policy.domain.service.*;
import com.homebase.ecom.policy.domain.repository.PolicyRepository;
import com.homebase.ecom.policy.infrastructure.persistence.ChenilePolicyEntityStore;
import com.homebase.ecom.policy.infrastructure.persistence.adapter.*;
import com.homebase.ecom.policy.infrastructure.persistence.mapper.*;
import com.homebase.ecom.policy.infrastructure.mapper.*;
import com.homebase.ecom.policy.infrastructure.persistence.repository.*;
import com.homebase.ecom.policy.api.service.PolicyService;
import com.homebase.ecom.policy.domain.repository.DecisionRepository;
import com.homebase.ecom.policy.infrastructure.persistence.repository.DecisionJpaRepository;
import com.homebase.ecom.policy.infrastructure.persistence.adapter.DecisionRepositoryImpl;
import com.homebase.ecom.policy.infrastructure.persistence.adapter.FactDefinitionJpaRepository;
import com.homebase.ecom.policy.infrastructure.persistence.adapter.FactDefinitionRepositoryImpl;
import com.homebase.ecom.policy.infrastructure.persistence.mapper.FactDefinitionMapper;
import com.homebase.ecom.policy.domain.repository.FactDefinitionRepository;
import com.homebase.ecom.policy.api.service.FactMetadataService;
import com.homebase.ecom.policy.service.impl.FactMetadataServiceImpl;
import com.homebase.ecom.policy.service.loader.FactDataLoader;
import com.homebase.ecom.policy.api.service.DecisionService;
import org.chenile.utils.entity.service.EntityStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PolicyConfiguration {

    @Autowired
    private PolicyJpaRepository policyJpaRepository;

    private static final String FLOW_DEFINITION_FILE = "stm/policy-lifecycle-new.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Policy";
    public static final String PREFIX_FOR_RESOLVER = "policy";

    @Bean
    BeanFactoryAdapter policyBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl policyFlowStore(@Qualifier("policyBeanFactoryAdapter") BeanFactoryAdapter adapter) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(adapter);
        return stmFlowStore;
    }

    @Bean
    STM<Policy> policyEntityStm(@Qualifier("policyFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMImpl<Policy> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider policyActionsInfoProvider(@Qualifier("policyFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("policy", provider);
        return provider;
    }

    @Bean
    HmStateEntityServiceImpl<Policy> policyStateEntityService(
            @Qualifier("policyEntityStm") STM<Policy> stm,
            @Qualifier("policyActionsInfoProvider") STMActionsInfoProvider infoProvider,
            EntityStore<Policy> entityStore) {
        return new HmStateEntityServiceImpl<>(stm, infoProvider, entityStore);
    }

    @Bean
    DefaultPostSaveHook<Policy> policyDefaultPostSaveHook(
            @Qualifier("policyTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean
    GenericEntryAction<Policy> policyEntryAction(EntityStore<Policy> entityStore,
            @Qualifier("policyActionsInfoProvider") STMActionsInfoProvider infoProvider,
            @Qualifier("policyFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("policyDefaultPostSaveHook") DefaultPostSaveHook<Policy> postSaveHook) {
        GenericEntryAction<Policy> entryAction = new GenericEntryAction<>(entityStore, infoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    DefaultAutomaticStateComputation<Policy> policyDefaultAutoState(
            @Qualifier("policyTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("policyFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<Policy> autoState = new DefaultAutomaticStateComputation<>(
                stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    GenericExitAction<Policy> policyExitAction(@Qualifier("policyFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<Policy> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    XmlFlowReader policyFlowReader(@Qualifier("policyFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean
    STMTransitionAction<Policy> defaultPolicySTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver policyTransitionActionResolver(
            @Qualifier("defaultPolicySTMTransitionAction") STMTransitionAction<Policy> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean
    StmBodyTypeSelector policyBodyTypeSelector(
            @Qualifier("policyActionsInfoProvider") STMActionsInfoProvider infoProvider,
            @Qualifier("policyTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(infoProvider, stmTransitionActionResolver);
    }

    @Bean
    STMTransitionAction<Policy> policyBaseTransitionAction(
            @Qualifier("policyTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("policyActivityChecker") ActivityChecker activityChecker,
            @Qualifier("policyFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<Policy> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean
    ActivityChecker policyActivityChecker(@Qualifier("policyFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete policyActivitiesCompletionCheck(
            @Qualifier("policyActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    // Individual Actions
    @Bean
    SubmitPolicyAction policySubmitAction() { return new SubmitPolicyAction(); }

    @Bean
    ActivatePolicyAction policyActivateAction() { return new ActivatePolicyAction(); }

    @Bean
    ApprovePolicyAction policyApproveAction() { return new ApprovePolicyAction(); }

    @Bean
    RevertPolicyAction policyRevertAction() { return new RevertPolicyAction(); }

    @Bean
    DeprecatePolicyAction policyDeprecateAction() { return new DeprecatePolicyAction(); }

    @Bean
    DeactivatePolicyAction policyDeactivateAction() { return new DeactivatePolicyAction(); }

    // Config & Enablement
    @Bean
    ConfigProvider policyConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    public ConfigBasedEnablementStrategy policyConfigBasedEnablementStrategy(
            @Qualifier("policyConfigProvider") ConfigProvider configProvider,
            @Qualifier("policyFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider,
                PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // Post Save Hooks for States
    @Bean
    DRAFTPolicyPostSaveHook policyDRAFTPostSaveHook() { return new DRAFTPolicyPostSaveHook(); }

    @Bean
    REVIEWPolicyPostSaveHook policyREVIEWPostSaveHook() { return new REVIEWPolicyPostSaveHook(); }

    @Bean
    ACTIVEPolicyPostSaveHook policyACTIVEPostSaveHook() { return new ACTIVEPolicyPostSaveHook(); }

    @Bean
    DEPRECATEDPolicyPostSaveHook policyDEPRECATEDPostSaveHook() { return new DEPRECATEDPolicyPostSaveHook(); }

    // Mappers
    @Bean
    public PolicyMapper policyMapper() { return new PolicyMapper(); }

    @Bean
    public DecisionMapper decisionMapper() { return new DecisionMapper(); }

    @Bean
    public PolicyDtoMapper policyDtoMapper() { return new PolicyDtoMapper(); }

    // Repositories and Stores
    @Bean
    public PolicyRepository policyRepository(PolicyJpaRepository jpaRepository, PolicyMapper mapper) {
        return new PolicyRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public EntityStore<Policy> policyEntityStore(PolicyRepository repository) {
        return new ChenilePolicyEntityStore(repository);
    }

    @Bean
    public DecisionRepository decisionRepository(DecisionJpaRepository jpaRepository, DecisionMapper mapper) {
        return new DecisionRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public PolicyRepositoryAdapter policyRepositoryAdapter(PolicyRepository repository) {
        return new PolicyRepositoryAdapter(repository);
    }

    // Services and Engines
    @Bean
    public RuleEngine policyRuleEngine() {
        return new SpelRuleEngine();
    }

    @Bean
    public ContextEnricher policyContextEnricher() {
        return new ContextEnricherImpl();
    }

    @Bean
    public DecisionService decisionService(PolicyRepository repository, RuleEngine ruleEngine, DecisionRepository decisionRepository) {
        return new DecisionServiceImpl(repository, ruleEngine, decisionRepository);
    }

    @Bean
    public FactDefinitionMapper factDefinitionMapper() {
        return new FactDefinitionMapper();
    }

    @Bean
    public FactDefinitionRepository factDefinitionRepository(FactDefinitionJpaRepository jpaRepository, FactDefinitionMapper mapper) {
        return new FactDefinitionRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public FactMetadataService factMetadataService(FactDefinitionRepository repository) {
        return new FactMetadataServiceImpl(repository);
    }

    @Bean
    public FactDataLoader factDataLoader(FactDefinitionRepository repository) {
        return new FactDataLoader(repository, new com.fasterxml.jackson.databind.ObjectMapper());
    }

    @Bean
    public PolicyService policyService(PolicyRepository repository, HmStateEntityServiceImpl<Policy> stateService, FactMetadataService factMetadataService) {
        return new PolicyServiceImpl(repository, stateService, factMetadataService);
    }
    
    @Bean
    public PolicyDataLoader policyDataLoader(
            PolicyRepository repository) {
        return new PolicyDataLoader(repository, new com.fasterxml.jackson.databind.ObjectMapper());
    }
}
