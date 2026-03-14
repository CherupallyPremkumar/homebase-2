package com.homebase.ecom.policy.api.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

public class PolicyRuleViolationException extends PolicyViolationException {
    public PolicyRuleViolationException(String message) {
        super("policy", message);
    }
}
