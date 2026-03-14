package com.homebase.ecom.checkout.api.dto;

/**
 * Price Breakdown DTO
 */
public class PriceBreakdownDTO {

    private MoneyDTO subtotal;
    private MoneyDTO shipping;
    private MoneyDTO tax;
    private MoneyDTO discount;
    private MoneyDTO total;

    // Getters and Setters
    public MoneyDTO getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(MoneyDTO subtotal) {
        this.subtotal = subtotal;
    }

    public MoneyDTO getShipping() {
        return shipping;
    }

    public void setShipping(MoneyDTO shipping) {
        this.shipping = shipping;
    }

    public MoneyDTO getTax() {
        return tax;
    }

    public void setTax(MoneyDTO tax) {
        this.tax = tax;
    }

    public MoneyDTO getDiscount() {
        return discount;
    }

    public void setDiscount(MoneyDTO discount) {
        this.discount = discount;
    }

    public MoneyDTO getTotal() {
        return total;
    }

    public void setTotal(MoneyDTO total) {
        this.total = total;
    }
}
