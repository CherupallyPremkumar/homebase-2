package com.homebase.ecom.rulesengine.api.exception;

public class RuleSetNotFoundException extends RuntimeException {
    public RuleSetNotFoundException(String id) {
        super("RuleSet not found: " + id);
    }
}
