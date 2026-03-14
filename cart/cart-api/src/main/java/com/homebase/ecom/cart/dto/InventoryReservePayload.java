package com.homebase.ecom.cart.dto;

import org.chenile.workflow.param.MinimalPayload;

public class InventoryReservePayload extends MinimalPayload {
    private static final long serialVersionUID = 1L;
    private String reservationId;
    private boolean success;

    public String getReservationId() { return reservationId; }
    public void setReservationId(String reservationId) { this.reservationId = reservationId; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}
