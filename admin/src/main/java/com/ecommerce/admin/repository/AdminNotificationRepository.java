package com.ecommerce.admin.repository;

import com.ecommerce.shared.domain.AdminNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminNotificationRepository extends JpaRepository<AdminNotification, String> {

    List<AdminNotification> findAllByOrderByCreatedAtDesc();

    List<AdminNotification> findByIsReadFalseOrderByCreatedAtDesc();

    long countByIsReadFalse();
}
