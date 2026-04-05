package com.homebase.ecom.reconciliation.service.cmds;

import com.homebase.ecom.reconciliation.dto.MismatchDto;
import com.homebase.ecom.reconciliation.dto.ReconciliationRequest;
import com.homebase.ecom.reconciliation.dto.ReconciliationResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Context object passed through the OWIZ chain. Each command in the reconciliation
 * flow reads from and writes to this context.
 */
public class ReconciliationContext {

    private ReconciliationRequest request;
    private ReconciliationResult result;

    /** Gateway transaction ID -> amount */
    private Map<String, BigDecimal> gatewayTransactions = new HashMap<>();

    /** System transaction ID -> amount (from orders/payments) */
    private Map<String, BigDecimal> systemTransactions = new HashMap<>();

    /** System transaction ID -> order ID mapping */
    private Map<String, String> transactionToOrderMap = new HashMap<>();

    /** Detected mismatches before auto-resolution */
    private List<MismatchDto> detectedMismatches = new ArrayList<>();

    public ReconciliationContext(ReconciliationRequest request) {
        this.request = request;
        this.result = new ReconciliationResult();
    }

    public ReconciliationRequest getRequest() {
        return request;
    }

    public ReconciliationResult getResult() {
        return result;
    }

    public void setResult(ReconciliationResult result) {
        this.result = result;
    }

    public Map<String, BigDecimal> getGatewayTransactions() {
        return gatewayTransactions;
    }

    public void setGatewayTransactions(Map<String, BigDecimal> gatewayTransactions) {
        this.gatewayTransactions = gatewayTransactions;
    }

    public Map<String, BigDecimal> getSystemTransactions() {
        return systemTransactions;
    }

    public void setSystemTransactions(Map<String, BigDecimal> systemTransactions) {
        this.systemTransactions = systemTransactions;
    }

    public Map<String, String> getTransactionToOrderMap() {
        return transactionToOrderMap;
    }

    public void setTransactionToOrderMap(Map<String, String> transactionToOrderMap) {
        this.transactionToOrderMap = transactionToOrderMap;
    }

    public List<MismatchDto> getDetectedMismatches() {
        return detectedMismatches;
    }

    public void setDetectedMismatches(List<MismatchDto> detectedMismatches) {
        this.detectedMismatches = detectedMismatches;
    }
}
