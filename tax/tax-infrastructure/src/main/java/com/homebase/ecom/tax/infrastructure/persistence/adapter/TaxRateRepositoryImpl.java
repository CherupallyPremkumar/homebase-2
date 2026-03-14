package com.homebase.ecom.tax.infrastructure.persistence.adapter;

import com.homebase.ecom.tax.model.TaxRate;
import com.homebase.ecom.tax.model.port.TaxRateRepository;
import com.homebase.ecom.tax.infrastructure.persistence.mapper.TaxMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaxRateRepositoryImpl implements TaxRateRepository {

    private final TaxRateJpaRepository jpaRepository;
    private final TaxMapper mapper;

    public TaxRateRepositoryImpl(TaxRateJpaRepository jpaRepository, TaxMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<TaxRate> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<TaxRate> findByRegionCode(String regionCode) {
        return jpaRepository.findByRegionCode(regionCode).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public TaxRate save(TaxRate taxRate) {
        return mapper.toModel(jpaRepository.save(mapper.toEntity(taxRate)));
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
