package com.homebase.ecom.user.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * JPA Embeddable for Preferences.
 * Embedded directly into the user_profiles table (no separate table).
 */
@Embeddable
public class PreferencesJpaEntity {

    @Column(name = "pref_language")
    private String language;

    @Column(name = "pref_currency")
    private String currency;

    @Column(name = "pref_email_notif")
    private boolean emailNotifications;

    @Column(name = "pref_sms_notif")
    private boolean smsNotifications;

    @Column(name = "pref_timezone")
    private String timezone;

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public boolean isEmailNotifications() { return emailNotifications; }
    public void setEmailNotifications(boolean emailNotifications) { this.emailNotifications = emailNotifications; }

    public boolean isSmsNotifications() { return smsNotifications; }
    public void setSmsNotifications(boolean smsNotifications) { this.smsNotifications = smsNotifications; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
}
