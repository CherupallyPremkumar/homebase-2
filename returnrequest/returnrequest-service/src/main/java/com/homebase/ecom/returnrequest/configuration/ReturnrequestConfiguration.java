package com.homebase.ecom.returnrequest.configuration;

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
import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.service.cmds.*;
import com.homebase.ecom.returnrequest.service.event.ReturnRequestEventHandler;
import com.homebase.ecom.returnrequest.service.healthcheck.ReturnrequestHealthChecker;
import com.homebase.ecom.returnrequest.infrastructure.persistence.ChenileReturnrequestEntityStore;
import com.homebase.ecom.returnrequest.infrastructure.persistence.adapter.ReturnrequestJpaRepository;
import com.homebase.ecom.returnrequest.infrastructure.persistence.mapper.ReturnrequestMapper;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.stm.State;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import com.homebase.ecom.returnrequest.service.postSaveHooks.*;
import com.homebase.ecom.returnrequest.service.validator.ReturnRequestPolicyValidator;

@Configuration
public class ReturnrequestConfiguration {
    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/returnrequest/returnrequest-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Returnrequest";
    public static final String PREFIX_FOR_RESOLVER = "returnrequest";

    @Bean BeanFactoryAdapter returnrequestBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean STMFlowStoreImpl returnrequestFlowStore(
            @Qualifier("returnrequestBeanFactoryAdapter") BeanFactoryAdapter returnrequestBeanFactoryAdapter
            ) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(returnrequestBeanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean STM<Returnrequest> returnrequestEntityStm(@Qualifier("returnrequestFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<Returnrequest> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean STMActionsInfoProvider returnrequestActionsInfoProvider(@Qualifier("returnrequestFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("returnrequest", provider);
        return provider;
    }

    @Bean ReturnrequestMapper returnrequestMapper() {
        return new ReturnrequestMapper();
    }

    @Bean EntityStore<Returnrequest> returnrequestEntityStore(ReturnrequestJpaRepository jpaRepository, ReturnrequestMapper mapper) {
        return new ChenileReturnrequestEntityStore(jpaRepository, mapper);
    }

    @Bean StateEntityServiceImpl<Returnrequest> _returnrequestStateEntityService_(
            @Qualifier("returnrequestEntityStm") STM<Returnrequest> stm,
            @Qualifier("returnrequestActionsInfoProvider") STMActionsInfoProvider returnrequestInfoProvider,
            @Qualifier("returnrequestEntityStore") EntityStore<Returnrequest> entityStore) {
        return new HmStateEntityServiceImpl<>(stm, returnrequestInfoProvider, entityStore);
    }

    // OGNL scripting for auto-states
    @Bean OgnlScriptingStrategy ognlScriptingStrategy() {
        return new OgnlScriptingStrategy();
    }

    @Bean IfAction ifAction() {
        return new IfAction();
    }

    // PostSaveHook infrastructure
    @Bean DefaultPostSaveHook<Returnrequest> returnrequestDefaultPostSaveHook(
            @Qualifier("returnrequestTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean GenericEntryAction<Returnrequest> returnrequestEntryAction(
            @Qualifier("returnrequestEntityStore") EntityStore<Returnrequest> entityStore,
            @Qualifier("returnrequestActionsInfoProvider") STMActionsInfoProvider returnrequestInfoProvider,
            @Qualifier("returnrequestFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("returnrequestDefaultPostSaveHook") DefaultPostSaveHook<Returnrequest> postSaveHook) {
        GenericEntryAction<Returnrequest> entryAction = new GenericEntryAction<>(entityStore, returnrequestInfoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean DefaultAutomaticStateComputation<Returnrequest> returnrequestDefaultAutoState(
            @Qualifier("returnrequestTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("returnrequestFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<Returnrequest> autoState = new DefaultAutomaticStateComputation<>(stmTransitionActionResolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    @Bean GenericExitAction<Returnrequest> returnrequestExitAction(@Qualifier("returnrequestFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<Returnrequest> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean XmlFlowReader returnrequestFlowReader(@Qualifier("returnrequestFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean ReturnrequestHealthChecker returnrequestHealthChecker() {
        return new ReturnrequestHealthChecker();
    }

    @Bean STMTransitionAction<Returnrequest> defaultreturnrequestSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean STMTransitionActionResolver returnrequestTransitionActionResolver(
            @Qualifier("defaultreturnrequestSTMTransitionAction") STMTransitionAction<Returnrequest> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction, true);
    }

    @Bean StmBodyTypeSelector returnrequestBodyTypeSelector(
            @Qualifier("returnrequestActionsInfoProvider") STMActionsInfoProvider returnrequestInfoProvider,
            @Qualifier("returnrequestTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(returnrequestInfoProvider, stmTransitionActionResolver);
    }

    @Bean STMTransitionAction<Returnrequest> returnrequestBaseTransitionAction(
            @Qualifier("returnrequestTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("returnrequestActivityChecker") ActivityChecker activityChecker,
            @Qualifier("returnrequestFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<Returnrequest> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean ActivityChecker returnrequestActivityChecker(@Qualifier("returnrequestFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean AreActivitiesComplete activitiesCompletionCheck(@Qualifier("returnrequestActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    // ================================================================
    // STM Transition Actions (convention: "returnrequest" + eventId + "Action")
    // ================================================================

    @Bean ReviewReturnReturnrequestAction returnrequestReviewReturnAction() {
        return new ReviewReturnReturnrequestAction();
    }

    @Bean ApproveReturnReturnrequestAction returnrequestApproveReturnAction() {
        return new ApproveReturnReturnrequestAction();
    }

    @Bean RejectReturnReturnrequestAction returnrequestRejectReturnAction() {
        return new RejectReturnReturnrequestAction();
    }

    @Bean PartialApproveReturnrequestAction returnrequestPartialApproveAction() {
        return new PartialApproveReturnrequestAction();
    }

    @Bean ReceiveItemReturnrequestAction returnrequestReceiveItemAction() {
        return new ReceiveItemReturnrequestAction();
    }

    @Bean InspectItemReturnrequestAction returnrequestInspectItemAction() {
        return new InspectItemReturnrequestAction();
    }

    @Bean InitiateRefundReturnrequestAction returnrequestInitiateRefundAction() {
        return new InitiateRefundReturnrequestAction();
    }

    @Bean CompletedExitAction returnrequestCompletedExitAction() {
        return new CompletedExitAction();
    }

    @Bean EscalateReturnrequestAction returnrequestEscalateAction() {
        return new EscalateReturnrequestAction();
    }

    @Bean CompleteReturnReturnrequestAction returnrequestCompleteReturnAction() {
        return new CompleteReturnReturnrequestAction();
    }

    // ================================================================
    // Config and Enablement
    // ================================================================

    @Bean ConfigProviderImpl returnrequestConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean ConfigBasedEnablementStrategy returnrequestConfigBasedEnablementStrategy(
            @Qualifier("returnrequestConfigProvider") ConfigProvider configProvider,
            @Qualifier("returnrequestFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider, PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }

    // ================================================================
    // PostSaveHooks (convention: "returnrequest" + STATE + "PostSaveHook")
    // ================================================================

    @Bean REQUESTEDReturnrequestPostSaveHook returnrequestREQUESTEDPostSaveHook() {
        return new REQUESTEDReturnrequestPostSaveHook();
    }

    @Bean UNDER_REVIEWReturnrequestPostSaveHook returnrequestUNDER_REVIEWPostSaveHook() {
        return new UNDER_REVIEWReturnrequestPostSaveHook();
    }

    @Bean APPROVEDReturnrequestPostSaveHook returnrequestAPPROVEDPostSaveHook() {
        return new APPROVEDReturnrequestPostSaveHook();
    }

    @Bean REJECTEDReturnrequestPostSaveHook returnrequestREJECTEDPostSaveHook() {
        return new REJECTEDReturnrequestPostSaveHook();
    }

    @Bean ITEM_RECEIVEDReturnrequestPostSaveHook returnrequestITEM_RECEIVEDPostSaveHook() {
        return new ITEM_RECEIVEDReturnrequestPostSaveHook();
    }

    @Bean INSPECTEDReturnrequestPostSaveHook returnrequestINSPECTEDPostSaveHook() {
        return new INSPECTEDReturnrequestPostSaveHook();
    }

    @Bean REFUND_INITIATEDReturnrequestPostSaveHook returnrequestREFUND_INITIATEDPostSaveHook() {
        return new REFUND_INITIATEDReturnrequestPostSaveHook();
    }

    @Bean COMPLETEDReturnrequestPostSaveHook returnrequestCOMPLETEDPostSaveHook() {
        return new COMPLETEDReturnrequestPostSaveHook();
    }

    // ================================================================
    // Security: STM ACL authorities builder
    // ================================================================

    @Bean java.util.function.Function<org.chenile.core.context.ChenileExchange, String[]> returnrequestEventAuthoritiesSupplier(
            @Qualifier("returnrequestActionsInfoProvider") STMActionsInfoProvider returnrequestInfoProvider) throws Exception {
        StmAuthoritiesBuilder builder = new StmAuthoritiesBuilder(returnrequestInfoProvider, false);
        return builder.build();
    }

    // ================================================================
    // Policy Validator
    // ================================================================

    @Bean ReturnRequestPolicyValidator returnRequestPolicyValidator() {
        return new ReturnRequestPolicyValidator();
    }

    // ================================================================
    // Hexagonal Ports / Adapters
    // Wired in ReturnrequestInfrastructureConfiguration (returnrequest-infrastructure module)
    // ================================================================

    // ================================================================
    // Chenile-Kafka Event Handler (Item 10, 14)
    // ================================================================

    @Bean("returnrequestEventService")
    ReturnRequestEventHandler returnrequestEventService() {
        return new ReturnRequestEventHandler();
    }
}
