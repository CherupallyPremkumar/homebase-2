package com.ecommerce.payment.repository;

import com.ecommerce.payment.domain.WebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WebhookEventRepository extends JpaRepository<WebhookEvent, String> {

    boolean existsByGatewayEventId(String gatewayEventId);

    Optional<WebhookEvent> findByGatewayEventId(String gatewayEventId);

    List<WebhookEvent> findByProcessedFalseOrderByCreatedAtAsc();
}
