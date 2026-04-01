package com.homebase.ecom.rulesengine.api.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

public class RuleViolationException extends PolicyViolationException {
    public RuleViolationException(String message) {
        super("rules-engine", message);
    }
}
