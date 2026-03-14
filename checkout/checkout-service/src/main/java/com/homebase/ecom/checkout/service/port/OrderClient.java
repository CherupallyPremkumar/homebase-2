package com.homebase.ecom.checkout.service.port;

import com.homebase.ecom.checkout.domain.model.OrderDetails;
import com.homebase.ecom.checkout.domain.model.Checkout;

public interface OrderClient {
    OrderDetails createOrder(Checkout checkout);
    void cancelOrder(String orderId);
}
