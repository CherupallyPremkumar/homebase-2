package com.homebase.ecom.policy.infrastructure.persistence.entity;

import com.homebase.ecom.policy.api.enums.Effect;
import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "policy_rule")
public class RuleEntity extends BaseJpaEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "expression")
    private String expression;

    @Enumerated(EnumType.STRING)
    @Column(name = "effect")
    private Effect effect;  

    @Column(name = "priority")
    private int priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id")
    private PolicyEntity policy;

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

    public PolicyEntity getPolicy() {
        return policy;
    }

    public void setPolicy(PolicyEntity policy) {
        this.policy = policy;
    }
}
