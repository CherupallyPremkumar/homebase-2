package com.homebase.ecom.policy.domain.valueobject;

import java.io.Serializable;

public final class PolicyId implements Serializable {
    private final String value;

    public PolicyId(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolicyId policyId = (PolicyId) o;
        return value != null ? value.equals(policyId.value) : policyId.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "PolicyId(" + value + ")";
    }
}
