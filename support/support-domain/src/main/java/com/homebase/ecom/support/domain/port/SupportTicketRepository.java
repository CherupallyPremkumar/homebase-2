package com.homebase.ecom.support.domain.port;

import com.homebase.ecom.support.model.SupportTicket;

import java.util.List;
import java.util.Optional;

/**
 * Hexagonal port for support ticket persistence.
 */
public interface SupportTicketRepository {
    Optional<SupportTicket> findById(String id);
    void save(SupportTicket ticket);
    void delete(String id);
    long countOpenTicketsByCustomerId(String customerId);
}
