package com.homebase.ecom.checkout.infrastructure.client;

import com.homebase.ecom.checkout.domain.model.PaymentDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "payment-service", url = "${payment.service.url:http://localhost:8085}")
public interface PaymentServiceClient {
    @PostMapping("/api/payments/checkout-session")
    PaymentDetails createPaymentSession(@RequestParam UUID orderId, @RequestParam BigDecimal amount, @RequestParam UUID userId);
}
