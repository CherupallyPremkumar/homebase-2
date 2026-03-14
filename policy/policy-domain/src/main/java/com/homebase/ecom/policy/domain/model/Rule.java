package com.homebase.ecom.policy.domain.model;

import com.homebase.ecom.policy.api.enums.Effect;
import org.chenile.utils.entity.model.BaseEntity;

public class Rule extends BaseEntity {
    private String name;
    private String expression;
    private Effect effect = Effect.ALLOW;
    private int priority;
    private boolean active = true;

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
}
