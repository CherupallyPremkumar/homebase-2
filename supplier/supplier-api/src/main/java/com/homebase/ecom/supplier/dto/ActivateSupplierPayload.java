package com.homebase.ecom.supplier.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the activateSupplier event.
 * Admin or system activates a supplier after onboarding is complete.
 * Transitions from APPROVED -> ACTIVE.
 */
public class ActivateSupplierPayload extends MinimalPayload {
}
