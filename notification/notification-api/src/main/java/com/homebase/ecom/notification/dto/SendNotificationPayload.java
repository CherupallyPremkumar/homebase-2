package com.homebase.ecom.notification.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the send event in the notification STM (QUEUED -> SENDING).
 * Triggers actual dispatch via channel adapter.
 */
public class SendNotificationPayload extends MinimalPayload {
}
