package com.homebase.ecom.demoorder.event;

import java.time.LocalDateTime;

/**
 * Published when a demo order is created.
 */
public class DemoOrderCreatedEvent extends DemoOrderEvent {

    private String productName;
    private int quantity;
    private String customerId;

    public DemoOrderCreatedEvent() {}

    public DemoOrderCreatedEvent(String orderId, LocalDateTime timestamp,
                                  String productName, int quantity, String customerId) {
        super("ORDER_CREATED", orderId, timestamp);
        this.productName = productName;
        this.quantity = quantity;
        this.customerId = customerId;
    }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
}
