package com.homebase.ecom.policy.api.dto;

import java.io.Serializable;
import java.util.Map;

public class EvaluateRequest implements Serializable {
    private String policyId;
    private String subjectId;   // The Saree ID
    private String resource;    // e.g., "catalog:premium-collection"
    private String action;      // e.g., "LIST_ON_STOREFRONT"
    private Map<String, Object> facts;    // The core saree attributes
    private Map<String, String> metadata; // TraceId, Timestamp, etc.
    private String targetModule; // e.g. "CATALOG", "CART"

    // Getters and Setters
    public String getPolicyId() { return policyId; }
    public void setPolicyId(String policyId) { this.policyId = policyId; }

    public String getSubjectId() { return subjectId; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }

    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public Map<String, String> getMetadata() { return metadata; }
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }

    public Map<String, Object> getFacts() {
        return facts;
    }

    public void setFacts(Map<String, Object> facts) {
        this.facts = facts;
    }

    public String getTargetModule() {
        return targetModule;
    }

    public void setTargetModule(String targetModule) {
        this.targetModule = targetModule;
    }
}