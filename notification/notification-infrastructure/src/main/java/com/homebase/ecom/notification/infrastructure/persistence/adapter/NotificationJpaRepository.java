package com.homebase.ecom.notification.infrastructure.persistence.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import com.homebase.ecom.notification.infrastructure.persistence.entity.NotificationEntity;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, String> {
}
