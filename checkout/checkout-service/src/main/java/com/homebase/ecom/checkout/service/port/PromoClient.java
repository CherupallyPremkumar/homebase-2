package com.homebase.ecom.checkout.service.port;

import java.util.UUID;

public interface PromoClient {
    void commitPromo(String couponCode, UUID userId);
    void releasePromo(String couponCode, UUID userId);
}
