package com.ecommerce.admin.controller;

import com.ecommerce.admin.service.AdminNotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/notifications")
public class AdminNotificationController {

    private final AdminNotificationService notificationService;

    public AdminNotificationController(AdminNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public String listNotifications(Model model) {
        model.addAttribute("notifications", notificationService.getAllNotifications());
        model.addAttribute("unreadCount", notificationService.getUnreadCount());
        return "admin/notifications/list";
    }

    @PostMapping("/{id}/read")
    public String markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
        return "redirect:/admin/notifications";
    }

    @PostMapping("/read-all")
    public String markAllAsRead() {
        notificationService.markAllAsRead();
        return "redirect:/admin/notifications";
    }

    @GetMapping("/count")
    @ResponseBody
    public long getUnreadCount() {
        return notificationService.getUnreadCount();
    }
}
