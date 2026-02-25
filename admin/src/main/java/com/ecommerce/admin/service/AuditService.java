package com.ecommerce.admin.service;

import com.ecommerce.admin.repository.AuditLogRepository;
import com.ecommerce.shared.domain.AuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private static final Logger log = LoggerFactory.getLogger(AuditService.class);
    private static final String AUDIT_TOPIC = "audit.events";

    private final AuditLogRepository auditLogRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public AuditService(AuditLogRepository auditLogRepository,
            KafkaTemplate<String, Object> kafkaTemplate) {
        this.auditLogRepository = auditLogRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Log an admin action. Persists to DB and publishes to Kafka.
     */
    public void logAction(String action, String entityType, String entityId, String details) {
        String performedBy = resolveCurrentUser();
        logAction(action, entityType, entityId, performedBy, details, null);
    }

    public void logAction(String action, String entityType, String entityId,
            String performedBy, String details, String ipAddress) {
        try {
            AuditLog entry = new AuditLog();
            entry.setAction(action);
            entry.setEntityType(entityType);
            entry.setEntityId(entityId);
            entry.setPerformedBy(performedBy);
            entry.setDetails(details);
            entry.setIpAddress(ipAddress);

            auditLogRepository.save(entry);

            // Publish to Kafka for downstream consumers
            try {
                kafkaTemplate.send(AUDIT_TOPIC, entityType + ":" + entityId,
                        String.format(
                                "{\"action\":\"%s\",\"entityType\":\"%s\",\"entityId\":\"%s\",\"performedBy\":\"%s\",\"details\":\"%s\"}",
                                action, entityType, entityId, performedBy,
                                details != null ? details.replace("\"", "'") : ""));
            } catch (Exception kafkaEx) {
                log.warn("Failed to publish audit event to Kafka (non-critical): {}", kafkaEx.getMessage());
            }

            log.info("AUDIT: {} performed {} on {}:{} — {}", performedBy, action, entityType, entityId, details);
        } catch (Exception e) {
            log.error("Failed to create audit log entry: {}", e.getMessage());
        }
    }

    private String resolveCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        return "SYSTEM";
    }
}
