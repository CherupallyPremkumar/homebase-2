package com.homebase.ecom.settlement.dto;

import java.io.Serializable;

public class ConfirmPayoutPayload implements Serializable {
    private static final long serialVersionUID = 1L;
    private String paymentReference; // Cash, Bank Ref, etc.
    private String notes;

    public ConfirmPayoutPayload() {
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
