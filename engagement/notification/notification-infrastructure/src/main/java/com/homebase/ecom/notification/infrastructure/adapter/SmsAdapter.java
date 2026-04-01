package com.homebase.ecom.notification.infrastructure.adapter;

import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.domain.port.SmsPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SMS channel adapter. In production, connects to Twilio/AWS SNS.
 * Current implementation is a no-op stub for development.
 */
public class SmsAdapter implements SmsPort {

    private static final Logger log = LoggerFactory.getLogger(SmsAdapter.class);

    @Override
    public void send(Notification notification) {
        log.info("SMS dispatch: to={}, body length={}, templateId={}",
                notification.getRecipientAddress(),
                notification.getBody() != null ? notification.getBody().length() : 0,
                notification.getTemplateId());
        // Production: integrate with Twilio/SNS
    }
}
