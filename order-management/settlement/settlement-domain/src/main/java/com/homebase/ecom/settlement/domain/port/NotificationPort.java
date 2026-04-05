package com.homebase.ecom.settlement.domain.port;

import com.homebase.ecom.settlement.model.Settlement;

/**
 * Outbound port for sending settlement-related notifications.
 * Implementations connect to email, SMS, push notification services, etc.
 */
public interface NotificationPort {

    /**
     * Notifies the supplier that their settlement has been approved.
     */
    void notifySettlementApproved(Settlement settlement);

    /**
     * Notifies the supplier that their settlement has been disbursed.
     */
    void notifySettlementDisbursed(Settlement settlement);

    /**
     * Notifies the supplier about a dispute resolution.
     */
    void notifyDisputeResolved(Settlement settlement, String resolution);
}
