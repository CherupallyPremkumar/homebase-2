package com.homebase.ecom.compliance.exception;

import org.chenile.base.exception.NotFoundException;

public class AgreementNotFoundException extends NotFoundException {
    public AgreementNotFoundException(String id) {
        super(404, "Agreement not found: " + id);
    }
}
