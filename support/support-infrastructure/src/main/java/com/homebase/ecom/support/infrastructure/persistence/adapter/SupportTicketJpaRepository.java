package com.homebase.ecom.support.infrastructure.persistence.adapter;

import com.homebase.ecom.support.infrastructure.persistence.entity.SupportTicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SupportTicketJpaRepository extends JpaRepository<SupportTicketEntity, String> {

    @Query("SELECT COUNT(t) FROM SupportTicketEntity t WHERE t.customerId = :customerId AND t.stateId NOT IN ('CLOSED')")
    long countOpenTicketsByCustomerId(@Param("customerId") String customerId);
}
