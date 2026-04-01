package com.homebase.ecom.rulesengine.api.dto;

import com.homebase.ecom.rulesengine.api.enums.Effect;
import java.io.Serializable;
import java.util.List;

public class RuleSetDto implements Serializable {
    private String id;
    private String name;
    private String description;
    private List<RuleDto> rules;
    private boolean active;
    private Effect defaultEffect;
    private String currentState;
    private String targetModule;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RuleDto> getRules() {
        return rules;
    }

    public void setRules(List<RuleDto> rules) {
        this.rules = rules;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Effect getDefaultEffect() {
        return defaultEffect;
    }

    public void setDefaultEffect(Effect defaultEffect) {
        this.defaultEffect = defaultEffect;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getTargetModule() {
        return targetModule;
    }

    public void setTargetModule(String targetModule) {
        this.targetModule = targetModule;
    }
}
