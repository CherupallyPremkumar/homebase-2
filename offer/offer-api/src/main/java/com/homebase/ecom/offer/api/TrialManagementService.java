package com.homebase.ecom.offer.api;

import java.util.Date;

/**
 * Service to manage the 15-day trial period for Marketplace Offers.
 */
public interface TrialManagementService {

    /**
     * Starts the trial for an offer (usually called when Dad approves it).
     */
    void startTrial(String offerId);

    /**
     * Periodically check for expired trials and finalize them.
     */
    void processExpiredTrials();

    /**
     * Manually fail a trial (e.g., if Dad rejects the quality after seeing 
     * several customer returns within 15 days).
     */
    void failTrial(String offerId, String reason);
}
