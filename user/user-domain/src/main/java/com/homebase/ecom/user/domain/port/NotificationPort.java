package com.homebase.ecom.user.domain.port;

/**
 * Outbound Port (Hexagonal): Notification dispatch.
 * Abstraction for sending notifications to users (email, SMS, push).
 * Infrastructure provides the implementation (e.g., via notification service).
 */
public interface NotificationPort {

    /** Notify user that their email has been verified and account is active. */
    void notifyUserVerified(String userId, String email);

    /** Notify user that their account has been suspended. */
    void notifyUserSuspended(String userId, String email, String reason);

    /** Notify user that their account has been deactivated. */
    void notifyUserDeactivated(String userId, String email);

    /** Notify user about KYC verification result. */
    void notifyKycVerified(String userId, String email);
}
