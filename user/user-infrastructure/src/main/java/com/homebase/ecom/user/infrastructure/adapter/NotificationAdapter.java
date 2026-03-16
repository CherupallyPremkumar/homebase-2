package com.homebase.ecom.user.infrastructure.adapter;

import com.homebase.ecom.user.domain.port.NotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Infrastructure adapter for NotificationPort.
 * Dispatches notifications via the notification service or direct email/SMS.
 * Currently logs operations; real integration added when notification-client is configured.
 *
 * No @Component -- wired in UserConfiguration.
 */
public class NotificationAdapter implements NotificationPort {

    private static final Logger log = LoggerFactory.getLogger(NotificationAdapter.class);

    @Override
    public void notifyUserVerified(String userId, String email) {
        log.info("Notification: user {} ({}) verified, features enabled", userId, email);
        // TODO: Send via notification service
    }

    @Override
    public void notifyUserSuspended(String userId, String email, String reason) {
        log.info("Notification: user {} ({}) suspended. Reason: {}", userId, email, reason);
        // TODO: Send via notification service
    }

    @Override
    public void notifyUserDeactivated(String userId, String email) {
        log.info("Notification: user {} ({}) deactivated", userId, email);
        // TODO: Send via notification service
    }

    @Override
    public void notifyKycVerified(String userId, String email) {
        log.info("Notification: user {} ({}) KYC verified", userId, email);
        // TODO: Send via notification service
    }
}
