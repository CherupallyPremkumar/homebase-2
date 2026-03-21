package com.homebase.ecom.demoorder.configuration;

import com.homebase.ecom.demoorder.model.DemoOrder;
import com.homebase.ecom.demoorder.port.DemoOrderEventPublisherPort;
import com.homebase.ecom.demoorder.service.cmds.DemoDefaultTransitionAction;
import com.homebase.ecom.demoorder.service.postSaveHooks.PROCESSEDDemoOrderPostSaveHook;
import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.impl.HmStateEntityServiceImpl;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * STM wiring for DemoOrder -- follows the Chenile hexagonal pattern exactly.
 * All beans via @Bean, no @Component/@Service/@Repository.
 */
@Configuration
public class DemoOrderConfiguration {

    private static final String ORDER_FLOW_FILE = "com/homebase/ecom/demoorder/demo-order-states.xml";
    private static final String ORDER_PREFIX = "demoOrder";

    // ================================================================
    // STM Core Beans
    // ================================================================

    @Bean
    BeanFactoryAdapter demoOrderBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl demoOrderFlowStore(
            @Qualifier("demoOrderBeanFactoryAdapter") BeanFactoryAdapter bfa) {
        STMFlowStoreImpl fs = new STMFlowStoreImpl();
        fs.setBeanFactory(bfa);
        return fs;
    }

    @Bean
    STM<DemoOrder> demoOrderEntityStm(
            @Qualifier("demoOrderFlowStore") STMFlowStoreImpl fs) {
        STMImpl<DemoOrder> stm = new STMImpl<>();
        stm.setStmFlowStore(fs);
        return stm;
    }

    @Bean
    STMActionsInfoProvider demoOrderActionsInfoProvider(
            @Qualifier("demoOrderFlowStore") STMFlowStoreImpl fs) {
        return new STMActionsInfoProvider(fs);
    }

    @Bean
    STMTransitionAction<DemoOrder> defaultdemoOrderSTMTransitionAction() {
        return new DemoDefaultTransitionAction<>();
    }

    @Bean
    STMTransitionActionResolver demoOrderTransitionActionResolver(
            @Qualifier("defaultdemoOrderSTMTransitionAction") STMTransitionAction<DemoOrder> defaultAction) {
        return new STMTransitionActionResolver(ORDER_PREFIX, defaultAction, true);
    }

    @Bean
    DefaultPostSaveHook<DemoOrder> demoOrderDefaultPostSaveHook(
            @Qualifier("demoOrderTransitionActionResolver") STMTransitionActionResolver resolver) {
        return new DefaultPostSaveHook<>(resolver);
    }

    @Bean
    GenericEntryAction<DemoOrder> demoOrderEntryAction(
            @Qualifier("demoOrderEntityStore") EntityStore<DemoOrder> es,
            @Qualifier("demoOrderActionsInfoProvider") STMActionsInfoProvider provider,
            @Qualifier("demoOrderFlowStore") STMFlowStoreImpl fs,
            @Qualifier("demoOrderDefaultPostSaveHook") DefaultPostSaveHook<DemoOrder> psh) {
        GenericEntryAction<DemoOrder> entryAction = new GenericEntryAction<>(es, provider, psh);
        fs.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    GenericExitAction<DemoOrder> demoOrderExitAction(
            @Qualifier("demoOrderFlowStore") STMFlowStoreImpl fs) {
        GenericExitAction<DemoOrder> exitAction = new GenericExitAction<>();
        fs.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    STMTransitionAction<DemoOrder> demoOrderBaseTransitionAction(
            @Qualifier("demoOrderTransitionActionResolver") STMTransitionActionResolver resolver,
            @Qualifier("demoOrderFlowStore") STMFlowStoreImpl fs) {
        BaseTransitionAction<DemoOrder> baseAction = new BaseTransitionAction<>(resolver);
        fs.setDefaultTransitionAction(baseAction);
        return baseAction;
    }

    @Bean
    XmlFlowReader demoOrderFlowReader(
            @Qualifier("demoOrderFlowStore") STMFlowStoreImpl fs) throws Exception {
        XmlFlowReader reader = new XmlFlowReader(fs);
        reader.setFilename(ORDER_FLOW_FILE);
        return reader;
    }

    // ================================================================
    // StateEntityService
    // ================================================================

    @Bean
    StateEntityServiceImpl<DemoOrder> _demoOrderStateEntityService_(
            @Qualifier("demoOrderEntityStm") STM<DemoOrder> stm,
            @Qualifier("demoOrderActionsInfoProvider") STMActionsInfoProvider provider,
            @Qualifier("demoOrderEntityStore") EntityStore<DemoOrder> es) {
        return new HmStateEntityServiceImpl<>(stm, provider, es);
    }

    @Bean
    StmBodyTypeSelector demoOrderBodyTypeSelector(
            @Qualifier("demoOrderActionsInfoProvider") STMActionsInfoProvider provider,
            @Qualifier("demoOrderTransitionActionResolver") STMTransitionActionResolver resolver) {
        return new StmBodyTypeSelector(provider, resolver);
    }

    // ================================================================
    // Post-Save Hooks
    // ================================================================

    @Bean
    PROCESSEDDemoOrderPostSaveHook demoOrderPROCESSEDPostSaveHook(
            DemoOrderEventPublisherPort eventPublisher) {
        return new PROCESSEDDemoOrderPostSaveHook(eventPublisher);
    }
}
