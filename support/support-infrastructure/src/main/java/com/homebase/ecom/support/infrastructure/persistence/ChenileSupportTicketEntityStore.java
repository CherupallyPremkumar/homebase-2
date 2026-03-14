package com.homebase.ecom.support.infrastructure.persistence;

import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.support.infrastructure.persistence.adapter.SupportTicketJpaRepository;
import com.homebase.ecom.support.infrastructure.persistence.entity.SupportTicketEntity;
import com.homebase.ecom.support.infrastructure.persistence.mapper.SupportTicketMapper;
import org.chenile.jpautils.store.ChenileJpaEntityStore;

public class ChenileSupportTicketEntityStore extends ChenileJpaEntityStore<SupportTicket, SupportTicketEntity> {

    public ChenileSupportTicketEntityStore(SupportTicketJpaRepository repository, SupportTicketMapper mapper) {
        super(repository, (entity) -> mapper.toModel(entity), (model) -> mapper.toEntity(model));
    }
}
