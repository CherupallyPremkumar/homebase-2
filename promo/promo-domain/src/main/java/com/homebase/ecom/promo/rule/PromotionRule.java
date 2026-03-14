package com.homebase.ecom.promo.rule;

import com.homebase.ecom.promo.model.CartSnapshot;
import com.homebase.ecom.promo.model.PromotionResult;
import com.homebase.ecom.promo.model.RuleMetadata;
import com.homebase.ecom.shared.Money;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class PromotionRule implements Serializable {
    private static final long serialVersionUID = 1L;

    protected UUID ruleId;
    protected String name;
    protected List<Condition> conditions;
    protected List<Action> actions;
    protected RuleMetadata metadata;

    public UUID getRuleId() {
        return ruleId;
    }

    public void setRuleId(UUID ruleId) {
        this.ruleId = ruleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public RuleMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(RuleMetadata metadata) {
        this.metadata = metadata;
    }

    public abstract PromotionResult evaluate(CartSnapshot cart);

    public abstract boolean matchesConditions(CartSnapshot cart);

    public abstract Money calculateSavings(CartSnapshot cart);

    public boolean isApplicable() {
        if (metadata == null) return true;
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        if (metadata.getStartDate() != null && now.isBefore(metadata.getStartDate())) return false;
        if (metadata.getEndDate() != null && now.isAfter(metadata.getEndDate())) return false;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PromotionRule that = (PromotionRule) o;
        return Objects.equals(ruleId, that.ruleId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(conditions, that.conditions) &&
                Objects.equals(actions, that.actions) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleId, name, conditions, actions, metadata);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "ruleId=" + ruleId +
                ", name=" + name +
                ", conditions=" + conditions +
                ", actions=" + actions +
                ", metadata=" + metadata +
                ")";
    }
}
