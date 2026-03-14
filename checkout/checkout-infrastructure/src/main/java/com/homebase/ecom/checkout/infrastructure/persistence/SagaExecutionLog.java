package com.homebase.ecom.checkout.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "saga_execution_logs")
public class SagaExecutionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID checkoutId;
    private String stepName;
    private String status;
    private LocalDateTime executedAt;

    @Column(columnDefinition = "TEXT")
    private String resultData;
    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    // Manual Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UUID getCheckoutId() { return checkoutId; }
    public void setCheckoutId(UUID checkoutId) { this.checkoutId = checkoutId; }

    public String getStepName() { return stepName; }
    public void setStepName(String stepName) { this.stepName = stepName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getExecutedAt() { return executedAt; }
    public void setExecutedAt(LocalDateTime executedAt) { this.executedAt = executedAt; }

    public String getResultData() { return resultData; }
    public void setResultData(String resultData) { this.resultData = resultData; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
