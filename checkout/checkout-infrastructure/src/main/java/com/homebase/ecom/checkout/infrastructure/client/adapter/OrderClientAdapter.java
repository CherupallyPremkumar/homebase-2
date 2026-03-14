package com.homebase.ecom.checkout.infrastructure.client.adapter;

import com.homebase.ecom.checkout.domain.model.Checkout;
import com.homebase.ecom.checkout.domain.model.OrderDetails;
import com.homebase.ecom.checkout.infrastructure.client.OrderServiceClient;
import com.homebase.ecom.checkout.service.port.OrderClient;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class OrderClientAdapter implements OrderClient {
    private final OrderServiceClient orderServiceClient;

    public OrderClientAdapter(OrderServiceClient orderServiceClient) {
        this.orderServiceClient = orderServiceClient;
    }

    @Override
    public OrderDetails createOrder(Checkout checkout) {
        return orderServiceClient.createOrder(
            checkout.getUserId(),
            checkout.getLockedCart(),
            checkout.getLockedPrice(),
            null // coupon code from checkout?
        );
    }

    @Override
    public void cancelOrder(String orderId) {
        orderServiceClient.cancelOrder(UUID.fromString(orderId));
    }
}
