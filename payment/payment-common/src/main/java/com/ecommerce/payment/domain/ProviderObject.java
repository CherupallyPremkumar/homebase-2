package com.ecommerce.payment.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "provider_objects", uniqueConstraints = {
        @UniqueConstraint(name = "ux_provider_objects_gateway_object_provider_id", columnNames = {
                "gateway_type", "object_type", "provider_object_id"
        })
})
public class ProviderObject {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "gateway_type", nullable = false, length = 20)
    private String gatewayType;

    @Column(name = "object_type", nullable = false, length = 50)
    private String objectType;

    @Column(name = "provider_object_id", nullable = false, length = 255)
    private String providerObjectId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private String payload;

    @Column(name = "fetched_at", nullable = false)
    private LocalDateTime fetchedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (fetchedAt == null) {
            fetchedAt = LocalDateTime.now();
        }
        createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(String gatewayType) {
        this.gatewayType = gatewayType;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getProviderObjectId() {
        return providerObjectId;
    }

    public void setProviderObjectId(String providerObjectId) {
        this.providerObjectId = providerObjectId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public LocalDateTime getFetchedAt() {
        return fetchedAt;
    }

    public void setFetchedAt(LocalDateTime fetchedAt) {
        this.fetchedAt = fetchedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
