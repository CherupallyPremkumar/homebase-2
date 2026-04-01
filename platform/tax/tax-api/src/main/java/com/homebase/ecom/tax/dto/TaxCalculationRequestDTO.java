package com.homebase.ecom.tax.dto;

import java.util.List;

/**
 * Request to calculate tax for a set of items.
 * Used by Pricing module's TaxCalculationPort and by Checkout.
 */
public class TaxCalculationRequestDTO {

    private List<TaxableItemDTO> items;
    private String sourceState;      // seller's state (warehouse)
    private String destinationState; // buyer's state (shipping address)
    private String currency;

    public TaxCalculationRequestDTO() {}

    public List<TaxableItemDTO> getItems() { return items; }
    public void setItems(List<TaxableItemDTO> items) { this.items = items; }

    public String getSourceState() { return sourceState; }
    public void setSourceState(String sourceState) { this.sourceState = sourceState; }

    public String getDestinationState() { return destinationState; }
    public void setDestinationState(String destinationState) { this.destinationState = destinationState; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
