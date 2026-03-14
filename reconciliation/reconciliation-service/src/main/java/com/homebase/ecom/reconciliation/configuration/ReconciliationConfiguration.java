package com.homebase.ecom.reconciliation.configuration;

import com.homebase.ecom.reconciliation.service.cmds.*;
import org.chenile.owiz.OrchExecutor;
import org.chenile.owiz.config.impl.XmlOrchConfigurator;
import org.chenile.owiz.impl.OrchExecutorImpl;
import org.chenile.owiz.impl.OwizSpringFactoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for the reconciliation OWIZ orchestration flow.
 * Wires the XmlOrchConfigurator with the flow XML and registers all chain commands.
 */
@Configuration
public class ReconciliationConfiguration {

    private static final String FLOW_DEFINITION_FILE =
            "com/homebase/ecom/reconciliation/reconciliation-flow.xml";

    @Bean
    OwizSpringFactoryAdapter reconciliationBeanFactory() {
        return new OwizSpringFactoryAdapter();
    }

    @Bean
    XmlOrchConfigurator<ReconciliationContext> reconciliationOrchConfigurator(
            OwizSpringFactoryAdapter reconciliationBeanFactory) throws Exception {
        XmlOrchConfigurator<ReconciliationContext> configurator = new XmlOrchConfigurator<>();
        configurator.setBeanFactoryAdapter(reconciliationBeanFactory);
        configurator.setFilename(FLOW_DEFINITION_FILE);
        return configurator;
    }

    @Bean
    OrchExecutor<ReconciliationContext> reconciliationOrchExecutor(
            XmlOrchConfigurator<ReconciliationContext> reconciliationOrchConfigurator) {
        OrchExecutorImpl<ReconciliationContext> executor = new OrchExecutorImpl<>();
        executor.setOrchConfigurator(reconciliationOrchConfigurator);
        return executor;
    }

    // Chain command beans - names must match componentName in reconciliation-flow.xml

    @Bean
    FetchGatewayTransactions fetchGatewayTransactions() {
        return new FetchGatewayTransactions();
    }

    @Bean
    FetchSystemTransactions fetchSystemTransactions() {
        return new FetchSystemTransactions();
    }

    @Bean
    MatchTransactions matchTransactions() {
        return new MatchTransactions();
    }

    @Bean
    FlagMismatches flagMismatches() {
        return new FlagMismatches();
    }

    @Bean
    AutoResolveCommand autoResolveCommand() {
        return new AutoResolveCommand();
    }

    @Bean
    GenerateReconciliationReport generateReconciliationReport() {
        return new GenerateReconciliationReport();
    }
}
