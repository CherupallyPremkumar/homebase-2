package com.homebase.ecom.offer.service.configuration;

import com.homebase.ecom.offer.api.OfferService;
import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.port.OfferRepository;
import com.homebase.ecom.offer.infrastructure.persistence.ChenileOfferEntityStore;
import com.homebase.ecom.offer.infrastructure.persistence.adapter.OfferJpaRepository;
import com.homebase.ecom.offer.infrastructure.persistence.adapter.OfferRepositoryImpl;
import com.homebase.ecom.offer.infrastructure.persistence.mapper.OfferMapper;
import com.homebase.ecom.offer.service.cmds.DefaultSTMTransitionAction;
import com.homebase.ecom.offer.service.cmds.OfferApproveAction;
import com.homebase.ecom.offer.service.cmds.OfferRejectAction;
import com.homebase.ecom.offer.service.cmds.OfferLifecycleActions;
import com.homebase.ecom.offer.service.impl.OfferServiceImpl;
import com.homebase.ecom.offer.service.postSaveHooks.*;
import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OfferConfiguration {

    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/offer/offer-states.xml";
    public static final String PREFIX_FOR_RESOLVER = "offer";

    // Infrastructure
    @Bean
    public OfferMapper offerMapper() {
        return new OfferMapper();
    }

    @Bean
    public OfferRepository offerRepository(OfferJpaRepository offerJpaRepository, OfferMapper offerMapper) {
        return new OfferRepositoryImpl(offerJpaRepository, offerMapper);
    }

    @Bean
    public EntityStore<Offer> offerEntityStore(OfferRepository offerRepository) {
        return new ChenileOfferEntityStore(offerRepository);
    }

    // STM Infrastructure
    @Bean
    BeanFactoryAdapter offerBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl offerFlowStore(
            @Qualifier("offerBeanFactoryAdapter") BeanFactoryAdapter beanFactoryAdapter) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(beanFactoryAdapter);
        return stmFlowStore;
    }

    @Bean
    STM<Offer> offerEntityStm(@Qualifier("offerFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<Offer> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider offerActionsInfoProvider(@Qualifier("offerFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("offer", provider);
        return provider;
    }

    @Bean
    XmlFlowReader offerFlowReader(@Qualifier("offerFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        return flowReader;
    }

    @Bean
    STMTransitionAction<Offer> defaultOfferSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver offerTransitionActionResolver(
            @Qualifier("defaultOfferSTMTransitionAction") STMTransitionAction<Offer> defaultAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultAction, true);
    }

    @Bean
    StateEntityServiceImpl<Offer> _offerStateEntityService_(
            @Qualifier("offerEntityStm") STM<Offer> stm,
            @Qualifier("offerActionsInfoProvider") STMActionsInfoProvider infoProvider,
            @Qualifier("offerEntityStore") EntityStore<Offer> entityStore) {
        return new StateEntityServiceImpl<>(stm, infoProvider, entityStore);
    }

    // STM Actions
    @Bean
    public OfferApproveAction offerApproveAction() {
        return new OfferApproveAction();
    }

    @Bean
    public OfferRejectAction offerRejectAction() {
        return new OfferRejectAction();
    }

    @Bean
    public OfferLifecycleActions.ActivateAction offerActivateAction() {
        return new OfferLifecycleActions.ActivateAction();
    }

    @Bean
    public OfferLifecycleActions.DeactivateAction offerDeactivateAction() {
        return new OfferLifecycleActions.DeactivateAction();
    }

    @Bean
    public OfferLifecycleActions.ReturnAction offerReturnAction() {
        return new OfferLifecycleActions.ReturnAction();
    }

    @Bean
    public OfferLifecycleActions.ResubmitAction offerResubmitAction() {
        return new OfferLifecycleActions.ResubmitAction();
    }

    // PostSaveHooks

    @Bean
    PENDING_REVIEWOfferPostSaveHook offerPENDING_REVIEWPostSaveHook() {
        return new PENDING_REVIEWOfferPostSaveHook();
    }

    @Bean
    ACTIVEOfferPostSaveHook offerACTIVEPostSaveHook() {
        return new ACTIVEOfferPostSaveHook();
    }

    @Bean
    REJECTEDOfferPostSaveHook offerREJECTEDPostSaveHook() {
        return new REJECTEDOfferPostSaveHook();
    }

    @Bean
    RETURNEDOfferPostSaveHook offerRETURNEDPostSaveHook() {
        return new RETURNEDOfferPostSaveHook();
    }

    @Bean
    INACTIVEOfferPostSaveHook offerINACTIVEPostSaveHook() {
        return new INACTIVEOfferPostSaveHook();
    }

    // STM Entry/Exit/PostSaveHook infrastructure

    @Bean
    DefaultPostSaveHook<Offer> offerDefaultPostSaveHook(
            @Qualifier("offerTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new DefaultPostSaveHook<>(stmTransitionActionResolver);
    }

    @Bean
    GenericEntryAction<Offer> offerEntryAction(@Qualifier("offerEntityStore") EntityStore<Offer> entityStore,
            @Qualifier("offerActionsInfoProvider") STMActionsInfoProvider offerInfoProvider,
            @Qualifier("offerFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("offerDefaultPostSaveHook") DefaultPostSaveHook<Offer> postSaveHook) {
        GenericEntryAction<Offer> entryAction = new GenericEntryAction<>(entityStore, offerInfoProvider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    GenericExitAction<Offer> offerExitAction(@Qualifier("offerFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<Offer> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    // Service
    @Bean
    public OfferService offerService(OfferRepository offerRepository, OfferMapper offerMapper,
                                    @Qualifier("_offerStateEntityService_") StateEntityServiceImpl<Offer> stateEntityService) {
        return new OfferServiceImpl(offerRepository, offerMapper, stateEntityService);
    }
}
