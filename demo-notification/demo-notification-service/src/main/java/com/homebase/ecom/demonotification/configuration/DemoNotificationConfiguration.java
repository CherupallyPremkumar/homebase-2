package com.homebase.ecom.demonotification.configuration;

import com.homebase.ecom.demonotification.model.DemoNotification;
import com.homebase.ecom.demonotification.service.DemoNotificationService;
import com.homebase.ecom.demonotification.service.cmds.DemoDefaultTransitionAction;
import com.homebase.ecom.demonotification.service.impl.DemoNotificationServiceImpl;
import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.stmcmds.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

/**
 * STM wiring for DemoNotification -- follows the Chenile hexagonal pattern exactly.
 * This module has ZERO compile-time dependency on demo-order.
 * Cross-module communication happens only via @EventsSubscribedTo on the controller.
 */
@Configuration
public class DemoNotificationConfiguration {

    private static final String FLOW_FILE = "com/homebase/ecom/demonotification/demo-notification-states.xml";
    private static final String PREFIX = "demoNotification";

    // ================================================================
    // STM Core Beans
    // ================================================================

    @Bean
    BeanFactoryAdapter demoNotifBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl demoNotifFlowStore(
            @Qualifier("demoNotifBeanFactoryAdapter") BeanFactoryAdapter bfa) {
        STMFlowStoreImpl fs = new STMFlowStoreImpl();
        fs.setBeanFactory(bfa);
        return fs;
    }

    @Bean
    STM<DemoNotification> demoNotifEntityStm(
            @Qualifier("demoNotifFlowStore") STMFlowStoreImpl fs) {
        STMImpl<DemoNotification> stm = new STMImpl<>();
        stm.setStmFlowStore(fs);
        return stm;
    }

    @Bean
    STMActionsInfoProvider demoNotifActionsInfoProvider(
            @Qualifier("demoNotifFlowStore") STMFlowStoreImpl fs) {
        return new STMActionsInfoProvider(fs);
    }

    @Bean
    STMTransitionAction<DemoNotification> defaultdemoNotificationSTMTransitionAction() {
        return new DemoDefaultTransitionAction<>();
    }

    @Bean
    STMTransitionActionResolver demoNotifTransitionActionResolver(
            @Qualifier("defaultdemoNotificationSTMTransitionAction") STMTransitionAction<DemoNotification> defaultAction) {
        return new STMTransitionActionResolver(PREFIX, defaultAction, true);
    }

    @Bean
    DefaultPostSaveHook<DemoNotification> demoNotifDefaultPostSaveHook(
            @Qualifier("demoNotifTransitionActionResolver") STMTransitionActionResolver resolver) {
        return new DefaultPostSaveHook<>(resolver);
    }

    @Bean
    GenericEntryAction<DemoNotification> demoNotifEntryAction(
            @Qualifier("demoNotifEntityStore") EntityStore<DemoNotification> es,
            @Qualifier("demoNotifActionsInfoProvider") STMActionsInfoProvider provider,
            @Qualifier("demoNotifFlowStore") STMFlowStoreImpl fs,
            @Qualifier("demoNotifDefaultPostSaveHook") DefaultPostSaveHook<DemoNotification> psh) {
        GenericEntryAction<DemoNotification> entryAction = new GenericEntryAction<>(es, provider, psh);
        fs.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    GenericExitAction<DemoNotification> demoNotifExitAction(
            @Qualifier("demoNotifFlowStore") STMFlowStoreImpl fs) {
        GenericExitAction<DemoNotification> exitAction = new GenericExitAction<>();
        fs.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    STMTransitionAction<DemoNotification> demoNotifBaseTransitionAction(
            @Qualifier("demoNotifTransitionActionResolver") STMTransitionActionResolver resolver,
            @Qualifier("demoNotifFlowStore") STMFlowStoreImpl fs) {
        BaseTransitionAction<DemoNotification> baseAction = new BaseTransitionAction<>(resolver);
        fs.setDefaultTransitionAction(baseAction);
        return baseAction;
    }

    @Bean
    XmlFlowReader demoNotifFlowReader(
            @Qualifier("demoNotifFlowStore") STMFlowStoreImpl fs) throws Exception {
        XmlFlowReader reader = new XmlFlowReader(fs);
        reader.setFilename(FLOW_FILE);
        return reader;
    }

    // ================================================================
    // DemoNotification Service
    // ================================================================

    @Bean
    DemoNotificationServiceImpl _demoNotifStateEntityService_(
            @Qualifier("demoNotifEntityStm") STM<DemoNotification> stm,
            @Qualifier("demoNotifActionsInfoProvider") STMActionsInfoProvider provider,
            @Qualifier("demoNotifEntityStore") EntityStore<DemoNotification> es,
            ObjectMapper objectMapper) {
        return new DemoNotificationServiceImpl(stm, provider, es, objectMapper);
    }

    @Bean
    StmBodyTypeSelector demoNotifBodyTypeSelector(
            @Qualifier("demoNotifActionsInfoProvider") STMActionsInfoProvider provider,
            @Qualifier("demoNotifTransitionActionResolver") STMTransitionActionResolver resolver) {
        return new StmBodyTypeSelector(provider, resolver);
    }
}
