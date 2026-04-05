package com.homebase.ecom.checkout.domain.port;

import com.homebase.ecom.checkout.model.Checkout;

/**
 * Port for creating/cancelling orders during checkout.
 * Adapter calls Order service.
 */
public interface OrderCreationPort {

    /**
     * Creates an order from the checkout data.
     * @return the created order ID
     */
    String createOrder(Checkout checkout);

    /**
     * Cancels the order — used for compensation.
     */
    void cancelOrder(String orderId);
}
