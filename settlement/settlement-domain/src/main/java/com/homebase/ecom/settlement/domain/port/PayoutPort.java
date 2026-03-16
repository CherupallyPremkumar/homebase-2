package com.homebase.ecom.settlement.domain.port;

import com.homebase.ecom.settlement.model.Settlement;

/**
 * Outbound port for initiating payouts to suppliers.
 * Implementations connect to payment gateways, banking APIs, etc.
 */
public interface PayoutPort {

    /**
     * Initiates payout to the supplier.
     * @param settlement the settlement to disburse
     * @return disbursement reference (e.g., bank transaction ID)
     */
    String initiatePayout(Settlement settlement);

    /**
     * Checks if a disbursement was successful.
     */
    boolean isPayoutSuccessful(String disbursementReference);
}
