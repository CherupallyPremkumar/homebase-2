package com.homebase.ecom.notification.infrastructure.adapter;

import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.domain.port.PushPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Push notification channel adapter. In production, connects to Firebase FCM / APNs.
 * Current implementation is a no-op stub for development.
 */
public class PushAdapter implements PushPort {

    private static final Logger log = LoggerFactory.getLogger(PushAdapter.class);

    @Override
    public void send(Notification notification) {
        log.info("PUSH dispatch: to={}, subject={}, templateId={}",
                notification.getRecipientAddress(),
                notification.getSubject(),
                notification.getTemplateId());
        // Production: integrate with Firebase FCM / APNs
    }
}
