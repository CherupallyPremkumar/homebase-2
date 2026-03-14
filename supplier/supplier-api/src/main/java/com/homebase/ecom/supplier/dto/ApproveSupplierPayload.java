package com.homebase.ecom.supplier.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the approveSupplier event.
 * Admin approves a supplier application after document review.
 */
public class ApproveSupplierPayload extends MinimalPayload {

    private Double commissionPercentage;

    public Double getCommissionPercentage() { return commissionPercentage; }
    public void setCommissionPercentage(Double commissionPercentage) { this.commissionPercentage = commissionPercentage; }
}
