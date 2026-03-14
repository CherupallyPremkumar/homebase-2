package com.homebase.ecom.support.domain.port;

import com.homebase.ecom.support.model.SupportTicket;

import java.util.Optional;

public interface SupportTicketRepository {
    Optional<SupportTicket> findById(String id);
    void save(SupportTicket ticket);
    void delete(String id);
}
