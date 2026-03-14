package com.homebase.ecom.policy.domain.model;

import com.homebase.ecom.policy.api.enums.Effect;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import java.util.ArrayList;
import java.util.List;

import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

public class Policy extends AbstractExtendedStateEntity implements ContainsTransientMap {
    private String name;
    private String description;
    private boolean active;
    private Effect defaultEffect = Effect.DENY;
    private List<Rule> rules = new ArrayList<>();
    private String targetModule;
    private TransientMap transientMap = new TransientMap();

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

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public void removeRule(String ruleId) {
        rules.removeIf(r -> r.getId().equals(ruleId));
    }

    @Override
    public TransientMap getTransientMap() {
        return transientMap;
    }

    public void setTransientMap(TransientMap transientMap) {
        this.transientMap = transientMap;
    }

    public String getTargetModule() {
        return targetModule;
    }

    public void setTargetModule(String targetModule) {
        this.targetModule = targetModule;
    }
}
