package com.homebase.ecom.payment.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutSessionRequestTest {

    @Test
    void testBuilderAndGetters() {
        CheckoutSessionRequest.LineItem item = CheckoutSessionRequest.LineItem.builder()
                .productId("PROD-1")
                .productName("Test Product")
                .unitPrice(BigDecimal.valueOf(1000))
                .quantity(1)
                .build();

        CheckoutSessionRequest request = CheckoutSessionRequest.builder()
                .orderId("ORD-123")
                .amount(BigDecimal.valueOf(1000))
                .currency("USD")
                .successUrl("https://example.com/success")
                .cancelUrl("https://example.com/cancel")
                .items(Collections.singletonList(item))
                .build();

        assertEquals("ORD-123", request.getOrderId());
        assertEquals(BigDecimal.valueOf(1000), request.getAmount());
        assertEquals("USD", request.getCurrency());
        assertEquals(1, request.getItems().size());
        assertEquals("Test Product", request.getItems().get(0).getProductName());
    }

    @Test
    void testNoArgsConstructor() {
        CheckoutSessionRequest request = new CheckoutSessionRequest();
        assertNull(request.getOrderId());
        assertNull(request.getItems());
    }
}
