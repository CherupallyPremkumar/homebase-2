package com.homebase.ecom.promo.model;

public enum ResolutionStrategy {
    BEST_DEAL,      // Apply the most single beneficial promotion
    STACK_ALL,      // Apply all stackable ones
    PRIORITY_ONLY,  // Apply based on priority only
    EXCLUDE_REST    // If one exclusive applies, exclude all others
}
