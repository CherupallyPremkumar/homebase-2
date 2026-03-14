package com.homebase.ecom.catalog.infrastructure.integration;

import com.homebase.ecom.catalog.domain.port.out.OfferClientPort;
import java.math.BigDecimal;

public class OfferClientAdapter implements OfferClientPort {

    // private final OfferClient feignClient; (to be injected later)

    @Override
    public OfferOverview fetchOfferDetails(String offerId) {
        // TODO: Call actual offer-client (Feign) when available
        // Return dummy data for now
        OfferOverview overview = new OfferOverview();
        overview.setId(offerId);
        overview.setVariantId("dummy-variant-id");
        overview.setPrice(new BigDecimal("99.99"));
        overview.setActive(true);
        return overview;
    }
}
