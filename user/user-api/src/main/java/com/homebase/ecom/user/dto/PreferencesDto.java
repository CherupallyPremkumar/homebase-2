package com.homebase.ecom.user.dto;

/**
 * DTO for user preferences.
 * Lives in user-api -- safe to share across all modules.
 */
public class PreferencesDto {

    private String language;
    private String currency;
    private boolean emailNotifications;
    private boolean smsNotifications;
    private String timezone;

    public PreferencesDto() {}

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
