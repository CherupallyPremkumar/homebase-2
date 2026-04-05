package com.homebase.ecom.query.stm;

import org.chenile.stm.impl.STMActionsInfoProvider;
import org.chenile.stm.impl.STMFlowStoreImpl;
import org.chenile.stm.impl.XmlFlowReader;
import org.chenile.workflow.api.WorkflowRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * Loads stripped STM XMLs (metadata only — no componentName, no auto-states, no conditions)
 * and registers each flow's {@link STMActionsInfoProvider} into {@link WorkflowRegistry}.
 * <p>
 * This enables query-build standalone mode to know which events are allowed per state,
 * their ACLs, and body types — without requiring the service-layer action beans.
 */
@Configuration
public class QueryStmConfiguration {

    private static final Logger log = LoggerFactory.getLogger(QueryStmConfiguration.class);

    /**
     * Each entry: { registryName, classpathXmlFile }
     * <p>
     * Registry names match those used by each BC's Configuration class when calling
     * {@code WorkflowRegistry.addSTMActionsInfoProvider(name, provider)}.
     */
    private static final String PREFIX = "com/homebase/ecom/query/stm/";

    private static final String[][] FLOW_DEFINITIONS = {
        { "cart",               PREFIX + "cart/cart-states.xml" },
        { "user",               PREFIX + "user/user-states.xml" },
        { "product",            PREFIX + "product/product-states.xml" },
        { "inventory",          PREFIX + "inventory/inventory-states.xml" },
        { "offer",              PREFIX + "offer/offer-states.xml" },
        { "promo",              PREFIX + "promo/promo-states.xml" },
        { "checkout",           PREFIX + "checkout/checkout-states.xml" },
        { "order",              PREFIX + "order/order-states.xml" },
        { "payment",            PREFIX + "payment/payment-states.xml" },
        { "shipping",           PREFIX + "shipping/shipping-states.xml" },
        { "notification",       PREFIX + "notification/notification-states.xml" },
        { "settlement",         PREFIX + "settlement/settlement-states.xml" },
        { "returnrequest",      PREFIX + "returnrequest/returnrequest-states.xml" },
        { "review",             PREFIX + "review/review-states.xml" },
        { "support",            PREFIX + "support/support-states.xml" },
        { "supplier",           PREFIX + "supplier/supplier-states.xml" },
        { "onboarding",         PREFIX + "onboarding/onboarding-states.xml" },
        { "reconciliation",     PREFIX + "reconciliation/reconciliation-states.xml" },
        { "fulfillment",        PREFIX + "fulfillment/fulfillment-states.xml" },
        { "returnProcessing",   PREFIX + "returnprocessing/return-processing-states.xml" },
        { "supplierLifecycle",  PREFIX + "supplierlifecycle/supplier-lifecycle-states.xml" },
        { "ruleSet",            PREFIX + "rulesengine/ruleset-lifecycle.xml" },
        { "agreement",          PREFIX + "compliance/agreement-states.xml" },
        { "platformPolicy",     PREFIX + "compliance/platform-policy-states.xml" },
    };

    @PostConstruct
    public void loadAllStmMetadata() {
        int loaded = 0;
        for (String[] def : FLOW_DEFINITIONS) {
            String registryName = def[0];
            String xmlFile = def[1];
            try {
                STMFlowStoreImpl flowStore = new STMFlowStoreImpl();
                // No BeanFactoryAdapter needed — stripped XMLs have no componentName references
                XmlFlowReader reader = new XmlFlowReader(flowStore);
                reader.setFilename(xmlFile);

                STMActionsInfoProvider provider = new STMActionsInfoProvider(flowStore);
                WorkflowRegistry.addSTMActionsInfoProvider(registryName, provider);
                loaded++;
                log.debug("Loaded query STM metadata: {}", registryName);
            } catch (Exception e) {
                log.warn("Failed to load query STM metadata for {} from {}: {}",
                        registryName, xmlFile, e.getMessage());
            }
        }
        log.info("Query STM metadata loaded: {}/{} flows registered in WorkflowRegistry",
                loaded, FLOW_DEFINITIONS.length);
    }
}
