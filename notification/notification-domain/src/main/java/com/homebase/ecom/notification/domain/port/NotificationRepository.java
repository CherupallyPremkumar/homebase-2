package com.homebase.ecom.notification.domain.port;

import java.util.Optional;
import com.homebase.ecom.notification.domain.model.Notification;

public interface NotificationRepository {
    Optional<Notification> findById(String id);
    void save(Notification notification);
    void delete(String id);
}
