package com.homebase.ecom.user.domain.model;

import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * User -- Domain Aggregate Root (DDD).
 *
 * Entity fields:
 *   id, email, firstName, lastName, phone, role (CUSTOMER/SELLER/ADMIN/SUPPORT_AGENT),
 *   addresses (List), defaultAddressId, kycStatus, loginAttempts, lastLoginAt,
 *   stateId, flowId.
 *
 * Lifecycle states (user-states.xml):
 *   REGISTERED -> EMAIL_VERIFIED -> ACTIVE -> SUSPENDED -> DEACTIVATED
 *   KYC: KYC_PENDING -> KYC_VERIFIED
 *
 * NO JPA annotations -- those live in UserJpaEntity in user-infrastructure.
 */
public class User extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    // --- Identity ---
    /** Keycloak `sub` claim -- canonical, immutable identity bridge. */
    private String keycloakId;
    private String email;

    // --- Profile ---
    private String firstName;
    private String lastName;
    private String phone;

    // --- Role ---
    private String role; // CUSTOMER, SELLER, ADMIN, SUPPORT_AGENT

    // --- Address book (max 10) ---
    private List<Address> addresses = new ArrayList<>();
    private String defaultAddressId;

    private static final int MAX_ADDRESSES = 10;

    // --- KYC ---
    private String kycStatus; // KYC_PENDING, KYC_VERIFIED

    // --- Preferences ---
    private Preferences preferences;

    // --- Extended profile (DB: user-004) ---
    private String profileImageUrl;
    private java.time.LocalDate dateOfBirth;
    private String gender;
    private boolean emailVerified;
    private boolean phoneVerified;
    private String referralCode;
    private String referredBy;
    private String customerTier;

    // --- Account security ---
    /** Consecutive failed logins. Resets on success. STM locks at configurable threshold. */
    private int loginAttempts;
    private Instant lastLoginAt;
    private String lockReason;
    private String suspendReason;
    private String tenant;

    // --- Chenile STM: Activity log ---
    private List<UserActivityLog> activities = new ArrayList<>();

    // --- Chenile STM: Transient map (per-request scratchpad) ---
    private TransientMap transientMap = new TransientMap();

    // --- Address book business logic ---

    public void addAddress(Address address) {
        if (addresses.size() >= MAX_ADDRESSES) {
            throw new IllegalStateException("Max " + MAX_ADDRESSES + " addresses allowed");
        }
        if (addresses.isEmpty()) {
            address.markAsDefault();
            this.defaultAddressId = address.getId();
        }
        addresses.add(address);
    }

    public void removeAddress(String addressId) {
        Address toRemove = findAddressById(addressId);
        boolean wasDefault = toRemove.isDefault();
        addresses.remove(toRemove);
        if (wasDefault && !addresses.isEmpty()) {
            addresses.get(0).markAsDefault();
            this.defaultAddressId = addresses.get(0).getId();
        } else if (addresses.isEmpty()) {
            this.defaultAddressId = null;
        }
    }

    public void setDefaultAddress(String addressId) {
        addresses.forEach(Address::clearDefault);
        Address addr = findAddressById(addressId);
        addr.markAsDefault();
        this.defaultAddressId = addressId;
    }

    private Address findAddressById(String addressId) {
        return addresses.stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Address not found: " + addressId));
    }

    // --- Security business logic ---

    /** @return true if threshold reached -- action should trigger lockAccount */
    public boolean recordFailedLogin() {
        this.loginAttempts++;
        return this.loginAttempts >= 5; // default; overridden by cconfig
    }

    public boolean recordFailedLogin(int maxAttempts) {
        this.loginAttempts++;
        return this.loginAttempts >= maxAttempts;
    }

    public void resetLoginAttempts() {
        this.loginAttempts = 0;
    }

    public void recordSuccessfulLogin() {
        this.loginAttempts = 0;
        this.lastLoginAt = Instant.now();
    }

    // --- ActivityEnabledStateEntity ---

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        UserActivityLog log = new UserActivityLog();
        log.activityName = eventId;
        log.activityComment = comment;
        log.activitySuccess = true;
        activities.add(log);
        return log;
    }

    // --- ContainsTransientMap ---

    @Override
    public TransientMap getTransientMap() { return transientMap; }
    public void setTransientMap(TransientMap tm) { this.transientMap = tm; }

    // --- Getters / Setters ---

    public String getKeycloakId() { return keycloakId; }
    public void setKeycloakId(String keycloakId) { this.keycloakId = keycloakId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<Address> getAddresses() { return Collections.unmodifiableList(addresses); }
    public void setAddresses(List<Address> addresses) { this.addresses = addresses; }

    public String getDefaultAddressId() { return defaultAddressId; }
    public void setDefaultAddressId(String defaultAddressId) { this.defaultAddressId = defaultAddressId; }

    public String getKycStatus() { return kycStatus; }
    public void setKycStatus(String kycStatus) { this.kycStatus = kycStatus; }

    public int getLoginAttempts() { return loginAttempts; }
    public void setLoginAttempts(int loginAttempts) { this.loginAttempts = loginAttempts; }

    public Instant getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(Instant lastLoginAt) { this.lastLoginAt = lastLoginAt; }

    public String getLockReason() { return lockReason; }
    public void setLockReason(String lockReason) { this.lockReason = lockReason; }

    public String getSuspendReason() { return suspendReason; }
    public void setSuspendReason(String suspendReason) { this.suspendReason = suspendReason; }

    public List<UserActivityLog> getActivities() { return activities; }
    public void setActivities(List<UserActivityLog> activities) { this.activities = activities; }

    public Preferences getPreferences() { return preferences; }
    public void setPreferences(Preferences preferences) { this.preferences = preferences; }

    // Backward compatibility for existing code referencing failedLoginAttempts
    public int getFailedLoginAttempts() { return loginAttempts; }
    public void setFailedLoginAttempts(int f) { this.loginAttempts = f; }
    public void resetFailedLoginAttempts() { this.loginAttempts = 0; }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }

    // --- Extended profile getters/setters ---
    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public java.time.LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(java.time.LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public boolean isEmailVerified() { return emailVerified; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }

    public boolean isPhoneVerified() { return phoneVerified; }
    public void setPhoneVerified(boolean phoneVerified) { this.phoneVerified = phoneVerified; }

    public String getReferralCode() { return referralCode; }
    public void setReferralCode(String referralCode) { this.referralCode = referralCode; }

    public String getReferredBy() { return referredBy; }
    public void setReferredBy(String referredBy) { this.referredBy = referredBy; }

    public String getCustomerTier() { return customerTier; }
    public void setCustomerTier(String customerTier) { this.customerTier = customerTier; }
}
