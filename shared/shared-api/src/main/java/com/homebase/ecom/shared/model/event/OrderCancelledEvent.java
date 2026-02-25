package com.homebase.ecom.shared.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelledEvent {
    private String orderId;
    private String customerId;
    private List<OrderItemPayload> items;
    private LocalDateTime timestamp;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemPayload {
        private String productId;
        private Integer quantity;
    }
}
