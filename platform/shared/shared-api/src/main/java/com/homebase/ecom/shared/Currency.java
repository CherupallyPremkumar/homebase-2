package com.homebase.ecom.shared;

import java.io.Serializable;

/**
 * Metadata for a currency supported by the platform.
 */
public record Currency(
        String code, // ISO 4217 (e.g., "USD", "INR")
        String symbol, // (e.g., "$", "₹")
        int precision // Number of decimal places (e.g., 2 for USD, 0 for JPY)
) implements Serializable {
    private static final long serialVersionUID = 1L;
}
