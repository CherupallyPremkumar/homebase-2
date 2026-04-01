package com.homebase.ecom.compliance.exception;

import org.chenile.base.exception.NotFoundException;

public class PolicyNotFoundException extends NotFoundException {
    public PolicyNotFoundException(String id) {
        super(404, "Platform policy not found: " + id);
    }
}
