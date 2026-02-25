package com.ecommerce.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutSessionRequest {

    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String successUrl;
    private String cancelUrl;
    private List<LineItem> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LineItem {
        private String productId;
        private String productName;
        private BigDecimal unitPrice;
        private int quantity;
        private BigDecimal totalPrice;
    }
}
