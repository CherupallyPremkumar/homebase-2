package com.homebase.ecom.notification.infrastructure.adapter;

import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.domain.port.EmailPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Email channel adapter. In production, connects to SMTP/SendGrid/SES.
 * Current implementation is a no-op stub for development.
 */
public class EmailAdapter implements EmailPort {

    private static final Logger log = LoggerFactory.getLogger(EmailAdapter.class);

    @Override
    public void send(Notification notification) {
        log.info("EMAIL dispatch: to={}, subject={}, templateId={}",
                notification.getRecipientAddress(),
                notification.getSubject(),
                notification.getTemplateId());
        // Production: integrate with SMTP/SendGrid/SES
    }
}
