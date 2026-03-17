package com.homebase.ecom.rulesengine.domain.valueobject;

import java.io.Serializable;

public final class RuleSetId implements Serializable {
    private final String value;

    public RuleSetId(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleSetId ruleSetId = (RuleSetId) o;
        return value != null ? value.equals(ruleSetId.value) : ruleSetId.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "RuleSetId(" + value + ")";
    }
}
