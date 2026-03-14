package com.homebase.ecom.checkout.infrastructure.persistence.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebase.ecom.checkout.domain.model.*;
import com.homebase.ecom.checkout.infrastructure.persistence.CheckoutEntity;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Component("checkoutRepositoryAdapter")
public class CheckoutRepositoryAdapter implements com.homebase.ecom.checkout.domain.repository.CheckoutRepository {

    private final com.homebase.ecom.checkout.infrastructure.persistence.CheckoutRepository jpaRepository;
    private final ObjectMapper objectMapper;

    public CheckoutRepositoryAdapter(com.homebase.ecom.checkout.infrastructure.persistence.CheckoutRepository jpaRepository, ObjectMapper objectMapper) {
        this.jpaRepository = jpaRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<Checkout> findById(UUID id) {
        return jpaRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public Optional<Checkout> findByIdempotencyKey(String idempotencyKey) {
        return jpaRepository.findByIdempotencyKey(idempotencyKey).map(this::mapToDomain);
    }

    @Override
    public void save(Checkout checkout) {
        CheckoutEntity entity = new CheckoutEntity();
        entity.setCheckoutId(checkout.getCheckoutId());
        entity.setCartId(checkout.getCartId());
        entity.setUserId(checkout.getUserId());
        entity.setOrderId(checkout.getOrderId());
        entity.setState(checkout.getState());
        entity.setIdempotencyKey(checkout.getIdempotencyKey());
        entity.setCreatedAt(checkout.getCreatedAt());
        entity.setCompletedAt(checkout.getCompletedAt());

        try {
            entity.setLockedCartJson(objectMapper.writeValueAsString(checkout.getLockedCart()));
            entity.setLockedPriceJson(objectMapper.writeValueAsString(checkout.getLockedPrice()));
            entity.setOrderDetailsJson(objectMapper.writeValueAsString(checkout.getOrderDetails()));
            entity.setPaymentDetailsJson(objectMapper.writeValueAsString(checkout.getPaymentDetails()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize checkout snapshots", e);
        }

        jpaRepository.save(entity);
    }

    private Checkout mapToDomain(CheckoutEntity entity) {
        Checkout checkout = new Checkout(entity.getCartId(), entity.getUserId(), entity.getIdempotencyKey());
        checkout.setCheckoutId(entity.getCheckoutId());
        checkout.setState(entity.getState());
        checkout.setOrderId(entity.getOrderId());
        checkout.setCreatedAt(entity.getCreatedAt());
        checkout.setCompletedAt(entity.getCompletedAt());

        try {
            if (entity.getLockedCartJson() != null) {
                checkout.setLockedCart(objectMapper.readValue(entity.getLockedCartJson(), CartSnapshot.class));
            }
            if (entity.getLockedPriceJson() != null) {
                checkout.setLockedPrice(objectMapper.readValue(entity.getLockedPriceJson(), PriceSnapshot.class));
            }
            if (entity.getOrderDetailsJson() != null) {
                checkout.setOrderDetails(objectMapper.readValue(entity.getOrderDetailsJson(), OrderDetails.class));
            }
            if (entity.getPaymentDetailsJson() != null) {
                checkout.setPaymentDetails(objectMapper.readValue(entity.getPaymentDetailsJson(), PaymentDetails.class));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize checkout snapshots", e);
        }

        return checkout;
    }
}
