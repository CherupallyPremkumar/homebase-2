package com.homebase.ecom.support.infrastructure.persistence.adapter;

import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.support.domain.port.SupportTicketRepository;
import com.homebase.ecom.support.infrastructure.persistence.entity.SupportTicketEntity;
import com.homebase.ecom.support.infrastructure.persistence.mapper.SupportTicketMapper;

import java.util.Optional;

public class SupportTicketRepositoryImpl implements SupportTicketRepository {

    private final SupportTicketJpaRepository jpaRepository;
    private final SupportTicketMapper mapper;

    public SupportTicketRepositoryImpl(SupportTicketJpaRepository jpaRepository, SupportTicketMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<SupportTicket> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public void save(SupportTicket ticket) {
        SupportTicketEntity entity = mapper.toEntity(ticket);
        jpaRepository.save(entity);
        ticket.setId(entity.getId());
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
