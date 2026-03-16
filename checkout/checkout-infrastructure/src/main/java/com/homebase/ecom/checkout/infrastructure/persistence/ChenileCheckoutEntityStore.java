package com.homebase.ecom.checkout.infrastructure.persistence;

import com.homebase.ecom.checkout.model.Checkout;
import com.homebase.ecom.checkout.infrastructure.persistence.entity.CheckoutEntity;
import com.homebase.ecom.checkout.infrastructure.persistence.repository.CheckoutJpaRepository;
import com.homebase.ecom.checkout.infrastructure.persistence.mapper.CheckoutMapper;
import org.chenile.jpautils.store.ChenileJpaEntityStore;

public class ChenileCheckoutEntityStore extends ChenileJpaEntityStore<Checkout, CheckoutEntity> {

    public ChenileCheckoutEntityStore(CheckoutJpaRepository repository, CheckoutMapper mapper) {
        super(repository, (entity) -> mapper.toModel(entity), (model) -> mapper.toEntity(model),
                (existing, updated) -> mapper.mergeEntity(existing, updated));
    }
}
