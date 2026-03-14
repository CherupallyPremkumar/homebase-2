package com.homebase.ecom.checkout.domain.service;

import com.homebase.ecom.checkout.domain.model.CartSnapshot;
import com.homebase.ecom.checkout.domain.model.PriceSnapshot;
import com.homebase.ecom.checkout.domain.model.OrderDetails;
import java.util.UUID;

public interface IOrderService {
    OrderDetails createOrder(UUID userId, CartSnapshot cart, PriceSnapshot price, String couponCode);
    void cancelOrder(UUID orderId);
}
