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
    private static final String[][] FLOW_DEFINITIONS = {
        { "cart",               "com/homebase/ecom/cart/cart-states.xml" },
        { "user",               "com/homebase/ecom/user/user-states.xml" },
        { "product",            "com/homebase/ecom/product/product-states.xml" },
        { "inventory",          "com/homebase/ecom/inventory/inventory-states.xml" },
        { "offer",              "com/homebase/ecom/offer/offer-states.xml" },
        { "promo",              "com/homebase/ecom/promo/promo-states.xml" },
        { "checkout",           "com/homebase/ecom/checkout/checkout-states.xml" },
        { "order",              "com/homebase/ecom/order/order-states.xml" },
        { "payment",            "com/homebase/ecom/payment/payment-states.xml" },
        { "shipping",           "com/homebase/ecom/shipping/shipping-states.xml" },
        { "notification",       "com/homebase/ecom/notification/notification-states.xml" },
        { "settlement",         "com/homebase/ecom/settlement/settlement-states.xml" },
        { "returnrequest",      "com/homebase/ecom/returnrequest/returnrequest-states.xml" },
        { "review",             "com/homebase/ecom/review/review-states.xml" },
        { "support",            "com/homebase/ecom/support/support-states.xml" },
        { "supplier",           "com/homebase/ecom/supplier/supplier-states.xml" },
        { "onboarding",         "com/homebase/ecom/onboarding/onboarding-states.xml" },
        { "reconciliation",     "com/homebase/ecom/reconciliation/reconciliation-states.xml" },
        { "fulfillment",        "com/homebase/ecom/fulfillment/fulfillment-states.xml" },
        { "returnProcessing",   "com/homebase/ecom/returnprocessing/return-processing-states.xml" },
        { "supplierLifecycle",  "com/homebase/ecom/supplierlifecycle/supplier-lifecycle-states.xml" },
        { "ruleSet",            "com/homebase/ecom/rulesengine/ruleset-lifecycle.xml" },
        { "agreement",          "com/homebase/ecom/compliance/agreement-states.xml" },
        { "platformPolicy",     "com/homebase/ecom/compliance/platform-policy-states.xml" },
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
