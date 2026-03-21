package com.homebase.ecom.user.infrastructure.adapter;

import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.service.NotificationService;
import com.homebase.ecom.user.domain.port.NotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Infrastructure adapter for NotificationPort.
 * Dispatches notifications via the notification service (Chenile proxy).
 *
 * Creates notification entities through the NotificationService STM,
 * which handles template resolution, channel routing, and delivery tracking.
 *
 * No @Component -- wired in UserInfrastructureConfiguration.
 */
public class NotificationAdapter implements NotificationPort {

    private static final Logger log = LoggerFactory.getLogger(NotificationAdapter.class);

    private static final String CHANNEL_EMAIL = "EMAIL";
    private static final String PRIORITY_HIGH = "HIGH";
    private static final String PRIORITY_NORMAL = "NORMAL";
    private static final String REFERENCE_TYPE_USER = "USER";

    private final NotificationService notificationService;

    public NotificationAdapter(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void notifyUserVerified(String userId, String email) {
        log.info("Notification: user {} ({}) verified, features enabled", userId, email);
        // TODO: Send via notification service
        try {
            Notification notification = buildNotification(
                    userId, email,
                    "user-verified",
                    "Welcome! Your account is verified",
                    "Your HomeBase account has been verified successfully. "
                            + "You can now access all features of the platform.",
                    PRIORITY_NORMAL,
                    Map.of("event", "USER_VERIFIED")
            );
            notificationService.create(notification);
            log.info("User verified notification created for user {}", userId);
        } catch (Exception e) {
            log.error("Failed to send user-verified notification for user {}: {}",
                    userId, e.getMessage(), e);
        }
    }

    @Override
    public void notifyUserSuspended(String userId, String email, String reason) {
        log.info("Notification: user {} ({}) suspended. Reason: {}", userId, email, reason);
        // TODO: Send via notification service
        try {
            Notification notification = buildNotification(
                    userId, email,
                    "user-suspended",
                    "Your account has been suspended",
                    "Your HomeBase account has been suspended. Reason: " + reason
                            + ". Please contact support for assistance.",
                    PRIORITY_HIGH,
                    Map.of("event", "USER_SUSPENDED", "reason", reason)
            );
            notificationService.create(notification);
            log.info("User suspended notification created for user {}", userId);
        } catch (Exception e) {
            log.error("Failed to send user-suspended notification for user {}: {}",
                    userId, e.getMessage(), e);
        }
    }

    @Override
    public void notifyUserDeactivated(String userId, String email) {
        log.info("Notification: user {} ({}) deactivated", userId, email);
        // TODO: Send via notification service
        try {
            Notification notification = buildNotification(
                    userId, email,
                    "user-deactivated",
                    "Your account has been deactivated",
                    "Your HomeBase account has been deactivated as requested. "
                            + "You can reactivate your account by logging in again.",
                    PRIORITY_NORMAL,
                    Map.of("event", "USER_DEACTIVATED")
            );
            notificationService.create(notification);
            log.info("User deactivated notification created for user {}", userId);
        } catch (Exception e) {
            log.error("Failed to send user-deactivated notification for user {}: {}",
                    userId, e.getMessage(), e);
        }
    }

    @Override
    public void notifyKycVerified(String userId, String email) {
        log.info("Notification: user {} ({}) KYC verified", userId, email);
        // TODO: Send via notification service
        try {
            Notification notification = buildNotification(
                    userId, email,
                    "kyc-verified",
                    "KYC verification complete",
                    "Your KYC documents have been verified successfully. "
                            + "You now have full access to all HomeBase features.",
                    PRIORITY_NORMAL,
                    Map.of("event", "KYC_VERIFIED")
            );
            notificationService.create(notification);
            log.info("KYC verified notification created for user {}", userId);
        } catch (Exception e) {
            log.error("Failed to send kyc-verified notification for user {}: {}",
                    userId, e.getMessage(), e);
        }
    }

    /**
     * Builds a Notification domain entity with standard fields for user notifications.
     * The notification STM will handle state transitions (CREATED -> QUEUED -> DISPATCHING -> SENT).
     */
    private Notification buildNotification(String userId, String email,
                                           String templateId, String subject,
                                           String body, String priority,
                                           Map<String, String> eventMetadata) {
        Notification notification = new Notification();
        notification.setCustomerId(userId);
        notification.setChannel(CHANNEL_EMAIL);
        notification.setTemplateId(templateId);
        notification.setSubject(subject);
        notification.setBody(body);
        notification.setRecipientAddress(email);
        notification.setPriority(priority);
        notification.setReferenceType(REFERENCE_TYPE_USER);
        notification.setReferenceId(userId);
        notification.setMetadata(eventMetadata);
        return notification;
    }
}
