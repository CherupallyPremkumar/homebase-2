package com.homebase.ecom.onboarding.port;

/**
 * Port for notification delivery — sends onboarding lifecycle notifications.
 */
public interface NotificationPort {

    /**
     * Notify supplier that their onboarding is complete and they are active.
     */
    void notifyOnboardingCompleted(String onboardingId, String businessName, String supplierId);

    /**
     * Notify supplier that their application was rejected.
     */
    void notifyOnboardingRejected(String onboardingId, String businessName, String reason);

    /**
     * Notify supplier that additional documents are requested.
     */
    void notifyDocumentsRequested(String onboardingId, String businessName, String notes);
}
