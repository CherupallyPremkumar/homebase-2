package com.homebase.ecom.promo.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

public final class RuleMetadata implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int priority; // 0-100
    private final StackabilityType stackability;
    private final int maxUsageCount;
    private final String createdBy;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Set<String> tags;

    private RuleMetadata(Builder builder) {
        this.priority = builder.priority;
        this.stackability = builder.stackability;
        this.maxUsageCount = builder.maxUsageCount;
        this.createdBy = builder.createdBy;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.tags = builder.tags;
    }

    public int getPriority() {
        return priority;
    }

    public StackabilityType getStackability() {
        return stackability;
    }

    public int getMaxUsageCount() {
        return maxUsageCount;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Set<String> getTags() {
        return tags;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int priority;
        private StackabilityType stackability;
        private int maxUsageCount;
        private String createdBy;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Set<String> tags;

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder stackability(StackabilityType stackability) {
            this.stackability = stackability;
            return this;
        }

        public Builder maxUsageCount(int maxUsageCount) {
            this.maxUsageCount = maxUsageCount;
            return this;
        }

        public Builder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder startDate(LocalDateTime startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDateTime endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder tags(Set<String> tags) {
            this.tags = tags;
            return this;
        }

        public RuleMetadata build() {
            return new RuleMetadata(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RuleMetadata that = (RuleMetadata) o;
        return priority == that.priority &&
                maxUsageCount == that.maxUsageCount &&
                stackability == that.stackability &&
                java.util.Objects.equals(createdBy, that.createdBy) &&
                java.util.Objects.equals(createdAt, that.createdAt) &&
                java.util.Objects.equals(updatedAt, that.updatedAt) &&
                java.util.Objects.equals(startDate, that.startDate) &&
                java.util.Objects.equals(endDate, that.endDate) &&
                java.util.Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(priority, stackability, maxUsageCount, createdBy, createdAt, updatedAt, startDate,
                endDate, tags);
    }
}
