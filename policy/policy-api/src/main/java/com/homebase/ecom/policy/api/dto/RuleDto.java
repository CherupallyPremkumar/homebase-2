package com.homebase.ecom.policy.api.dto;

import com.homebase.ecom.policy.api.enums.Effect;
import java.io.Serializable;
import java.util.Map;

public class RuleDto implements Serializable {
    private String id;
    private String name;
    private String expression; // SpEL or similar expression
    private Map<String, Object> metadata;
    private Effect effect;
    private int priority;

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

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
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
}
