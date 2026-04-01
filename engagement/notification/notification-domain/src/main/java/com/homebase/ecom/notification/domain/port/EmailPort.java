package com.homebase.ecom.notification.domain.port;

import com.homebase.ecom.notification.domain.model.Notification;

/**
 * Port for sending email notifications.
 * Adapter implementations connect to SMTP, SendGrid, SES, etc.
 */
public interface EmailPort {

    /**
     * Send an email notification.
     * @param notification the notification with subject, body, recipientAddress populated
     * @throws NotificationDeliveryException if delivery fails
     */
    void send(Notification notification);
}
