package com.ecommerce.admin.controller;

import com.ecommerce.shared.domain.AuditLog;
import com.ecommerce.admin.repository.AuditLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/audit")
public class AdminAuditController {

    private final AuditLogRepository auditLogRepository;

    public AdminAuditController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @GetMapping
    public String auditLog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String entityType,
            Model model) {

        Page<AuditLog> logs;
        if (entityType != null && !entityType.isEmpty()) {
            logs = auditLogRepository.findByEntityTypeOrderByCreatedAtDesc(entityType, PageRequest.of(page, size));
        } else {
            logs = auditLogRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
        }

        model.addAttribute("logs", logs.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", logs.getTotalPages());
        model.addAttribute("entityType", entityType);

        return "admin/audit/list";
    }
}
