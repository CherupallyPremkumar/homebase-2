package com.homebase.ecom.reconciliation.configuration.controller;

import com.homebase.ecom.reconciliation.dto.ReconciliationRequest;
import com.homebase.ecom.reconciliation.dto.ReconciliationResult;
import com.homebase.ecom.reconciliation.service.cmds.ReconciliationContext;
import org.chenile.owiz.OrchExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Reconciliation", description = "Payment reconciliation")
public class ReconciliationController {

    @Autowired
    private OrchExecutor<ReconciliationContext> reconciliationOrchExecutor;

    @PostMapping("/reconciliation/run")
    public ResponseEntity<ReconciliationResult> runReconciliation(
            @RequestBody ReconciliationRequest request) throws Exception {
        ReconciliationContext context = new ReconciliationContext(request);
        reconciliationOrchExecutor.execute(context);
        return ResponseEntity.ok(context.getResult());
    }
}
