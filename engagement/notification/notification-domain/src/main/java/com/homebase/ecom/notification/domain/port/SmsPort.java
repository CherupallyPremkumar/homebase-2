package com.homebase.ecom.notification.domain.port;

import com.homebase.ecom.notification.domain.model.Notification;

/**
 * Port for sending SMS notifications.
 * Adapter implementations connect to Twilio, AWS SNS, etc.
 */
public interface SmsPort {

    /**
     * Send an SMS notification.
     * @param notification the notification with body, recipientAddress (phone number) populated
     * @throws NotificationDeliveryException if delivery fails
     */
    void send(Notification notification);
}
