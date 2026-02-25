package com.ecommerce.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "com.ecommerce.admin.controller")
public class AdminControllerAdvice {

    @org.springframework.beans.factory.annotation.Value("${app.features.admin.analytics:false}")
    private boolean adminAnalyticsEnabled;

    @org.springframework.beans.factory.annotation.Value("${app.features.admin.payments:false}")
    private boolean adminPaymentsEnabled;

    @org.springframework.beans.factory.annotation.Value("${app.features.admin.refunds:false}")
    private boolean adminRefundsEnabled;

    @org.springframework.beans.factory.annotation.Value("${app.features.admin.audit:true}")
    private boolean adminAuditEnabled;

    @org.springframework.beans.factory.annotation.Value("${app.features.admin.notifications:true}")
    private boolean adminNotificationsEnabled;

    @ModelAttribute("currentUri")
    public String currentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("adminAnalyticsEnabled")
    public boolean adminAnalyticsEnabled() {
        return adminAnalyticsEnabled;
    }

    @ModelAttribute("adminPaymentsEnabled")
    public boolean adminPaymentsEnabled() {
        return adminPaymentsEnabled;
    }

    @ModelAttribute("adminRefundsEnabled")
    public boolean adminRefundsEnabled() {
        return adminRefundsEnabled;
    }

    @ModelAttribute("adminAuditEnabled")
    public boolean adminAuditEnabled() {
        return adminAuditEnabled;
    }

    @ModelAttribute("adminNotificationsEnabled")
    public boolean adminNotificationsEnabled() {
        return adminNotificationsEnabled;
    }
}
