package com.homebase.ecom.returnrequest.infrastructure.persistence;

import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.infrastructure.persistence.adapter.ReturnrequestJpaRepository;
import com.homebase.ecom.returnrequest.infrastructure.persistence.entity.ReturnrequestEntity;
import com.homebase.ecom.returnrequest.infrastructure.persistence.mapper.ReturnrequestMapper;
import org.chenile.jpautils.store.ChenileJpaEntityStore;

public class ChenileReturnrequestEntityStore extends ChenileJpaEntityStore<Returnrequest, ReturnrequestEntity> {

    public ChenileReturnrequestEntityStore(ReturnrequestJpaRepository repository, ReturnrequestMapper mapper) {
        super(repository, (entity) -> mapper.toModel(entity), (model) -> mapper.toEntity(model));
    }
}
