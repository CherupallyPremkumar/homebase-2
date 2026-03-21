package com.homebase.ecom.payment.infrastructure.persistence.adapter;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.domain.port.PaymentRepositoryPort;
import com.homebase.ecom.payment.infrastructure.persistence.mapper.PaymentMapper;
import com.homebase.ecom.payment.infrastructure.persistence.repository.PaymentJpaRepository;

import java.util.Optional;

/**
 * Adapter implementing PaymentRepositoryPort for query operations outside STM lifecycle.
 */
public class PaymentQueryAdapter implements PaymentRepositoryPort {

    private final PaymentJpaRepository jpaRepository;
    private final PaymentMapper mapper;

    public PaymentQueryAdapter(PaymentJpaRepository jpaRepository, PaymentMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Payment> findByOrderId(String orderId) {
        return jpaRepository.findByOrderId(orderId).map(mapper::toModel);
    }

    @Override
    public Optional<Payment> findByGatewayTransactionId(String gatewayTransactionId) {
        return jpaRepository.findByGatewayTransactionId(gatewayTransactionId).map(mapper::toModel);
    }
}
