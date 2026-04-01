package com.homebase.ecom.tax.infrastructure.persistence.adapter;

import com.homebase.ecom.tax.model.TaxRate;
import com.homebase.ecom.tax.model.port.TaxRateRepository;
import com.homebase.ecom.tax.infrastructure.persistence.mapper.TaxMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaxRateRepositoryImpl implements TaxRateRepository {
    private final TaxRateJpaRepository jpa;
    private final TaxMapper mapper;
    public TaxRateRepositoryImpl(TaxRateJpaRepository jpa, TaxMapper mapper) { this.jpa = jpa; this.mapper = mapper; }

    @Override public Optional<TaxRate> findById(String id) { return jpa.findById(id).map(mapper::toModel); }
    @Override public Optional<TaxRate> findByHsnCode(String hsnCode) { return jpa.findByHsnCode(hsnCode).map(mapper::toModel); }
    @Override public Optional<TaxRate> findActiveByHsnCode(String hsnCode) { return jpa.findByHsnCodeAndActiveTrue(hsnCode).map(mapper::toModel); }
    @Override public List<TaxRate> findByRegionCode(String regionCode) { return jpa.findByActiveTrue().stream().map(mapper::toModel).collect(Collectors.toList()); }
    @Override public TaxRate save(TaxRate rate) { return mapper.toModel(jpa.save(mapper.toEntity(rate))); }
    @Override public void delete(String id) { jpa.deleteById(id); }
}
