package com.homebase.ecom.checkout.infrastructure.client;

import com.homebase.ecom.checkout.domain.model.CartSnapshot;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@FeignClient(name = "cart-service", url = "${cart.service.url:http://localhost:8080}")
public interface CartServiceClient {
    @PostMapping("/api/carts/{cartId}/lock")
    CartSnapshot lockCart(@PathVariable UUID cartId);

    @PostMapping("/api/carts/{cartId}/unlock")
    void unlockCart(@PathVariable UUID cartId);
}
