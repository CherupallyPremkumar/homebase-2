package com.homebase.ecom.checkout.infrastructure.client.adapter;

import com.homebase.ecom.checkout.domain.model.Checkout;
import com.homebase.ecom.checkout.domain.model.PaymentDetails;
import com.homebase.ecom.checkout.infrastructure.client.PaymentServiceClient;
import com.homebase.ecom.checkout.service.port.PaymentClient;
import org.springframework.stereotype.Component;

@Component
public class PaymentClientAdapter implements PaymentClient {
    private final PaymentServiceClient paymentServiceClient;

    public PaymentClientAdapter(PaymentServiceClient paymentServiceClient) {
        this.paymentServiceClient = paymentServiceClient;
    }

    @Override
    public void initiatePayment(Checkout checkout) {
        PaymentDetails details = paymentServiceClient.createPaymentSession(
            checkout.getOrderId(),
            checkout.getLockedPrice().getFinalTotal(),
            checkout.getUserId()
        );
        checkout.setPaymentDetails(details);
    }
}
