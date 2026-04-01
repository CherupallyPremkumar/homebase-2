package com.homebase.ecom.supplier.service.impl;

import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.service.SupplierService;
import org.chenile.stm.STM;
import org.chenile.stm.impl.STMActionsInfoProvider;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SupplierServiceImpl extends StateEntityServiceImpl<Supplier> implements SupplierService {

    public SupplierServiceImpl(
            @Qualifier("supplierEntityStm") STM<Supplier> stm,
            @Qualifier("supplierActionsInfoProvider") STMActionsInfoProvider stmActionsInfoProvider,
            @Qualifier("supplierEntityStore") EntityStore<Supplier> entityStore) {
        super(stm, stmActionsInfoProvider, entityStore);
    }

    @Override
    public StateEntityServiceResponse<Supplier> create(Supplier entity) {
        return super.create(entity);
    }

    @Override
    public StateEntityServiceResponse<Supplier> process(Supplier entity, String event, Object payload) {
        return super.process(entity, event, payload);
    }

    @Override
    public StateEntityServiceResponse<Supplier> processById(String id, String event, Object payload) {
        return super.processById(id, event, payload);
    }
}
