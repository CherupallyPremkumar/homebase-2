package com.homebase.ecom.checkout.service.port;

import com.homebase.ecom.checkout.domain.model.Checkout;

public interface PaymentClient {
    void initiatePayment(Checkout checkout);
}
