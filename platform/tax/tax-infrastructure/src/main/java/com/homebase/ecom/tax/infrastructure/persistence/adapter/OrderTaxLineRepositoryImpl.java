package com.homebase.ecom.tax.infrastructure.persistence.adapter;

import com.homebase.ecom.tax.model.OrderTaxLine;
import com.homebase.ecom.tax.model.port.OrderTaxLineRepository;
import com.homebase.ecom.tax.infrastructure.persistence.mapper.TaxMapper;
import java.util.List;
import java.util.stream.Collectors;

public class OrderTaxLineRepositoryImpl implements OrderTaxLineRepository {
    private final OrderTaxLineJpaRepository jpa;
    private final TaxMapper mapper;
    public OrderTaxLineRepositoryImpl(OrderTaxLineJpaRepository jpa, TaxMapper mapper) { this.jpa = jpa; this.mapper = mapper; }

    @Override public OrderTaxLine save(OrderTaxLine line) { return mapper.toModel(jpa.save(mapper.toEntity(line))); }
    @Override public List<OrderTaxLine> findByOrderId(String orderId) { return jpa.findByOrderId(orderId).stream().map(mapper::toModel).collect(Collectors.toList()); }
}
