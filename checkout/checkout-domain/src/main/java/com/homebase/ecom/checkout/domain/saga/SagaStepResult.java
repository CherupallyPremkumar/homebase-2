package com.homebase.ecom.checkout.domain.saga;

public class SagaStepResult {
    private final String stepName;
    private final boolean success;
    private final String errorMessage;
    private final Object data;

    public SagaStepResult(String stepName, boolean success, String errorMessage, Object data) {
        this.stepName = stepName;
        this.success = success;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public String getStepName() { return stepName; }
    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }
    public Object getData() { return data; }

    // Manual Builder Pattern
    public static SagaStepResultBuilder builder() {
        return new SagaStepResultBuilder();
    }

    public static class SagaStepResultBuilder {
        private String stepName;
        private boolean success;
        private String errorMessage;
        private Object data;

        public SagaStepResultBuilder stepName(String stepName) { this.stepName = stepName; return this; }
        public SagaStepResultBuilder success(boolean success) { this.success = success; return this; }
        public SagaStepResultBuilder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }
        public SagaStepResultBuilder data(Object data) { this.data = data; return this; }
        public SagaStepResult build() { return new SagaStepResult(stepName, success, errorMessage, data); }
    }
}
