package com.homebase.ecom.dto;

import java.time.Instant;

/**
 * DTO for timeout-related cart queries:
 * - idleCarts (Cart.getIdleCarts)
 * - stuckCheckouts (Cart.getStuckCheckouts)
 * - stuckPayments (Cart.getStuckPayments)
 *
 * All three queries return only: id + lastModifiedTime.
 * Field names MUST match the SQL column aliases defined in cart.xml.
 */
public class CartTimeoutDto {

    private String id;
    private Instant lastModifiedTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Instant lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
}
