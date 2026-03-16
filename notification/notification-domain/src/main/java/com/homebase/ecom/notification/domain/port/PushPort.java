package com.homebase.ecom.notification.domain.port;

import com.homebase.ecom.notification.domain.model.Notification;

/**
 * Port for sending push notifications.
 * Adapter implementations connect to Firebase FCM, APNs, etc.
 */
public interface PushPort {

    /**
     * Send a push notification.
     * @param notification the notification with subject, body, recipientAddress (device token) populated
     * @throws NotificationDeliveryException if delivery fails
     */
    void send(Notification notification);
}
