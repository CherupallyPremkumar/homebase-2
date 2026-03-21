package com.homebase.ecom.payment.infrastructure.persistence.repository;

import com.homebase.ecom.payment.infrastructure.persistence.entity.WebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WebhookEventRepository extends JpaRepository<WebhookEvent, String> {

    boolean existsByGatewayEventId(String gatewayEventId);

    Optional<WebhookEvent> findByGatewayEventId(String gatewayEventId);

    List<WebhookEvent> findByProcessedFalseOrderByCreatedTimeAsc();
}
