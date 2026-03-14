package com.homebase.ecom.tax.infrastructure.persistence.adapter;

import com.homebase.ecom.tax.model.OrderTaxLine;
import com.homebase.ecom.tax.model.port.OrderTaxLineRepository;
import com.homebase.ecom.tax.infrastructure.persistence.mapper.TaxMapper;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTaxLineRepositoryImpl implements OrderTaxLineRepository {

    private final OrderTaxLineJpaRepository jpaRepository;
    private final TaxMapper mapper;

    public OrderTaxLineRepositoryImpl(OrderTaxLineJpaRepository jpaRepository, TaxMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<OrderTaxLine> findByOrderId(String orderId) {
        return jpaRepository.findByOrderId(orderId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public OrderTaxLine save(OrderTaxLine orderTaxLine) {
        return mapper.toModel(jpaRepository.save(mapper.toEntity(orderTaxLine)));
    }
}
