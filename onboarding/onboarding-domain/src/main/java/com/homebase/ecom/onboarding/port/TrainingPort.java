package com.homebase.ecom.onboarding.port;

import java.util.List;

/**
 * Port for training system integration.
 */
public interface TrainingPort {

    /**
     * Initialize training for a supplier onboarding application.
     */
    void initializeTraining(String onboardingId, String supplierId);

    /**
     * Get list of required training module IDs.
     */
    List<String> getRequiredModules();

    /**
     * Check if a specific module has been completed.
     */
    boolean isModuleCompleted(String onboardingId, String moduleId);
}
