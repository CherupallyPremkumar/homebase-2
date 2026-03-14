package com.homebase.ecom.supplierlifecycle.service;

import com.homebase.ecom.supplierlifecycle.dto.SupplierLifecycleRequest;
import com.homebase.ecom.supplierlifecycle.dto.SupplierLifecycleResponse;

/**
 * Service interface for processing supplier lifecycle orchestration requests.
 * Cascades supplier state changes (suspend, blacklist, reactivate) across
 * bounded contexts using OWIZ chain orchestration.
 */
public interface SupplierLifecycleService {

    /**
     * Process a supplier lifecycle action by executing the appropriate OWIZ flow.
     *
     * @param request the lifecycle request containing supplierId, action, and reason
     * @return response with counts of affected entities across bounded contexts
     */
    SupplierLifecycleResponse process(SupplierLifecycleRequest request);
}
