package com.homebase.ecom.onboarding.infrastructure.adapter;

import com.homebase.ecom.onboarding.port.NotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default adapter for onboarding notifications.
 * In production, this would integrate with email/SMS/push notification services.
 */
public class NotificationAdapter implements NotificationPort {

    private static final Logger log = LoggerFactory.getLogger(NotificationAdapter.class);

    @Override
    public void notifyOnboardingCompleted(String onboardingId, String businessName, String supplierId) {
        log.info("Notification: Onboarding completed for {} (id={}), supplier={}",
                businessName, onboardingId, supplierId);
    }

    @Override
    public void notifyOnboardingRejected(String onboardingId, String businessName, String reason) {
        log.info("Notification: Onboarding rejected for {} (id={}), reason: {}",
                businessName, onboardingId, reason);
    }

    @Override
    public void notifyDocumentsRequested(String onboardingId, String businessName, String notes) {
        log.info("Notification: Documents requested for {} (id={}), notes: {}",
                businessName, onboardingId, notes);
    }
}
