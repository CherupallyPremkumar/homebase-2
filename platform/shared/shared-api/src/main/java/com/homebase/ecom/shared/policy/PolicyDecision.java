package com.homebase.ecom.shared.policy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Result of a Policy evaluation.
 */
public class PolicyDecision implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean approved;
    private String reason;
    private List<String> errors = new ArrayList<>();

    public PolicyDecision() {
    }

    public static PolicyDecision approved() {
        PolicyDecision decision = new PolicyDecision();
        decision.setApproved(true);
        return decision;
    }

    public static PolicyDecision rejected(String reason) {
        PolicyDecision decision = new PolicyDecision();
        decision.setApproved(false);
        decision.setReason(reason);
        return decision;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void addError(String error) {
        this.errors.add(error);
    }
}
