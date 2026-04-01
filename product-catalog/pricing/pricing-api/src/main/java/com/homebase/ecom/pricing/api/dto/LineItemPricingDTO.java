package com.homebase.ecom.pricing.api.dto;

import com.homebase.ecom.shared.Money;
import java.io.Serializable;
import java.util.Objects;

/**
 * Per-line-item pricing breakdown returned by the Pricing Service.
 * Cart stores these values directly — no local calculation.
 */
public class LineItemPricingDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String variantId;
    private String productId;
    private Money unitPrice;
    private int quantity;
    private Money lineSubtotal;
    private Money lineDiscount;
    private Money lineTotal;
    private String discountReason;

    public LineItemPricingDTO() {}

    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public Money getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Money unitPrice) { this.unitPrice = unitPrice; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public Money getLineSubtotal() { return lineSubtotal; }
    public void setLineSubtotal(Money lineSubtotal) { this.lineSubtotal = lineSubtotal; }
    public Money getLineDiscount() { return lineDiscount; }
    public void setLineDiscount(Money lineDiscount) { this.lineDiscount = lineDiscount; }
    public Money getLineTotal() { return lineTotal; }
    public void setLineTotal(Money lineTotal) { this.lineTotal = lineTotal; }
    public String getDiscountReason() { return discountReason; }
    public void setDiscountReason(String discountReason) { this.discountReason = discountReason; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineItemPricingDTO that = (LineItemPricingDTO) o;
        return quantity == that.quantity &&
                Objects.equals(variantId, that.variantId) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(unitPrice, that.unitPrice) &&
                Objects.equals(lineSubtotal, that.lineSubtotal) &&
                Objects.equals(lineDiscount, that.lineDiscount) &&
                Objects.equals(lineTotal, that.lineTotal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variantId, productId, unitPrice, quantity, lineSubtotal, lineDiscount, lineTotal);
    }

    @Override
    public String toString() {
        return "LineItemPricingDTO{" +
                "variantId='" + variantId + '\'' +
                ", unitPrice=" + unitPrice +
                ", qty=" + quantity +
                ", lineTotal=" + lineTotal +
                ", lineDiscount=" + lineDiscount +
                '}';
    }
}
