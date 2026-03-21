package com.homebase.ecom.pricing.infrastructure.integration;

import com.homebase.ecom.pricing.domain.port.CustomerTierPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Infrastructure adapter: resolves customer pricing tier.
 * Currently returns STANDARD for all users.
 * Will be replaced with a real adapter calling user-client when User module
 * exposes tier information via its API.
 */
public class DefaultCustomerTierAdapter implements CustomerTierPort {

    private static final Logger log = LoggerFactory.getLogger(DefaultCustomerTierAdapter.class);
    private static final String DEFAULT_TIER = "STANDARD";

    @Override
    public String getTier(String userId) {
        log.debug("Resolving customer tier for userId={}, returning default: {}", userId, DEFAULT_TIER);
        return DEFAULT_TIER;
    }
}
