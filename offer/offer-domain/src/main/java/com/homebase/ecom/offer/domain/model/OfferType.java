package com.homebase.ecom.offer.domain.model;

/**
 * Types of offers that sellers can create.
 */
public enum OfferType {
    /** Standard deal with percentage or fixed discount */
    DEAL,

    /** Time-limited flash sale */
    LIGHTNING,

    /** Clearance pricing for end-of-life products */
    CLEARANCE
}
