package com.homebase.ecom.rulesengine.domain.model;

import com.homebase.ecom.rulesengine.api.enums.Effect;
import org.chenile.utils.entity.model.BaseEntity;

import java.util.HashMap;
import java.util.Map;

public class Rule extends BaseEntity {
    private String name;
    private String expression;
    private Effect effect = Effect.ALLOW;
    private int priority;
    private boolean active = true;
    private String tenant;
    private Map<String, String> metadata = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata != null ? metadata : new HashMap<>();
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
}
