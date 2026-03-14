package com.homebase.ecom.checkout.domain.repository;

import java.util.UUID;
import java.time.LocalDateTime;

public interface SagaExecutionLogRepository {
    void log(UUID checkoutId, String stepName, String status, String errorMessage, String resultData);
}
