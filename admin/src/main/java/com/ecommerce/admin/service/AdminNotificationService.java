package com.ecommerce.admin.service;

import com.ecommerce.shared.domain.AdminNotification;
import com.ecommerce.admin.repository.AdminNotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminNotificationService {

    private static final Logger log = LoggerFactory.getLogger(AdminNotificationService.class);

    private final AdminNotificationRepository notificationRepository;

    public AdminNotificationService(AdminNotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createNotification(String type, String title, String message, String link) {
        AdminNotification notification = new AdminNotification();
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setLink(link);
        notificationRepository.save(notification);
        log.info("Admin notification created: [{}] {}", type, title);
    }

    public void notifyNewOrder(String orderId, String amount) {
        createNotification("ORDER", "New Order Received",
                "Order ₹" + amount + " — click to view details",
                "/admin/orders/" + orderId);
    }

    public void notifyRefundRequest(String orderId) {
        createNotification("REFUND", "New Refund Request",
                "A customer has requested a refund for their order.",
                "/admin/refunds");
    }

    public void notifyPaymentFailure(String orderId) {
        createNotification("ALERT", "Payment Failed",
                "A payment attempt failed for an order.",
                "/admin/orders/" + orderId);
    }

    public void notifyLowStock(String productName, int quantity) {
        createNotification("INVENTORY", "Low Stock Alert",
                productName + " has only " + quantity + " units left.",
                "/admin/inventory/alerts/low-stock");
    }

    public List<AdminNotification> getAllNotifications() {
        return notificationRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<AdminNotification> getUnreadNotifications() {
        return notificationRepository.findByIsReadFalseOrderByCreatedAtDesc();
    }

    public long getUnreadCount() {
        return notificationRepository.countByIsReadFalse();
    }

    public void markAsRead(String notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }

    public void markAllAsRead() {
        List<AdminNotification> unread = notificationRepository.findByIsReadFalseOrderByCreatedAtDesc();
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }
}
