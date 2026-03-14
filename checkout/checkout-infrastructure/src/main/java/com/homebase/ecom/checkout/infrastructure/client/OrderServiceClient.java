package com.homebase.ecom.checkout.infrastructure.client;

import com.homebase.ecom.checkout.domain.model.CartSnapshot;
import com.homebase.ecom.checkout.domain.model.PriceSnapshot;
import com.homebase.ecom.checkout.domain.model.OrderDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@FeignClient(name = "order-service", url = "${order.service.url:http://localhost:8084}")
public interface OrderServiceClient {
    @PostMapping("/api/orders")
    OrderDetails createOrder(@RequestParam UUID userId, @RequestBody CartSnapshot cart, @RequestBody PriceSnapshot price, @RequestParam(required = false) String couponCode);

    @PostMapping("/api/orders/{orderId}/cancel")
    void cancelOrder(@PathVariable UUID orderId);
}
