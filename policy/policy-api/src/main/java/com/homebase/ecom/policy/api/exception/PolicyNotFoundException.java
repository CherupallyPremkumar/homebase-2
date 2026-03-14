package com.homebase.ecom.policy.api.exception;

public class PolicyNotFoundException extends RuntimeException {
    public PolicyNotFoundException(String id) {
        super("Policy not found: " + id);
    }
}
