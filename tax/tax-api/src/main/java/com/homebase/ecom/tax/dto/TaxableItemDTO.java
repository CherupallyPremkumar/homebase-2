package com.homebase.ecom.tax.dto;

/**
 * A single item that needs tax calculated.
 * taxableAmount is in minor units (paise for INR).
 */
public class TaxableItemDTO {

    private String variantId;
    private String productCategory;
    private String hsnCode;        // optional — resolved from category if missing
    private long taxableAmount;    // minor units
    private int quantity;

    public TaxableItemDTO() {}

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
