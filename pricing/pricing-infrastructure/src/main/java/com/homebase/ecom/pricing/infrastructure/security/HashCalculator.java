package com.homebase.ecom.pricing.infrastructure.security;

import com.homebase.ecom.pricing.domain.service.IHashCalculator;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

public class HashCalculator implements IHashCalculator {

    private static final String HMAC_SHA256 = "HmacSHA256";
    
    @Value("${security.pricing.hmac-secret-key:default-secret-key}")
    private String secretKey;

    @Override
    public String calculateBreakdownHash(String subtotal, String totalDiscount, String taxAmount, String shippingCost, String finalTotal) {
        try {
            String message = String.format("subtotal:%s|discount:%s|tax:%s|shipping:%s|final:%s",
                    subtotal, totalDiscount, taxAmount, shippingCost, finalTotal);
            
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate price hash", e);
        }
    }

    @Override
    public boolean verifyHash(String subtotal, String totalDiscount, String taxAmount, String shippingCost, String finalTotal, String expectedHash) {
        String calculated = calculateBreakdownHash(subtotal, totalDiscount, taxAmount, shippingCost, finalTotal);
        return constantTimeEquals(calculated, expectedHash);
    }

    private boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return Objects.equals(a, b);
        byte[] aBytes = a.getBytes(StandardCharsets.UTF_8);
        byte[] bBytes = b.getBytes(StandardCharsets.UTF_8);
        if (aBytes.length != bBytes.length) return false;
        int result = 0;
        for (int i = 0; i < aBytes.length; i++) {
            result |= aBytes[i] ^ bBytes[i];
        }
        return result == 0;
    }
}
