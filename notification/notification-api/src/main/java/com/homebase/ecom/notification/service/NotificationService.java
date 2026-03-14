package com.homebase.ecom.notification.service;

import org.chenile.workflow.api.StateEntityService;
import com.homebase.ecom.notification.domain.model.Notification;

/**
 * STM-based service interface for notification delivery tracking.
 */
public interface NotificationService extends StateEntityService<Notification> {
}
