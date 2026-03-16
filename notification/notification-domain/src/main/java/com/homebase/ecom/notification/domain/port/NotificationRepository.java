package com.homebase.ecom.notification.domain.port;

import java.util.List;
import java.util.Optional;
import com.homebase.ecom.notification.domain.model.Notification;

/**
 * Repository port for notification persistence.
 */
public interface NotificationRepository {
    Optional<Notification> findById(String id);
    List<Notification> findByCustomerId(String customerId);
    void save(Notification notification);
    void delete(String id);
}
