package com.homebase.ecom.pricing.domain.service;

public interface IHashCalculator {
    String calculateBreakdownHash(String subtotal, String totalDiscount, String taxAmount, String shippingCost, String finalTotal);
    boolean verifyHash(String subtotal, String totalDiscount, String taxAmount, String shippingCost, String finalTotal, String expectedHash);
}
