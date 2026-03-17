package com.homebase.ecom.rulesengine.configuration;

import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.service.cmds.*;
import com.homebase.ecom.rulesengine.service.impl.DecisionServiceImpl;
import org.chenile.core.context.ContextContainer;
import com.homebase.ecom.rulesengine.service.impl.RuleSetServiceImpl;
import com.homebase.ecom.rulesengine.service.loader.RuleSetDataLoader;
import com.homebase.ecom.rulesengine.service.postSaveHooks.ACTIVERuleSetPostSaveHook;
import com.homebase.ecom.rulesengine.service.postSaveHooks.DEPRECATEDRuleSetPostSaveHook;
import com.homebase.ecom.rulesengine.service.postSaveHooks.DRAFTRuleSetPostSaveHook;
import com.homebase.ecom.rulesengine.service.postSaveHooks.REVIEWRuleSetPostSaveHook;
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
import com.homebase.ecom.rulesengine.domain.service.*;
import com.homebase.ecom.rulesengine.domain.repository.RuleSetRepository;
import com.homebase.ecom.rulesengine.infrastructure.persistence.ChenileRuleSetEntityStore;
import com.homebase.ecom.rulesengine.infrastructure.persistence.adapter.*;
import com.homebase.ecom.rulesengine.infrastructure.persistence.mapper.*;
import com.homebase.ecom.rulesengine.infrastructure.mapper.*;
import com.homebase.ecom.rulesengine.infrastructure.persistence.repository.*;
import com.homebase.ecom.rulesengine.api.service.RuleSetService;
import com.homebase.ecom.rulesengine.domain.repository.DecisionRepository;
import com.homebase.ecom.rulesengine.infrastructure.persistence.repository.DecisionJpaRepository;
import com.homebase.ecom.rulesengine.infrastructure.persistence.adapter.DecisionRepositoryImpl;
import com.homebase.ecom.rulesengine.infrastructure.persistence.adapter.FactDefinitionJpaRepository;
import com.homebase.ecom.rulesengine.infrastructure.persistence.adapter.FactDefinitionRepositoryImpl;
import com.homebase.ecom.rulesengine.infrastructure.persistence.mapper.FactDefinitionMapper;
import com.homebase.ecom.rulesengine.domain.repository.FactDefinitionRepository;
import com.homebase.ecom.rulesengine.api.service.FactMetadataService;
import com.homebase.ecom.rulesengine.service.impl.FactMetadataServiceImpl;
import com.homebase.ecom.rulesengine.service.loader.FactDataLoader;
import com.homebase.ecom.rulesengine.api.service.DecisionService;
import com.homebase.ecom.rulesengine.infrastructure.engine.SpelRuleEngineAdapter;
import com.homebase.ecom.rulesengine.infrastructure.enricher.ContextEnricherAdapter;
import org.chenile.utils.entity.service.EntityStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RulesEngineConfiguration {

    @Autowired
    private RuleSetJpaRepository ruleSetJpaRepository;

    private static final String FLOW_DEFINITION_FILE = "stm/ruleset-lifecycle.xml";
    public static final String PREFIX_FOR_PROPERTIES = "RuleSet";
    public static final String PREFIX_FOR_RESOLVER = "ruleSet";

    @Bean
    BeanFactoryAdapter ruleSetBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl ruleSetFlowStore(@Qualifier("ruleSetBeanFactoryAdapter") BeanFactoryAdapter adapter) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(adapter);
        return stmFlowStore;
    }

    @Bean
    STM<RuleSet> ruleSetEntityStm(@Qualifier("ruleSetFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMImpl<RuleSet> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider ruleSetActionsInfoProvider(@Qualifier("ruleSetFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("ruleSet", provider);
        return provider;
    }

    @Bean
    HmStateEntityServiceImpl<RuleSet> ruleSetStateEntityService(
            @Qualifier("ruleSetEntityStm") STM<RuleSet> stm,
            @Qualifier("ruleSetActionsInfoProvider") STMActionsInfoProvider infoProvider,
            EntityStore<RuleSet> entityStore) {
        return new HmStateEntityServiceImpl<>(stm, infoProvider, entityStore);
    }

    @Bean
    DefaultPostSaveHook<RuleSet> ruleSetDefaultPostSaveHook(
            @Qualifier("ruleSetTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean
    GenericEntryAction<RuleSet> ruleSetEntryAction(EntityStore<RuleSet> entityStore,
            @Qualifier("ruleSetActionsInfoProvider") STMActionsInfoProvider infoProvider,
            @Qualifier("ruleSetFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("ruleSetDefaultPostSaveHook") DefaultPostSaveHook<RuleSet> postSaveHook) {
        GenericEntryAction<RuleSet> entryAction = new GenericEntryAction<>(entityStore, infoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    DefaultAutomaticStateComputation<RuleSet> ruleSetDefaultAutoState(
            @Qualifier("ruleSetTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("ruleSetFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<RuleSet> autoState = new DefaultAutomaticStateComputation<>(
                stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    GenericExitAction<RuleSet> ruleSetExitAction(@Qualifier("ruleSetFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<RuleSet> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    XmlFlowReader ruleSetFlowReader(@Qualifier("ruleSetFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean
    STMTransitionAction<RuleSet> defaultRuleSetSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver ruleSetTransitionActionResolver(
            @Qualifier("defaultRuleSetSTMTransitionAction") STMTransitionAction<RuleSet> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean
    StmBodyTypeSelector ruleSetBodyTypeSelector(
            @Qualifier("ruleSetActionsInfoProvider") STMActionsInfoProvider infoProvider,
            @Qualifier("ruleSetTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(infoProvider, stmTransitionActionResolver);
    }

    @Bean
    STMTransitionAction<RuleSet> ruleSetBaseTransitionAction(
            @Qualifier("ruleSetTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("ruleSetActivityChecker") ActivityChecker activityChecker,
            @Qualifier("ruleSetFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<RuleSet> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean
    ActivityChecker ruleSetActivityChecker(@Qualifier("ruleSetFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete ruleSetActivitiesCompletionCheck(
            @Qualifier("ruleSetActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    // Individual Actions
    @Bean
    SubmitRuleSetAction ruleSetSubmitAction() { return new SubmitRuleSetAction(); }

    @Bean
    ActivateRuleSetAction ruleSetActivateAction() { return new ActivateRuleSetAction(); }

    @Bean
    ApproveRuleSetAction ruleSetApproveAction() { return new ApproveRuleSetAction(); }

    @Bean
    RevertRuleSetAction ruleSetRevertAction() { return new RevertRuleSetAction(); }

    @Bean
    DeprecateRuleSetAction ruleSetDeprecateAction() { return new DeprecateRuleSetAction(); }

    @Bean
    DeactivateRuleSetAction ruleSetDeactivateAction() { return new DeactivateRuleSetAction(); }

    // Config & Enablement
    @Bean
    ConfigProvider ruleSetConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    public ConfigBasedEnablementStrategy ruleSetConfigBasedEnablementStrategy(
            @Qualifier("ruleSetConfigProvider") ConfigProvider configProvider,
            @Qualifier("ruleSetFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider,
                PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // Post Save Hooks for States
    @Bean
    DRAFTRuleSetPostSaveHook ruleSetDRAFTPostSaveHook() { return new DRAFTRuleSetPostSaveHook(); }

    @Bean
    REVIEWRuleSetPostSaveHook ruleSetREVIEWPostSaveHook() { return new REVIEWRuleSetPostSaveHook(); }

    @Bean
    ACTIVERuleSetPostSaveHook ruleSetACTIVEPostSaveHook() { return new ACTIVERuleSetPostSaveHook(); }

    @Bean
    DEPRECATEDRuleSetPostSaveHook ruleSetDEPRECATEDPostSaveHook() { return new DEPRECATEDRuleSetPostSaveHook(); }

    // Mappers
    @Bean
    public RuleSetMapper ruleSetMapper() { return new RuleSetMapper(); }

    @Bean
    public DecisionMapper decisionMapper() { return new DecisionMapper(); }

    @Bean
    public RuleSetDtoMapper ruleSetDtoMapper() { return new RuleSetDtoMapper(); }

    // Repositories and Stores
    @Bean
    public RuleSetRepository ruleSetRepository(RuleSetJpaRepository jpaRepository, RuleSetMapper mapper) {
        return new RuleSetRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public EntityStore<RuleSet> ruleSetEntityStore(RuleSetRepository repository) {
        return new ChenileRuleSetEntityStore(repository);
    }

    @Bean
    public DecisionRepository decisionRepository(DecisionJpaRepository jpaRepository, DecisionMapper mapper) {
        return new DecisionRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public RuleSetRepositoryAdapter ruleSetRepositoryAdapter(RuleSetRepository repository) {
        return new RuleSetRepositoryAdapter(repository);
    }

    // Services and Engines
    @Bean
    public RuleEngine ruleSetRuleEngine() {
        return new SpelRuleEngineAdapter();
    }

    @Bean
    public ContextEnricher ruleSetContextEnricher() {
        return new ContextEnricherAdapter();
    }

    @Bean
    public DecisionService decisionService(RuleSetRepository repository, RuleEngine ruleEngine, DecisionRepository decisionRepository, ContextContainer contextContainer) {
        return new DecisionServiceImpl(repository, ruleEngine, decisionRepository, contextContainer);
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
    public RuleSetService ruleSetService(RuleSetRepository repository, HmStateEntityServiceImpl<RuleSet> stateService, FactMetadataService factMetadataService) {
        return new RuleSetServiceImpl(repository, stateService, factMetadataService);
    }

    @Bean
    public RuleSetDataLoader ruleSetDataLoader(
            RuleSetRepository repository, ContextContainer contextContainer) {
        return new RuleSetDataLoader(repository, new com.fasterxml.jackson.databind.ObjectMapper(), contextContainer);
    }
}
