package com.homebase.ecom.notification.infrastructure.persistence;

import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.infrastructure.persistence.adapter.NotificationJpaRepository;
import com.homebase.ecom.notification.infrastructure.persistence.entity.NotificationEntity;
import com.homebase.ecom.notification.infrastructure.persistence.mapper.NotificationMapper;
import org.chenile.jpautils.store.ChenileJpaEntityStore;

public class ChenileNotificationEntityStore extends ChenileJpaEntityStore<Notification, NotificationEntity> {

    public ChenileNotificationEntityStore(NotificationJpaRepository repository, NotificationMapper mapper) {
        super(repository, (entity) -> mapper.toModel(entity), (model) -> mapper.toEntity(model),
                (existing, updated) -> mapper.mergeEntity(existing, updated));
    }
}
