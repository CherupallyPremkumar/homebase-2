package com.homebase.ecom.checkout.domain.service;

import com.homebase.ecom.checkout.domain.model.PaymentDetails;
import java.math.BigDecimal;
import java.util.UUID;

public interface IPaymentService {
    PaymentDetails createPaymentSession(UUID orderId, BigDecimal amount, UUID userId);
}
