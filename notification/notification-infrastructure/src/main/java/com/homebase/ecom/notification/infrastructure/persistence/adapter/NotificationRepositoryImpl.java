package com.homebase.ecom.notification.infrastructure.persistence.adapter;

import java.util.Optional;
import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.domain.port.NotificationRepository;
import com.homebase.ecom.notification.infrastructure.persistence.entity.NotificationEntity;
import com.homebase.ecom.notification.infrastructure.persistence.mapper.NotificationMapper;

public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository jpaRepository;
    private final NotificationMapper mapper;

    public NotificationRepositoryImpl(NotificationJpaRepository jpaRepository, NotificationMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Notification> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public void save(Notification notification) {
        NotificationEntity entity = mapper.toEntity(notification);
        jpaRepository.save(entity);
        notification.setId(entity.getId());
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
