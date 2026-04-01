package com.homebase.ecom.tax.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OWIZ pipeline context for tax calculation.
 * Flows through all 5 commands accumulating intermediate results.
 */
public class TaxContext {

    // ── Input ──
    private List<TaxableItem> items;
    private String sourceState;
    private String destinationState;
    private String currency;

    // ── Intermediate (set by pipeline commands) ──
    private TaxType taxType;                             // INTRA_STATE or INTER_STATE
    private Map<String, String> resolvedHsnCodes;        // variantId → hsnCode
    private Map<String, BigDecimal> taxRates;            // hsnCode → rate %
    private Map<String, BigDecimal> cessRates;           // hsnCode → cess rate % (if applicable)

    // ── Output ──
    private List<TaxLineResult> taxLineResults;
    private long totalTax;

    // ── Error handling ──
    private boolean error;
    private String errorMessage;

    public TaxContext() {
        this.resolvedHsnCodes = new HashMap<>();
        this.taxRates = new HashMap<>();
        this.cessRates = new HashMap<>();
        this.taxLineResults = new ArrayList<>();
    }

    public enum TaxType {
        INTRA_STATE,   // same state → CGST + SGST
        INTER_STATE    // different state → IGST
    }

    /**
     * Per-item tax result (intermediate).
     */
    public static class TaxLineResult {
        private String variantId;
        private String hsnCode;
        private long taxableAmount;
        private BigDecimal rate;
        private BigDecimal cessRate;
        private long cgstAmount;
        private long sgstAmount;
        private long igstAmount;
        private long cessAmount;
        private long totalTax;

        public String getVariantId() { return variantId; }
        public void setVariantId(String variantId) { this.variantId = variantId; }
        public String getHsnCode() { return hsnCode; }
        public void setHsnCode(String hsnCode) { this.hsnCode = hsnCode; }
        public long getTaxableAmount() { return taxableAmount; }
        public void setTaxableAmount(long taxableAmount) { this.taxableAmount = taxableAmount; }
        public BigDecimal getRate() { return rate; }
        public void setRate(BigDecimal rate) { this.rate = rate; }
        public BigDecimal getCessRate() { return cessRate; }
        public void setCessRate(BigDecimal cessRate) { this.cessRate = cessRate; }
        public long getCgstAmount() { return cgstAmount; }
        public void setCgstAmount(long cgstAmount) { this.cgstAmount = cgstAmount; }
        public long getSgstAmount() { return sgstAmount; }
        public void setSgstAmount(long sgstAmount) { this.sgstAmount = sgstAmount; }
        public long getIgstAmount() { return igstAmount; }
        public void setIgstAmount(long igstAmount) { this.igstAmount = igstAmount; }
        public long getCessAmount() { return cessAmount; }
        public void setCessAmount(long cessAmount) { this.cessAmount = cessAmount; }
        public long getTotalTax() { return totalTax; }
        public void setTotalTax(long totalTax) { this.totalTax = totalTax; }
    }

    /**
     * Input item for tax calculation (domain model, not DTO).
     */
    public static class TaxableItem {
        private String variantId;
        private String productCategory;
        private String hsnCode;
        private long taxableAmount;
        private int quantity;

        public String getVariantId() { return variantId; }
        public void setVariantId(String variantId) { this.variantId = variantId; }
        public String getProductCategory() { return productCategory; }
        public void setProductCategory(String productCategory) { this.productCategory = productCategory; }
        public String getHsnCode() { return hsnCode; }
        public void setHsnCode(String hsnCode) { this.hsnCode = hsnCode; }
        public long getTaxableAmount() { return taxableAmount; }
        public void setTaxableAmount(long taxableAmount) { this.taxableAmount = taxableAmount; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    // ── Getters/Setters ──

    public List<TaxableItem> getItems() { return items; }
    public void setItems(List<TaxableItem> items) { this.items = items; }

    public String getSourceState() { return sourceState; }
    public void setSourceState(String sourceState) { this.sourceState = sourceState; }

    public String getDestinationState() { return destinationState; }
    public void setDestinationState(String destinationState) { this.destinationState = destinationState; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public TaxType getTaxType() { return taxType; }
    public void setTaxType(TaxType taxType) { this.taxType = taxType; }

    public Map<String, String> getResolvedHsnCodes() { return resolvedHsnCodes; }
    public Map<String, BigDecimal> getTaxRates() { return taxRates; }
    public Map<String, BigDecimal> getCessRates() { return cessRates; }

    public List<TaxLineResult> getTaxLineResults() { return taxLineResults; }
    public void setTaxLineResults(List<TaxLineResult> taxLineResults) { this.taxLineResults = taxLineResults; }

    public long getTotalTax() { return totalTax; }
    public void setTotalTax(long totalTax) { this.totalTax = totalTax; }

    public boolean isError() { return error; }
    public void setError(boolean error) { this.error = error; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public void fail(String message) {
        this.error = true;
        this.errorMessage = message;
    }
}
