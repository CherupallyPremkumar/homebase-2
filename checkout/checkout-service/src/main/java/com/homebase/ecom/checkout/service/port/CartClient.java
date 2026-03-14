package com.homebase.ecom.checkout.service.port;

import com.homebase.ecom.checkout.domain.model.CartSnapshot;
import java.util.UUID;

public interface CartClient {
    CartSnapshot lockCart(UUID cartId);
    void unlockCart(UUID cartId);
}
