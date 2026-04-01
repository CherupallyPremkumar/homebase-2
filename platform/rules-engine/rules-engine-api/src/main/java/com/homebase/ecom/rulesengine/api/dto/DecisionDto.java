package com.homebase.ecom.rulesengine.api.dto;

import java.io.Serializable;
import java.util.Map;

import com.homebase.ecom.rulesengine.api.enums.Effect;

public class DecisionDto implements Serializable {
    private String id;
    private String ruleSetId;
    private String subjectId;
    private String resource;
    private String action;
    private Effect effect;
    private String reasons;
    private String targetModule;
    private String timestamp;
    private Map<String, String> metadata;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRuleSetId() { return ruleSetId; }
    public void setRuleSetId(String ruleSetId) { this.ruleSetId = ruleSetId; }

    public String getSubjectId() { return subjectId; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }

    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public Effect getEffect() { return effect; }
    public void setEffect(Effect effect) { this.effect = effect; }

    public String getReasons() { return reasons; }
    public void setReasons(String reasons) { this.reasons = reasons; }

    public String getTargetModule() {
        return targetModule;
    }

    public void setTargetModule(String targetModule) {
        this.targetModule = targetModule;
    }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public Map<String, String> getMetadata() { return metadata; }
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }
}
