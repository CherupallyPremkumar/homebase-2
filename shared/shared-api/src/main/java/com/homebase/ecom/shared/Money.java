package com.homebase.ecom.shared;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Money implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", length = 3)
    private String currency;

    protected Money() {
    } // JPA

    public Money(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Money add(Money other) {
        checkCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        checkCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    public Money multiply(BigDecimal factor) {
        return new Money(this.amount.multiply(factor), this.currency);
    }

    public Money divide(BigDecimal divisor) {
        return new Money(this.amount.divide(divisor, 2, BigDecimal.ROUND_HALF_UP), this.currency);
    }

    public boolean isGreaterThan(Money other) {
        checkCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(Money other) {
        checkCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }

    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public Money cap(Money maximum) {
        checkCurrency(maximum);
        if (this.isGreaterThan(maximum)) {
            return maximum;
        }
        return this;
    }

    private void checkCurrency(Money other) {
        if (!Objects.equals(this.currency, other.currency)) {
            throw new IllegalArgumentException("Currency mismatch: " + this.currency + " vs " + other.currency);
        }
    }

    public int compareTo(Money other) {
        checkCurrency(other);
        return this.amount.compareTo(other.amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) && Objects.equals(currency, money.currency);
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
