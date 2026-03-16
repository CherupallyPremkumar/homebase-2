package com.homebase.ecom.offer.domain.model;

public enum OfferStatus {
    /** Seller has created the offer, not yet submitted */
    DRAFT,

    /** Offer submitted, awaiting catalog admin approval */
    PENDING_APPROVAL,

    /** Offer approved by catalog admin */
    APPROVED,

    /** Offer is live and visible to customers */
    LIVE,

    /** Offer has expired (end date passed) */
    EXPIRED,

    /** Offer archived after expiration */
    ARCHIVED,

    /** Offer rejected by catalog admin */
    REJECTED,

    /** Offer suspended (e.g., out of stock, admin action) */
    SUSPENDED
}
