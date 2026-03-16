package com.homebase.ecom.settlement.infrastructure.adapter;

import com.homebase.ecom.settlement.domain.port.NotificationPort;
import com.homebase.ecom.settlement.model.Settlement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default adapter for settlement notifications.
 * In production, this would connect to email/SMS/push notification services.
 */
public class NotificationAdapter implements NotificationPort {

    private static final Logger log = LoggerFactory.getLogger(NotificationAdapter.class);

    @Override
    public void notifySettlementApproved(Settlement settlement) {
        log.info("Notification: Settlement {} approved for supplier {}. Net amount: {}",
                settlement.getId(), settlement.getSupplierId(), settlement.getNetAmount());
    }

    @Override
    public void notifySettlementDisbursed(Settlement settlement) {
        log.info("Notification: Settlement {} disbursed to supplier {}. Reference: {}",
                settlement.getId(), settlement.getSupplierId(), settlement.getDisbursementReference());
    }

    @Override
    public void notifyDisputeResolved(Settlement settlement, String resolution) {
        log.info("Notification: Settlement {} dispute resolved for supplier {}. Resolution: {}",
                settlement.getId(), settlement.getSupplierId(), resolution);
    }
}
