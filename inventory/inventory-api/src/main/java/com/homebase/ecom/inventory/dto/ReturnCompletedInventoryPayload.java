package com.homebase.ecom.inventory.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
    Customized Payload for the returnCompleted event.
*/
public class ReturnCompletedInventoryPayload extends MinimalPayload{

    /** Tracking reference from the supplier acknowledging receipt. */
    private String supplierAcknowledgementRef;

    public String getSupplierAcknowledgementRef() { return supplierAcknowledgementRef; }
    public void setSupplierAcknowledgementRef(String supplierAcknowledgementRef) { this.supplierAcknowledgementRef = supplierAcknowledgementRef; }
}
