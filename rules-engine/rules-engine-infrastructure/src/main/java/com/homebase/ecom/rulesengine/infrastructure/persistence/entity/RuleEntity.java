package com.homebase.ecom.rulesengine.infrastructure.persistence.entity;

import com.homebase.ecom.rulesengine.api.enums.Effect;
import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

import java.util.HashMap;
import java.util.Map;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "policy_rule_metadata", joinColumns = @JoinColumn(name = "rule_id"))
    @MapKeyColumn(name = "meta_key")
    @Column(name = "meta_value")
    private Map<String, String> metadata = new HashMap<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id")
    private RuleSetEntity ruleSet;

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

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata != null ? metadata : new HashMap<>();
    }

    public RuleSetEntity getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(RuleSetEntity ruleSet) {
        this.ruleSet = ruleSet;
    }
}
