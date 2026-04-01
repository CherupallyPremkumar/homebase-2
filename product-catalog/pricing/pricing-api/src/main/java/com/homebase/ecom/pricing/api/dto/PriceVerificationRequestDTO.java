package com.homebase.ecom.pricing.api.dto;

import java.io.Serializable;

/**
 * Request to verify a previously locked price is still valid.
 * Checkout sends this before initiating payment.
 */
public class PriceVerificationRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String lockToken;
    private String breakdownHash;

    public PriceVerificationRequestDTO() {}

    public String getLockToken() { return lockToken; }
    public void setLockToken(String lockToken) { this.lockToken = lockToken; }
    public String getBreakdownHash() { return breakdownHash; }
    public void setBreakdownHash(String breakdownHash) { this.breakdownHash = breakdownHash; }
}
