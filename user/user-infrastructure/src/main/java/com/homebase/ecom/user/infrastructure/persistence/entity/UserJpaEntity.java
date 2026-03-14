package com.homebase.ecom.user.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * JPA Entity for User.
 * Maps to user_profiles table.
 * Extends AbstractJpaStateEntity for Chenile STM integration.
 */
@Entity
@Table(name = "user_profiles")
public class UserJpaEntity extends AbstractJpaStateEntity implements ActivityEnabledStateEntity, ContainsTransientMap {

    @Column(name = "keycloak_id", unique = true, updatable = false)
    private String keycloakId;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "display_name")
    private String displayName;

    @Embedded
    private PreferencesJpaEntity preferences;

    @Column(name = "failed_login_attempts")
    private int failedLoginAttempts;

    @Column(name = "lock_reason")
    private String lockReason;

    @Column(name = "suspend_reason")
    private String suspendReason;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<AddressJpaEntity> addresses = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<UserActivityLogEntity> activities = new ArrayList<>();

    @Transient
    private TransientMap transientMap = new TransientMap();

    // ─── ActivityEnabledStateEntity ───────────────────────────────────────────

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        UserActivityLogEntity log = new UserActivityLogEntity();
        log.setName(eventId);
        log.setComment(comment);
        log.setSuccess(true);
        activities.add(log);
        return log;
    }

    // ─── ContainsTransientMap ─────────────────────────────────────────────────

    @Override
    public TransientMap getTransientMap() { return transientMap; }
    public void setTransientMap(TransientMap transientMap) { this.transientMap = transientMap; }

    // ─── Getters and Setters ─────────────────────────────────────────────────

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

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public PreferencesJpaEntity getPreferences() { return preferences; }
    public void setPreferences(PreferencesJpaEntity preferences) { this.preferences = preferences; }

    public int getFailedLoginAttempts() { return failedLoginAttempts; }
    public void setFailedLoginAttempts(int failedLoginAttempts) { this.failedLoginAttempts = failedLoginAttempts; }

    public String getLockReason() { return lockReason; }
    public void setLockReason(String lockReason) { this.lockReason = lockReason; }

    public String getSuspendReason() { return suspendReason; }
    public void setSuspendReason(String suspendReason) { this.suspendReason = suspendReason; }

    public List<AddressJpaEntity> getAddresses() { return addresses; }
    public void setAddresses(List<AddressJpaEntity> addresses) { this.addresses = addresses; }

    public List<UserActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<UserActivityLogEntity> activities) { this.activities = activities; }
}
