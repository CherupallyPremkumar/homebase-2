package com.homebase.ecom.user.dto;

/**
 * DTO for user preferences. Lives in user-api.
 */
public class PreferencesDto {

    private String language;              // ISO 639-1, e.g. "en", "fr"
    private String currency;             // ISO 4217, e.g. "INR", "USD"
    private boolean emailNotifications;
    private boolean smsNotifications;
    private String timezone;             // e.g. "Asia/Kolkata"

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
