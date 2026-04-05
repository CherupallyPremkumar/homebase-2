package com.homebase.ecom.reconciliation.infrastructure.persistence.adapter;

import com.homebase.ecom.reconciliation.model.ReconciliationBatch;
import com.homebase.ecom.reconciliation.port.ReconciliationBatchRepository;
import com.homebase.ecom.reconciliation.infrastructure.persistence.mapper.ReconciliationBatchMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReconciliationBatchRepositoryImpl implements ReconciliationBatchRepository {

    private final ReconciliationBatchJpaRepository jpaRepository;
    private final ReconciliationBatchMapper mapper;

    public ReconciliationBatchRepositoryImpl(ReconciliationBatchJpaRepository jpaRepository, ReconciliationBatchMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<ReconciliationBatch> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public void save(ReconciliationBatch batch) {
        jpaRepository.save(mapper.toEntity(batch));
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<ReconciliationBatch> findByBatchDate(LocalDate batchDate) {
        return jpaRepository.findByBatchDate(batchDate).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReconciliationBatch> findByGatewayType(String gatewayType) {
        return jpaRepository.findByGatewayType(gatewayType).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }
}
