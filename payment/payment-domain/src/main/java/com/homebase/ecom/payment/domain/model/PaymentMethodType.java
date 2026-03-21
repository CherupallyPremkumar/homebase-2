package com.homebase.ecom.payment.domain.model;

/**
 * Specific payment method types — maps to paymentMethodType field on Payment.
 * Covers full Indian market: UPI variants, wallets, EMI, BNPL.
 */
public enum PaymentMethodType {
    // Cards
    CREDIT_CARD(PaymentMethodCategory.CARD),
    DEBIT_CARD(PaymentMethodCategory.CARD),
    PREPAID_CARD(PaymentMethodCategory.CARD),

    // UPI
    UPI_COLLECT(PaymentMethodCategory.UPI),
    UPI_INTENT(PaymentMethodCategory.UPI),
    UPI_QR(PaymentMethodCategory.UPI),

    // Net Banking
    NET_BANKING(PaymentMethodCategory.NET_BANKING),

    // Wallets
    WALLET_PAYTM(PaymentMethodCategory.WALLET),
    WALLET_PHONEPE(PaymentMethodCategory.WALLET),
    WALLET_AMAZON_PAY(PaymentMethodCategory.WALLET),

    // COD
    COD_CASH(PaymentMethodCategory.COD),
    COD_CARD_ON_DELIVERY(PaymentMethodCategory.COD),

    // EMI
    EMI_CARD(PaymentMethodCategory.EMI),
    EMI_DEBIT_CARD(PaymentMethodCategory.EMI),

    // BNPL
    BNPL_SIMPL(PaymentMethodCategory.BNPL),
    BNPL_LAZYPAY(PaymentMethodCategory.BNPL);

    private final PaymentMethodCategory category;

    PaymentMethodType(PaymentMethodCategory category) {
        this.category = category;
    }

    public PaymentMethodCategory getCategory() {
        return category;
    }
}
