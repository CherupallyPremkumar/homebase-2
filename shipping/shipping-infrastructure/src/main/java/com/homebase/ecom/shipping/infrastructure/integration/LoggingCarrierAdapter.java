package com.homebase.ecom.shipping.infrastructure.integration;

import com.homebase.ecom.shipping.domain.port.CarrierPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Infrastructure adapter for carrier integration (3PL APIs).
 *
 * Currently a logging stub that returns mock label/tracking data.
 * Will delegate to actual carrier APIs (DHL, Delhivery, BlueDart, etc.)
 * once external carrier integration is complete.
 */
public class LoggingCarrierAdapter implements CarrierPort {

    private static final Logger log = LoggerFactory.getLogger(LoggingCarrierAdapter.class);

    @Override
    public String generateLabel(String carrier, String fromAddress, String toAddress,
                                String weight, String dimensions) {
        String trackingNumber = "TRK-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
        log.info("Generating shipping label: carrier={}, from={}, to={}, weight={}, dimensions={}, trackingNumber={}",
                carrier, fromAddress, toAddress, weight, dimensions, trackingNumber);
        return trackingNumber;
    }

    @Override
    public String getTrackingStatus(String carrier, String trackingNumber) {
        log.info("Fetching tracking status: carrier={}, trackingNumber={}", carrier, trackingNumber);
        return "IN_TRANSIT";
    }

    @Override
    public String buildTrackingUrl(String carrier, String trackingNumber) {
        String trackingUrl = "https://track.example.com/" + carrier + "/" + trackingNumber;
        log.info("Building tracking URL: carrier={}, trackingNumber={}, url={}",
                carrier, trackingNumber, trackingUrl);
        return trackingUrl;
    }
}
