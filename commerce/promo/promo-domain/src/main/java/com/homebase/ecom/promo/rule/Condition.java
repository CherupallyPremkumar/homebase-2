package com.homebase.ecom.promo.rule;

import com.homebase.ecom.promo.model.ConditionType;
import com.homebase.ecom.promo.model.Operator;

import java.io.Serializable;
import java.util.UUID;

public final class Condition implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID conditionId;
    private final ConditionType type;
    private final Operator operator;
    private final Object value;

    public Condition(UUID conditionId, ConditionType type, Operator operator, Object value) {
        this.conditionId = conditionId;
        this.type = type;
        this.operator = operator;
        this.value = value;
    }

    public UUID getConditionId() {
        return conditionId;
    }

    public ConditionType getType() {
        return type;
    }

    public Operator getOperator() {
        return operator;
    }

    public Object getValue() {
        return value;
    }

    public static ConditionBuilder builder() {
        return new ConditionBuilder();
    }

    public static class ConditionBuilder {
        private UUID conditionId;
        private ConditionType type;
        private Operator operator;
        private Object value;

        public ConditionBuilder conditionId(UUID conditionId) {
            this.conditionId = conditionId;
            return this;
        }

        public ConditionBuilder type(ConditionType type) {
            this.type = type;
            return this;
        }

        public ConditionBuilder operator(Operator operator) {
            this.operator = operator;
            return this;
        }

        public ConditionBuilder value(Object value) {
            this.value = value;
            return this;
        }

        public Condition build() {
            return new Condition(conditionId, type, operator, value);
        }
    }
}
