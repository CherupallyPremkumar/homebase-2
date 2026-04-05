package com.homebase.ecom.pricing.domain.port;

/**
 * Port for resolving the customer's pricing tier.
 * Infrastructure adapter calls User service via ProxyBuilder.
 */
public interface CustomerTierPort {
    String getTier(String userId);
}
