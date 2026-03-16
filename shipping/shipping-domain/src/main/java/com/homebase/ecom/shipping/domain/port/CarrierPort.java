package com.homebase.ecom.shipping.domain.port;

/**
 * Port for carrier integration (3PL APIs).
 * Adapter implementations connect to carrier systems (DHL, Delhivery, BlueDart, etc.).
 */
public interface CarrierPort {

    /**
     * Generates a shipping label with the carrier.
     * @return tracking number assigned by the carrier
     */
    String generateLabel(String carrier, String fromAddress, String toAddress, String weight, String dimensions);

    /**
     * Gets the current tracking status from the carrier API.
     */
    String getTrackingStatus(String carrier, String trackingNumber);

    /**
     * Builds a customer-facing tracking URL for the carrier.
     */
    String buildTrackingUrl(String carrier, String trackingNumber);
}
