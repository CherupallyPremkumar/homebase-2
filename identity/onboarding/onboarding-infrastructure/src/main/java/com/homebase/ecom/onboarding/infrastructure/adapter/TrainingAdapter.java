package com.homebase.ecom.onboarding.infrastructure.adapter;

import com.homebase.ecom.onboarding.port.TrainingPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Default adapter for training system integration.
 * In production, this would integrate with an LMS or training platform.
 */
public class TrainingAdapter implements TrainingPort {

    private static final Logger log = LoggerFactory.getLogger(TrainingAdapter.class);

    @Override
    public void initializeTraining(String onboardingId, String supplierId) {
        log.info("Initializing training for onboarding={}, supplier={}", onboardingId, supplierId);
        // In production: create training enrollment in LMS
    }

    @Override
    public List<String> getRequiredModules() {
        return List.of("PLATFORM_BASICS", "SELLER_POLICIES", "SHIPPING_GUIDELINES");
    }

    @Override
    public boolean isModuleCompleted(String onboardingId, String moduleId) {
        log.info("Checking training module completion: onboarding={}, module={}", onboardingId, moduleId);
        // In production: query LMS for completion status
        return false;
    }
}
