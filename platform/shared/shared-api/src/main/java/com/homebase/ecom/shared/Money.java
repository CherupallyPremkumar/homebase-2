package com.homebase.ecom.shared;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * Immutable Money value object. Stores amount in the smallest currency unit
 * (paise for INR, cents for USD, yen for JPY).
 *
 * DB column: BIGINT. JSON: {"amount": 49900, "currency": "INR"} = Rs.499.00
 *
 * The currency's ISO 4217 fraction digits determine the divisor:
 *   INR/USD = 100, JPY = 1, BHD = 1000.
 */
@Embeddable
public class Money implements Serializable, Comparable<Money> {
    private static final long serialVersionUID = 1L;

    public static final Money ZERO_INR = new Money(0, "INR");

    @Column(name = "amount")
    private long amount;

    @Column(name = "currency", length = 3)
    private String currency;

    protected Money() {} // JPA

    public Money(long amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    // ── Factory methods ─────────────────────────────────────────────

    public static Money of(long amountInSmallestUnit, String currency) {
        return new Money(amountInSmallestUnit, currency);
    }

    public static Money ofMajor(long majorUnits, String currency) {
        int divisor = divisorFor(currency);
        return new Money(majorUnits * divisor, currency);
    }

    public static Money zero(String currency) {
        return new Money(0, currency);
    }

    // ── Getters ─────────────────────────────────────────────────────

    public long getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    // ── Arithmetic ──────────────────────────────────────────────────

    public Money add(Money other) {
        checkCurrency(other);
        return new Money(this.amount + other.amount, this.currency);
    }

    public Money subtract(Money other) {
        checkCurrency(other);
        return new Money(this.amount - other.amount, this.currency);
    }

    public Money multiply(int factor) {
        return new Money(this.amount * factor, this.currency);
    }

    public Money multiply(long factor) {
        return new Money(this.amount * factor, this.currency);
    }

    public Money negate() {
        return new Money(-this.amount, this.currency);
    }

    /**
     * Splits this amount into {@code parts} equal portions.
     * Remainder paise are distributed one each to the first N portions.
     * e.g., 100 paise / 3 = [34, 33, 33]
     */
    public Money[] split(int parts) {
        if (parts <= 0) throw new IllegalArgumentException("parts must be > 0");
        long base = amount / parts;
        long remainder = amount % parts;
        Money[] result = new Money[parts];
        for (int i = 0; i < parts; i++) {
            result[i] = new Money(base + (i < remainder ? 1 : 0), currency);
        }
        return result;
    }

    // ── Comparisons ─────────────────────────────────────────────────

    public boolean isPositive() {
        return amount > 0;
    }

    public boolean isNegative() {
        return amount < 0;
    }

    public boolean isZero() {
        return amount == 0;
    }

    public boolean isGreaterThan(Money other) {
        checkCurrency(other);
        return this.amount > other.amount;
    }

    public boolean isLessThan(Money other) {
        checkCurrency(other);
        return this.amount < other.amount;
    }

    public Money cap(Money maximum) {
        checkCurrency(maximum);
        return this.amount > maximum.amount ? maximum : this;
    }

    public Money floor(Money minimum) {
        checkCurrency(minimum);
        return this.amount < minimum.amount ? minimum : this;
    }

    @Override
    public int compareTo(Money other) {
        checkCurrency(other);
        return Long.compare(this.amount, other.amount);
    }

    // ── Display ─────────────────────────────────────────────────────

    /**
     * Returns the amount in major units as BigDecimal (for display/formatting only).
     * e.g., 49900 INR -> 499.00
     */
    public BigDecimal toMajorUnits() {
        int fractionDigits = fractionDigits();
        return BigDecimal.valueOf(amount, fractionDigits);
    }

    /**
     * Display string like "Rs.499.00" or "$12.50".
     */
    public String toDisplayString() {
        return toMajorUnits().toPlainString() + " " + currency;
    }

    // ── Internal ────────────────────────────────────────────────────

    private int fractionDigits() {
        try {
            return Currency.getInstance(currency).getDefaultFractionDigits();
        } catch (IllegalArgumentException e) {
            return 2; // safe default
        }
    }

    private static int divisorFor(String currency) {
        try {
            int digits = Currency.getInstance(currency).getDefaultFractionDigits();
            return (int) Math.pow(10, digits);
        } catch (IllegalArgumentException e) {
            return 100; // safe default
        }
    }

    private void checkCurrency(Money other) {
        if (!Objects.equals(this.currency, other.currency)) {
            throw new IllegalArgumentException("Currency mismatch: " + this.currency + " vs " + other.currency);
        }
    }

    // ── Object ──────────────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount == money.amount && Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
