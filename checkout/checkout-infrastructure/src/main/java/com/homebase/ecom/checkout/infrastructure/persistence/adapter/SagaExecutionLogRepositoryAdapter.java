package com.homebase.ecom.checkout.infrastructure.persistence.adapter;

import com.homebase.ecom.checkout.infrastructure.persistence.SagaExecutionLog;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.UUID;

@Component("sagaExecutionLogRepositoryAdapter")
public class SagaExecutionLogRepositoryAdapter implements com.homebase.ecom.checkout.domain.repository.SagaExecutionLogRepository {

    private final com.homebase.ecom.checkout.infrastructure.persistence.SagaExecutionLogRepository jpaRepository;

    public SagaExecutionLogRepositoryAdapter(com.homebase.ecom.checkout.infrastructure.persistence.SagaExecutionLogRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void log(UUID checkoutId, String stepName, String status, String errorMessage, String resultData) {
        SagaExecutionLog entity = new SagaExecutionLog();
        entity.setCheckoutId(checkoutId);
        entity.setStepName(stepName);
        entity.setStatus(status);
        entity.setErrorMessage(errorMessage);
        entity.setResultData(resultData);
        entity.setExecutedAt(LocalDateTime.now());

        jpaRepository.save(entity);
    }
}
