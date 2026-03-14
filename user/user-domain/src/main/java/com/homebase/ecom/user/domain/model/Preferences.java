package com.homebase.ecom.user.domain.model;

import com.homebase.ecom.user.domain.port.CurrencyResolver;

/**
 * Preferences — value object embedded in User aggregate.
 *
 * Default currency is resolved per-request from Chenile's ContextContainer via
 * the CurrencyResolver port (implemented in infra by CurrencyResolverAdapter
 * which calls CurrencyInterceptor.getCurrencyFromContext()).
 *
 * Priority: x-chenile-region-id header → Accept-Language → INR
 *
 * Two currency levels:
 *   1. Saved here (DB)  — user's explicit saved preference
 *   2. ContextContainer — per-request fallback
 */
public class Preferences {

    private String language;            // ISO 639-1: "en", "fr", "hi"
    private String currency;            // ISO 4217: "INR", "USD", "EUR"
    private boolean emailNotifications;
    private boolean smsNotifications;
    private String timezone;            // e.g. "Asia/Kolkata"

    public Preferences() {}

    /**
     * Default preferences for newly provisioned user.
     * Currency is resolved from the current request context (not hardcoded).
     */
    public static Preferences defaults(CurrencyResolver currencyResolver) {
        Preferences p = new Preferences();
        p.language = "en";
        p.currency = currencyResolver.resolveCurrentCurrency(); // region-aware default
        p.emailNotifications = true;
        p.smsNotifications = true;
        p.timezone = "Asia/Kolkata";
        return p;
    }

    /** Reconstitution from persistence — no resolver needed. */
    public static Preferences of(String language, String currency,
                                  boolean emailNotifications, boolean smsNotifications,
                                  String timezone) {
        Preferences p = new Preferences();
        p.language = language;
        p.currency = currency;
        p.emailNotifications = emailNotifications;
        p.smsNotifications = smsNotifications;
        p.timezone = timezone;
        return p;
    }

    public String getLanguage()              { return language; }
    public String getCurrency()              { return currency; }
    public boolean isEmailNotifications()    { return emailNotifications; }
    public boolean isSmsNotifications()      { return smsNotifications; }
    public String getTimezone()              { return timezone; }

    public void setLanguage(String language)                       { this.language = language; }
    public void setCurrency(String currency)                       { this.currency = currency; }
    public void setEmailNotifications(boolean emailNotifications)  { this.emailNotifications = emailNotifications; }
    public void setSmsNotifications(boolean smsNotifications)      { this.smsNotifications = smsNotifications; }
    public void setTimezone(String timezone)                       { this.timezone = timezone; }
}
