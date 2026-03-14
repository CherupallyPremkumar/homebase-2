package com.homebase.ecom.reconciliation.service;

import com.homebase.ecom.reconciliation.dto.ReconciliationRequest;
import com.homebase.ecom.reconciliation.dto.ReconciliationResult;

public interface ReconciliationService {
    ReconciliationResult reconcile(ReconciliationRequest request);
}
