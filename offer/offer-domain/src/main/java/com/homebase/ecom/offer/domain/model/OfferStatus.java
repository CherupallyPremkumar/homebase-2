package com.homebase.ecom.offer.domain.model;

public enum OfferStatus {
    /** Maker has uploaded the product, waiting for Hub review */
    PENDING_REVIEW,
    
    /** Hub manager (Dad) approved the quality and price */
    ACTIVE,
    
    /** Hub manager rejected the quality or price */
    REJECTED,
    
    /** Quality check during 15-day trial failed, returned to maker */
    RETURNED,
    
    /** Maker or Hub discontinued the offer */
    INACTIVE
}
