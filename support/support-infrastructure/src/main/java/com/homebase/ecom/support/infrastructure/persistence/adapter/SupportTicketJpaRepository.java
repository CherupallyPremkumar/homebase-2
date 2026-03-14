package com.homebase.ecom.support.infrastructure.persistence.adapter;

import com.homebase.ecom.support.infrastructure.persistence.entity.SupportTicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportTicketJpaRepository extends JpaRepository<SupportTicketEntity, String> {
}
