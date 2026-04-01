package com.homebase.ecom.payment.domain.port;

import com.homebase.ecom.payment.domain.model.Payment;
import java.util.Optional;

/**
 * Port for payment persistence. Implemented by JPA adapter.
 * Note: STM uses EntityStore for persistence, this port is for query operations
 * outside the STM lifecycle.
 */
public interface PaymentRepositoryPort {

    Optional<Payment> findByOrderId(String orderId);

    Optional<Payment> findByGatewayTransactionId(String gatewayTransactionId);
}
