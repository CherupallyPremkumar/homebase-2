package com.homebase.ecom.cart.domain.model;

import com.homebase.ecom.shared.Money;
import java.util.List;

/**
 * Domain-level pricing result — what the Cart bounded context understands.
 * The infrastructure adapter translates external PricingResponseDTO into this.
 *
 * Cart domain never sees pricing-api DTOs. This is the anti-corruption boundary.
 */
public class PricingResult {

    private Money subtotal;
    private Money totalDiscount;
    private Money total;
    private List<LineItemPricing> lineItems;

    public PricingResult() {}

    public Money getSubtotal() { return subtotal; }
    public void setSubtotal(Money subtotal) { this.subtotal = subtotal; }
    public Money getTotalDiscount() { return totalDiscount; }
    public void setTotalDiscount(Money totalDiscount) { this.totalDiscount = totalDiscount; }
    public Money getTotal() { return total; }
    public void setTotal(Money total) { this.total = total; }
    public List<LineItemPricing> getLineItems() { return lineItems; }
    public void setLineItems(List<LineItemPricing> lineItems) { this.lineItems = lineItems; }

    /**
     * Per-line-item pricing from Pricing Service.
     */
    public static class LineItemPricing {
        private String variantId;
        private Money unitPrice;
        private Money lineTotal;

        public LineItemPricing() {}

        public String getVariantId() { return variantId; }
        public void setVariantId(String variantId) { this.variantId = variantId; }
        public Money getUnitPrice() { return unitPrice; }
        public void setUnitPrice(Money unitPrice) { this.unitPrice = unitPrice; }
        public Money getLineTotal() { return lineTotal; }
        public void setLineTotal(Money lineTotal) { this.lineTotal = lineTotal; }
    }
}
