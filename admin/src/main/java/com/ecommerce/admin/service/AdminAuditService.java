package com.ecommerce.admin.service;

import com.ecommerce.admin.dto.AdminAuditLogDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AdminAuditService {

    private static final Logger log = LoggerFactory.getLogger(AdminAuditService.class);

    // In a real implementation, this would save to a database.
    private final List<AdminAuditLogDto> inMemoryLogs = new ArrayList<>();

    public void logAction(String action, String entityType, String entityId, String actor, String details) {
        AdminAuditLogDto auditLog = AdminAuditLogDto.builder()
                .logId(UUID.randomUUID().toString())
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .actor(actor)
                .details(details)
                .status("SUCCESS")
                .timestamp(LocalDateTime.now())
                .build();

        log.info("ADMIN_AUDIT: {} by {} on {} {}: {}", action, actor, entityType, entityId, details);

        // Simulating database storage
        inMemoryLogs.add(auditLog);
    }

    public List<AdminAuditLogDto> getRecentLogs() {
        return new ArrayList<>(inMemoryLogs);
    }
}
