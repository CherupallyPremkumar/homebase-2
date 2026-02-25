package com.ecommerce.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuditLogDto {
    private String logId;
    private String action;
    private String entityType;
    private String entityId;
    private String actor;
    private String details;
    private String status;
    private LocalDateTime timestamp;
}
