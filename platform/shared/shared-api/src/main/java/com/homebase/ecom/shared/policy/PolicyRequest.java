package com.homebase.ecom.shared.policy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Carrier for Policy Orchestration.
 * Carries "Facts" from various modules (Cart, Inventory, Catalog)
 * to the Global Policy Engine.
 */
public class PolicyRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String policyName;
    private Map<String, Object> facts = new HashMap<>();

    public PolicyRequest() {
    }

    public PolicyRequest(String policyName) {
        this.policyName = policyName;
    }

    public void addFact(String key, Object value) {
        this.facts.put(key, value);
    }

    public Object getFact(String key) {
        return this.facts.get(key);
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public Map<String, Object> getFacts() {
        return facts;
    }

    public void setFacts(Map<String, Object> facts) {
        this.facts = facts;
    }
}
