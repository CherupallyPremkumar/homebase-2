package com.homebase.ecom.tax.dto;

import java.util.List;

/**
 * Tax calculation result with per-item breakdown.
 */
public class TaxCalculationResponseDTO {

    private List<TaxLineItemDTO> items;
    private long totalTax;          // minor units
    private String taxType;         // INTRA_STATE or INTER_STATE
    private String currency;
    private boolean error;
    private String message;

    public TaxCalculationResponseDTO() {}

    public static TaxCalculationResponseDTO error(String message) {
        TaxCalculationResponseDTO dto = new TaxCalculationResponseDTO();
        dto.setError(true);
        dto.setMessage(message);
        return dto;
    }

    public List<TaxLineItemDTO> getItems() { return items; }
    public void setItems(List<TaxLineItemDTO> items) { this.items = items; }

    public long getTotalTax() { return totalTax; }
    public void setTotalTax(long totalTax) { this.totalTax = totalTax; }

    public String getTaxType() { return taxType; }
    public void setTaxType(String taxType) { this.taxType = taxType; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public boolean isError() { return error; }
    public void setError(boolean error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
