package com.homebase.ecom.checkout.domain.saga;

public class RetryPolicy {
    private final int maxRetries;
    private final long initialDelayMs;
    private final double backoffMultiplier;

    public RetryPolicy(int maxRetries, long initialDelayMs, double backoffMultiplier) {
        this.maxRetries = maxRetries;
        this.initialDelayMs = initialDelayMs;
        this.backoffMultiplier = backoffMultiplier;
    }

    public int getMaxRetries() { return maxRetries; }
    public long getInitialDelayMs() { return initialDelayMs; }
    public double getBackoffMultiplier() { return backoffMultiplier; }

    public static RetryPolicyBuilder builder() {
        return new RetryPolicyBuilder();
    }

    public static class RetryPolicyBuilder {
        private int maxRetries;
        private long initialDelayMs;
        private double backoffMultiplier;

        public RetryPolicyBuilder maxRetries(int maxRetries) { this.maxRetries = maxRetries; return this; }
        public RetryPolicyBuilder initialDelayMs(long initialDelayMs) { this.initialDelayMs = initialDelayMs; return this; }
        public RetryPolicyBuilder backoffMultiplier(double backoffMultiplier) { this.backoffMultiplier = backoffMultiplier; return this; }
        public RetryPolicy build() { return new RetryPolicy(maxRetries, initialDelayMs, backoffMultiplier); }
    }
}
