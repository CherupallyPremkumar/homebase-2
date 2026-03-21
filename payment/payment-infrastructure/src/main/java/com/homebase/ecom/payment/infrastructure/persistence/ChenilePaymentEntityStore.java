package com.homebase.ecom.payment.infrastructure.persistence;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.infrastructure.persistence.repository.PaymentJpaRepository;
import com.homebase.ecom.payment.infrastructure.persistence.entity.PaymentEntity;
import com.homebase.ecom.payment.infrastructure.persistence.mapper.PaymentMapper;
import org.chenile.jpautils.store.ChenileJpaEntityStore;

public class ChenilePaymentEntityStore extends ChenileJpaEntityStore<Payment, PaymentEntity> {

    public ChenilePaymentEntityStore(PaymentJpaRepository repository, PaymentMapper mapper) {
        super(repository, (entity) -> mapper.toModel(entity), (model) -> mapper.toEntity(model));
    }
}
