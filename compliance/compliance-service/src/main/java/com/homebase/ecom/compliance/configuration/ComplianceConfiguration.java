package com.homebase.ecom.compliance.configuration;

import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.workflow.param.MinimalPayload;
import com.homebase.ecom.compliance.action.DefaultAgreementTransitionAction;
import com.homebase.ecom.compliance.action.DefaultPlatformPolicyTransitionAction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.stmcmds.*;
import com.homebase.ecom.compliance.service.AgreementStateEntityService;
import com.homebase.ecom.compliance.service.PlatformPolicyStateEntityService;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;

import com.homebase.ecom.compliance.model.Agreement;
import com.homebase.ecom.compliance.model.PlatformPolicy;
import com.homebase.ecom.compliance.service.ComplianceValidator;
import com.homebase.ecom.compliance.action.agreement.*;
import com.homebase.ecom.compliance.action.policy.*;
import com.homebase.ecom.compliance.hook.*;
import com.homebase.ecom.compliance.healthcheck.ComplianceHealthChecker;
import com.homebase.ecom.compliance.port.out.NotificationPort;
import com.homebase.ecom.compliance.infrastructure.persistence.ChenileAgreementEntityStore;
import com.homebase.ecom.compliance.infrastructure.persistence.ChenilePlatformPolicyEntityStore;
import com.homebase.ecom.compliance.infrastructure.persistence.repository.AgreementJpaRepository;
import com.homebase.ecom.compliance.infrastructure.persistence.repository.PlatformPolicyJpaRepository;
import com.homebase.ecom.compliance.infrastructure.persistence.repository.AgreementAcceptanceJpaRepository;
import com.homebase.ecom.compliance.infrastructure.persistence.repository.RegulationJpaRepository;
import com.homebase.ecom.compliance.infrastructure.persistence.repository.ComplianceMappingJpaRepository;
import com.homebase.ecom.compliance.infrastructure.persistence.repository.ComplianceAuditJpaRepository;
import com.homebase.ecom.compliance.infrastructure.persistence.mapper.AgreementMapper;
import com.homebase.ecom.compliance.infrastructure.persistence.mapper.PlatformPolicyMapper;
import com.homebase.ecom.compliance.infrastructure.persistence.adapter.*;
import com.homebase.ecom.compliance.infrastructure.integration.LoggingNotificationAdapter;
import com.homebase.ecom.compliance.port.out.*;

@Configuration
public class ComplianceConfiguration {

    private static final String AGREEMENT_FLOW_FILE = "com/homebase/ecom/compliance/agreement-states.xml";
    private static final String POLICY_FLOW_FILE = "com/homebase/ecom/compliance/platform-policy-states.xml";

    // ================================================================
    // Domain Validator
    // ================================================================

    // Compliance cconfig defaults — same values stored in cconfig table for frontend
    private static final int AGREEMENT_MIN_LEAD_DAYS = 7;
    private static final int POLICY_MIN_LEAD_DAYS = 3;
    private static final int AGREEMENT_TITLE_MAX_LENGTH = 500;
    private static final int POLICY_TITLE_MAX_LENGTH = 300;
    private static final int POLICY_SUMMARY_MAX_LENGTH = 2000;

    @Bean
    ComplianceValidator complianceValidator() {
        return new ComplianceValidator(
                AGREEMENT_MIN_LEAD_DAYS,
                POLICY_MIN_LEAD_DAYS,
                AGREEMENT_TITLE_MAX_LENGTH,
                POLICY_TITLE_MAX_LENGTH,
                POLICY_SUMMARY_MAX_LENGTH
        );
    }

    // ================================================================
    // Mappers
    // ================================================================

    @Bean
    AgreementMapper agreementMapper() {
        return new AgreementMapper();
    }

    @Bean
    PlatformPolicyMapper platformPolicyMapper() {
        return new PlatformPolicyMapper();
    }

    // ================================================================
    // Driven Port Adapters
    // ================================================================

    @Bean
    AgreementAcceptanceRepository agreementAcceptanceRepository(AgreementAcceptanceJpaRepository jpaRepo) {
        return new AgreementAcceptanceRepositoryImpl(jpaRepo);
    }

    @Bean
    RegulationRepository regulationRepository(RegulationJpaRepository jpaRepo) {
        return new RegulationRepositoryImpl(jpaRepo);
    }

    @Bean
    ComplianceAuditRepository complianceAuditRepository(ComplianceAuditJpaRepository jpaRepo) {
        return new ComplianceAuditRepositoryImpl(jpaRepo);
    }

    @Bean
    ComplianceMappingRepository complianceMappingRepository(ComplianceMappingJpaRepository jpaRepo) {
        return new ComplianceMappingRepositoryImpl(jpaRepo);
    }

    @Bean
    NotificationPort complianceNotificationPort() {
        return new LoggingNotificationAdapter();
    }

    // ================================================================
    // Entity Stores (Chenile bridge)
    // ================================================================

    @Bean
    EntityStore<Agreement> agreementEntityStore(AgreementJpaRepository jpaRepo, AgreementMapper mapper) {
        return new ChenileAgreementEntityStore(jpaRepo, mapper);
    }

    @Bean
    EntityStore<PlatformPolicy> platformPolicyEntityStore(PlatformPolicyJpaRepository jpaRepo, PlatformPolicyMapper mapper) {
        return new ChenilePlatformPolicyEntityStore(jpaRepo, mapper);
    }

    // ================================================================
    // AGREEMENT STM CHAIN
    // ================================================================

    @Bean
    BeanFactoryAdapter agreementBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl agreementFlowStore(
            @Qualifier("agreementBeanFactoryAdapter") BeanFactoryAdapter bfa) throws Exception {
        STMFlowStoreImpl fs = new STMFlowStoreImpl();
        fs.setBeanFactory(bfa);
        new XmlFlowReader(fs).setFilename(AGREEMENT_FLOW_FILE);
        return fs;
    }

    @Bean
    STM<Agreement> agreementEntityStm(
            @Qualifier("agreementFlowStore") STMFlowStoreImpl fs) throws Exception {
        STMImpl<Agreement> stm = new STMImpl<>();
        stm.setStmFlowStore(fs);
        return stm;
    }

    @Bean
    STMActionsInfoProvider agreementActionsInfoProvider(
            @Qualifier("agreementFlowStore") STMFlowStoreImpl fs) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(fs);
        WorkflowRegistry.addSTMActionsInfoProvider("agreement", provider);
        return provider;
    }

    @Bean
    STMTransitionAction<Agreement> defaultagreementSTMTransitionAction() {
        return new DefaultAgreementTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver agreementTransitionActionResolver(
            @Qualifier("defaultagreementSTMTransitionAction") STMTransitionAction<Agreement> defaultAction) {
        return new STMTransitionActionResolver("agreement", defaultAction, true);
    }

    @Bean
    DefaultPostSaveHook<Agreement> agreementDefaultPostSaveHook(
            @Qualifier("agreementTransitionActionResolver") STMTransitionActionResolver resolver) {
        return new DefaultPostSaveHook<>(resolver);
    }

    @Bean
    GenericEntryAction<Agreement> agreementEntryAction(
            @Qualifier("agreementEntityStore") EntityStore<Agreement> es,
            @Qualifier("agreementActionsInfoProvider") STMActionsInfoProvider p,
            @Qualifier("agreementFlowStore") STMFlowStoreImpl fs,
            @Qualifier("agreementDefaultPostSaveHook") DefaultPostSaveHook<Agreement> psh) {
        GenericEntryAction<Agreement> entryAction = new GenericEntryAction<>(es, p, psh);
        fs.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    GenericExitAction<Agreement> agreementExitAction(
            @Qualifier("agreementFlowStore") STMFlowStoreImpl fs) {
        GenericExitAction<Agreement> exitAction = new GenericExitAction<>();
        fs.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    DefaultAutomaticStateComputation<Agreement> agreementDefaultAutoState(
            @Qualifier("agreementTransitionActionResolver") STMTransitionActionResolver resolver,
            @Qualifier("agreementFlowStore") STMFlowStoreImpl fs) {
        DefaultAutomaticStateComputation<Agreement> autoState = new DefaultAutomaticStateComputation<>(resolver);
        fs.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    BaseTransitionAction<Agreement> agreementBaseTransitionAction(
            @Qualifier("agreementTransitionActionResolver") STMTransitionActionResolver resolver,
            @Qualifier("agreementActivityChecker") ActivityChecker checker,
            @Qualifier("agreementFlowStore") STMFlowStoreImpl fs) {
        BaseTransitionAction<Agreement> bta = new BaseTransitionAction<>(resolver);
        bta.activityChecker = checker;
        fs.setDefaultTransitionAction(bta);
        return bta;
    }

    @Bean
    ActivityChecker agreementActivityChecker(@Qualifier("agreementFlowStore") STMFlowStoreImpl fs) {
        return new ActivityChecker(fs);
    }

    @Bean
    AreActivitiesComplete agreementActivitiesCompletionCheck(
            @Qualifier("agreementActivityChecker") ActivityChecker checker) {
        return new AreActivitiesComplete(checker);
    }

    @Bean
    ConfigProviderImpl agreementConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    ConfigBasedEnablementStrategy agreementEnablementStrategy(
            @Qualifier("agreementConfigProvider") ConfigProvider configProvider,
            @Qualifier("agreementFlowStore") STMFlowStoreImpl fs) {
        ConfigBasedEnablementStrategy strategy = new ConfigBasedEnablementStrategy(configProvider, "Agreement");
        fs.setEnablementStrategy(strategy);
        return strategy;
    }

    @Bean
    AgreementStateEntityService _agreementStateEntityService_(
            @Qualifier("agreementEntityStm") STM<Agreement> stm,
            @Qualifier("agreementActionsInfoProvider") STMActionsInfoProvider p,
            @Qualifier("agreementEntityStore") EntityStore<Agreement> es,
            ComplianceValidator validator) {
        return new AgreementStateEntityService(stm, p, es, validator);
    }

    @Bean
    StmBodyTypeSelector agreementBodyTypeSelector(
            @Qualifier("agreementActionsInfoProvider") STMActionsInfoProvider p,
            @Qualifier("agreementTransitionActionResolver") STMTransitionActionResolver r) {
        return new StmBodyTypeSelector(p, r);
    }

    @Bean
    java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> agreementEventAuthoritiesSupplier(
            @Qualifier("agreementActionsInfoProvider") STMActionsInfoProvider p) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(p, false);
        return builder.build();
    }

    // ================================================================
    // Agreement Transition Actions
    // ================================================================

    @Bean
    AgreementPublishAction agreementPublishAction(ComplianceValidator validator) {
        return new AgreementPublishAction(validator);
    }

    @Bean
    AgreementSupersedeAction agreementSupersedeAction(ComplianceValidator validator) {
        return new AgreementSupersedeAction(validator);
    }

    @Bean
    AgreementSuspendAction agreementSuspendAction(ComplianceValidator validator) {
        return new AgreementSuspendAction(validator);
    }

    @Bean
    AgreementReinstateAction agreementReinstateAction(ComplianceValidator validator) {
        return new AgreementReinstateAction(validator);
    }

    @Bean
    AgreementRetireAction agreementRetireAction(ComplianceValidator validator) {
        return new AgreementRetireAction(validator);
    }

    // ================================================================
    // Agreement Post-Save Hooks
    // ================================================================

    @Bean
    AgreementPUBLISHEDPostSaveHook agreementPUBLISHEDPostSaveHook(NotificationPort complianceNotificationPort) {
        return new AgreementPUBLISHEDPostSaveHook(complianceNotificationPort);
    }

    // ================================================================
    // PLATFORM POLICY STM CHAIN
    // ================================================================

    @Bean
    BeanFactoryAdapter platformPolicyBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl platformPolicyFlowStore(
            @Qualifier("platformPolicyBeanFactoryAdapter") BeanFactoryAdapter bfa) throws Exception {
        STMFlowStoreImpl fs = new STMFlowStoreImpl();
        fs.setBeanFactory(bfa);
        new XmlFlowReader(fs).setFilename(POLICY_FLOW_FILE);
        return fs;
    }

    @Bean
    STM<PlatformPolicy> platformPolicyEntityStm(
            @Qualifier("platformPolicyFlowStore") STMFlowStoreImpl fs) throws Exception {
        STMImpl<PlatformPolicy> stm = new STMImpl<>();
        stm.setStmFlowStore(fs);
        return stm;
    }

    @Bean
    STMActionsInfoProvider platformPolicyActionsInfoProvider(
            @Qualifier("platformPolicyFlowStore") STMFlowStoreImpl fs) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(fs);
        WorkflowRegistry.addSTMActionsInfoProvider("platformPolicy", provider);
        return provider;
    }

    @Bean
    STMTransitionAction<PlatformPolicy> defaultplatformPolicySTMTransitionAction() {
        return new DefaultPlatformPolicyTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver platformPolicyTransitionActionResolver(
            @Qualifier("defaultplatformPolicySTMTransitionAction") STMTransitionAction<PlatformPolicy> defaultAction) {
        return new STMTransitionActionResolver("platformPolicy", defaultAction, true);
    }

    @Bean
    DefaultPostSaveHook<PlatformPolicy> platformPolicyDefaultPostSaveHook(
            @Qualifier("platformPolicyTransitionActionResolver") STMTransitionActionResolver resolver) {
        return new DefaultPostSaveHook<>(resolver);
    }

    @Bean
    GenericEntryAction<PlatformPolicy> platformPolicyEntryAction(
            @Qualifier("platformPolicyEntityStore") EntityStore<PlatformPolicy> es,
            @Qualifier("platformPolicyActionsInfoProvider") STMActionsInfoProvider p,
            @Qualifier("platformPolicyFlowStore") STMFlowStoreImpl fs,
            @Qualifier("platformPolicyDefaultPostSaveHook") DefaultPostSaveHook<PlatformPolicy> psh) {
        GenericEntryAction<PlatformPolicy> entryAction = new GenericEntryAction<>(es, p, psh);
        fs.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    GenericExitAction<PlatformPolicy> platformPolicyExitAction(
            @Qualifier("platformPolicyFlowStore") STMFlowStoreImpl fs) {
        GenericExitAction<PlatformPolicy> exitAction = new GenericExitAction<>();
        fs.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    DefaultAutomaticStateComputation<PlatformPolicy> platformPolicyDefaultAutoState(
            @Qualifier("platformPolicyTransitionActionResolver") STMTransitionActionResolver resolver,
            @Qualifier("platformPolicyFlowStore") STMFlowStoreImpl fs) {
        DefaultAutomaticStateComputation<PlatformPolicy> autoState = new DefaultAutomaticStateComputation<>(resolver);
        fs.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean
    BaseTransitionAction<PlatformPolicy> platformPolicyBaseTransitionAction(
            @Qualifier("platformPolicyTransitionActionResolver") STMTransitionActionResolver resolver,
            @Qualifier("platformPolicyActivityChecker") ActivityChecker checker,
            @Qualifier("platformPolicyFlowStore") STMFlowStoreImpl fs) {
        BaseTransitionAction<PlatformPolicy> bta = new BaseTransitionAction<>(resolver);
        bta.activityChecker = checker;
        fs.setDefaultTransitionAction(bta);
        return bta;
    }

    @Bean
    ActivityChecker platformPolicyActivityChecker(@Qualifier("platformPolicyFlowStore") STMFlowStoreImpl fs) {
        return new ActivityChecker(fs);
    }

    @Bean
    AreActivitiesComplete platformPolicyActivitiesCompletionCheck(
            @Qualifier("platformPolicyActivityChecker") ActivityChecker checker) {
        return new AreActivitiesComplete(checker);
    }

    @Bean
    ConfigProviderImpl platformPolicyConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    ConfigBasedEnablementStrategy platformPolicyEnablementStrategy(
            @Qualifier("platformPolicyConfigProvider") ConfigProvider configProvider,
            @Qualifier("platformPolicyFlowStore") STMFlowStoreImpl fs) {
        ConfigBasedEnablementStrategy strategy = new ConfigBasedEnablementStrategy(configProvider, "PlatformPolicy");
        fs.setEnablementStrategy(strategy);
        return strategy;
    }

    @Bean
    PlatformPolicyStateEntityService _platformPolicyStateEntityService_(
            @Qualifier("platformPolicyEntityStm") STM<PlatformPolicy> stm,
            @Qualifier("platformPolicyActionsInfoProvider") STMActionsInfoProvider p,
            @Qualifier("platformPolicyEntityStore") EntityStore<PlatformPolicy> es,
            ComplianceValidator validator) {
        return new PlatformPolicyStateEntityService(stm, p, es, validator);
    }

    @Bean
    StmBodyTypeSelector platformPolicyBodyTypeSelector(
            @Qualifier("platformPolicyActionsInfoProvider") STMActionsInfoProvider p,
            @Qualifier("platformPolicyTransitionActionResolver") STMTransitionActionResolver r) {
        return new StmBodyTypeSelector(p, r);
    }

    @Bean
    java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> platformPolicyEventAuthoritiesSupplier(
            @Qualifier("platformPolicyActionsInfoProvider") STMActionsInfoProvider p) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(p, false);
        return builder.build();
    }

    // ================================================================
    // Platform Policy Transition Actions
    // ================================================================

    @Bean
    PolicyPublishAction platformPolicyPublishAction(ComplianceValidator validator) {
        return new PolicyPublishAction(validator);
    }

    @Bean
    PolicyAmendAction platformPolicyAmendAction(ComplianceValidator validator) {
        return new PolicyAmendAction(validator);
    }

    @Bean
    PolicySuspendAction platformPolicySuspendAction(ComplianceValidator validator) {
        return new PolicySuspendAction(validator);
    }

    @Bean
    PolicyRetireAction platformPolicyRetireAction(ComplianceValidator validator) {
        return new PolicyRetireAction(validator);
    }

    // ================================================================
    // Platform Policy Post-Save Hooks
    // ================================================================

    @Bean
    PolicyPUBLISHEDPostSaveHook platformPolicyPUBLISHEDPostSaveHook(NotificationPort complianceNotificationPort) {
        return new PolicyPUBLISHEDPostSaveHook(complianceNotificationPort);
    }

    // ================================================================
    // Health Checker
    // ================================================================

    @Bean
    ComplianceHealthChecker complianceHealthChecker() {
        return new ComplianceHealthChecker();
    }
}
