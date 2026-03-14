package com.homebase.ecom.user.domain.model;

import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * User — Domain Aggregate Root (DDD).
 *
 * Follows the same pattern as Policy and Cart:
 *   extends AbstractExtendedStateEntity  ← non-JPA STM (chenile-corefork/utils)
 *   implements ActivityEnabledStateEntity ← audit trail of lifecycle events
 *   implements ContainsTransientMap       ← per-request scratchpad: actions → post-save hooks
 *
 * Composes:
 *   - Address (List, max 5)    ← extends BaseEntity (own id, createdTime)
 *   - Preferences (embedded)  ← pure value object, no separate identity
 *   - UserActivityLog          ← extends BaseEntity (own id, createdTime)
 *
 * NO JPA annotations here — those live in UserJpaEntity in user-infrastructure.
 *
 * Lifecycle states (user-states.xml):
 *   PENDING_VERIFICATION → ACTIVE ↔ LOCKED / SUSPENDED → DELETED
 */
public class User extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    // ─── Identity ─────────────────────────────────────────────────────────────
    /** Keycloak `sub` claim — canonical, immutable identity bridge. */
    private String keycloakId;
    private String email;

    // ─── Profile ──────────────────────────────────────────────────────────────
    private String firstName;
    private String lastName;
    private String phone;
    private String avatarUrl;

    // ─── Address book (max 5) ─────────────────────────────────────────────────
    private List<Address> addresses = new ArrayList<>();

    private static final int MAX_ADDRESSES = 5;

    // ─── Preferences (embedded value object) ─────────────────────────────────
    private Preferences preferences;

    // ─── Account security ─────────────────────────────────────────────────────
    /** Consecutive failed logins. Resets on success. STM locks at 3. */
    private int failedLoginAttempts;
    private String lockReason;
    private String suspendReason;

    // ─── Chenile STM: Activity log ────────────────────────────────────────────
    private List<UserActivityLog> activities = new ArrayList<>();

    // ─── Chenile STM: Transient map (per-request scratchpad) ─────────────────
    private TransientMap transientMap = new TransientMap();

    // ─── Address book business logic ──────────────────────────────────────────

    public void addAddress(Address address) {
        if (addresses.size() >= MAX_ADDRESSES) {
            throw new IllegalStateException("Max " + MAX_ADDRESSES + " addresses allowed");
        }
        if (addresses.isEmpty()) {
            address.markAsDefault(); // first address auto-becomes default
        }
        addresses.add(address);
    }

    public void removeAddress(String addressId) {
        Address toRemove = findAddressById(addressId);
        boolean wasDefault = toRemove.isDefault();
        addresses.remove(toRemove);
        if (wasDefault && !addresses.isEmpty()) {
            addresses.get(0).markAsDefault(); // promote first remaining
        }
    }

    public void setDefaultAddress(String addressId) {
        addresses.forEach(Address::clearDefault);
        findAddressById(addressId).markAsDefault();
    }

    private Address findAddressById(String addressId) {
        return addresses.stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Address not found: " + addressId));
    }

    // ─── Security business logic ──────────────────────────────────────────────

    /** @return true if threshold reached — action should trigger lockAccount */
    public boolean recordFailedLogin() {
        this.failedLoginAttempts++;
        return this.failedLoginAttempts >= 3;
    }

    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
    }

    // ─── ActivityEnabledStateEntity ───────────────────────────────────────────

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

    // ─── ContainsTransientMap ─────────────────────────────────────────────────

    @Override
    public TransientMap getTransientMap() { return transientMap; }
    public void setTransientMap(TransientMap tm) { this.transientMap = tm; }

    // ─── Getters / Setters ────────────────────────────────────────────────────

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

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public List<Address> getAddresses() { return Collections.unmodifiableList(addresses); }
    public void setAddresses(List<Address> addresses) { this.addresses = addresses; }

    public Preferences getPreferences() { return preferences; }
    public void setPreferences(Preferences preferences) { this.preferences = preferences; }

    public int getFailedLoginAttempts() { return failedLoginAttempts; }
    public void setFailedLoginAttempts(int f) { this.failedLoginAttempts = f; }

    public String getLockReason() { return lockReason; }
    public void setLockReason(String lockReason) { this.lockReason = lockReason; }

    public String getSuspendReason() { return suspendReason; }
    public void setSuspendReason(String suspendReason) { this.suspendReason = suspendReason; }

    public List<UserActivityLog> getActivities() { return activities; }
    public void setActivities(List<UserActivityLog> activities) { this.activities = activities; }
}
