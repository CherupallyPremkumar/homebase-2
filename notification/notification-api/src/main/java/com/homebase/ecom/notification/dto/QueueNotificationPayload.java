package com.homebase.ecom.notification.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the queue event (CREATED -> QUEUED).
 * Triggers policy validation before queueing.
 */
public class QueueNotificationPayload extends MinimalPayload {
}
