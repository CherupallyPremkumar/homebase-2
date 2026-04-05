package com.homebase.ecom.supplier.domain.port;

/**
 * Outbound Port (Hexagonal): Payout operations.
 * Domain depends on this interface. Settlement/payment infrastructure provides the adapter.
 */
public interface PayoutPort {

    /**
     * Holds all pending payouts for the given supplier.
     * Called when supplier is suspended or terminated.
     */
    void holdPayouts(String supplierId);

    /**
     * Releases held payouts for the given supplier.
     * Called when supplier is reactivated.
     */
    void releasePayouts(String supplierId);
}
