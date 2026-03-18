package com.homebase.ecom.user.infrastructure.persistence.mapper;

import com.homebase.ecom.user.domain.model.Address;
import com.homebase.ecom.user.domain.model.Preferences;
import com.homebase.ecom.user.domain.model.User;
import com.homebase.ecom.user.domain.model.UserActivityLog;
import com.homebase.ecom.user.infrastructure.persistence.entity.AddressJpaEntity;
import com.homebase.ecom.user.infrastructure.persistence.entity.PreferencesJpaEntity;
import com.homebase.ecom.user.infrastructure.persistence.entity.UserActivityLogEntity;
import com.homebase.ecom.user.infrastructure.persistence.entity.UserJpaEntity;

import java.util.stream.Collectors;

/**
 * Bidirectional mapper between Domain and JPA entities.
 * No Spring annotations (@Component) per gemini.md rules.
 */
public class UserMapper {

    public User toModel(UserJpaEntity entity) {
        if (entity == null) return null;

        User model = new User();
        // Base entity fields
        model.setId(entity.getId());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setVersion(entity.getVersion());
        model.setCurrentState(entity.getCurrentState());

        // User fields
        model.setKeycloakId(entity.getKeycloakId());
        model.setEmail(entity.getEmail());
        model.setFirstName(entity.getFirstName());
        model.setLastName(entity.getLastName());
        model.setPhone(entity.getPhone());
        model.setRole(entity.getRole());
        model.setDefaultAddressId(entity.getDefaultAddressId());
        model.setKycStatus(entity.getKycStatus());
        model.setLoginAttempts(entity.getLoginAttempts());
        model.setLastLoginAt(entity.getLastLoginAt());
        model.setLockReason(entity.getLockReason());
        model.setSuspendReason(entity.getSuspendReason());
        model.setTenant(entity.tenant);

        if (entity.getPreferences() != null) {
            model.setPreferences(toModel(entity.getPreferences()));
        }

        if (entity.getAddresses() != null) {
            model.setAddresses(entity.getAddresses().stream()
                    .map(this::toModel)
                    .collect(Collectors.toList()));
        }

        if (entity.getActivities() != null) {
            model.getActivities().addAll(entity.getActivities().stream()
                    .map(this::toModel)
                    .collect(Collectors.toList()));
        }

        return model;
    }

    public UserJpaEntity toEntity(User model) {
        if (model == null) return null;

        UserJpaEntity entity = new UserJpaEntity();
        // Base entity fields
        entity.setId(model.getId());
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setVersion(model.getVersion() != null ? model.getVersion() : 0L);
        entity.setCurrentState(model.getCurrentState());

        // User fields
        entity.setKeycloakId(model.getKeycloakId());
        entity.setEmail(model.getEmail());
        entity.setFirstName(model.getFirstName());
        entity.setLastName(model.getLastName());
        entity.setPhone(model.getPhone());
        entity.setRole(model.getRole());
        entity.setDefaultAddressId(model.getDefaultAddressId());
        entity.setKycStatus(model.getKycStatus());
        entity.setLoginAttempts(model.getLoginAttempts());
        entity.setLastLoginAt(model.getLastLoginAt());
        entity.setLockReason(model.getLockReason());
        entity.setSuspendReason(model.getSuspendReason());
        entity.tenant = model.getTenant();

        if (model.getPreferences() != null) {
            entity.setPreferences(toEntity(model.getPreferences()));
        }

        if (model.getAddresses() != null) {
            entity.setAddresses(model.getAddresses().stream()
                    .map(a -> {
                        AddressJpaEntity ae = toEntity(a);
                        ae.setUser(entity);
                        return ae;
                    })
                    .collect(Collectors.toList()));
        }

        if (model.getActivities() != null) {
            entity.setActivities(model.getActivities().stream()
                    .map(this::toEntity)
                    .collect(Collectors.toList()));
        }

        return entity;
    }

    public Address toModel(AddressJpaEntity entity) {
        if (entity == null) return null;
        Address model = new Address();
        model.setId(entity.getId());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setVersion(entity.getVersion());

        model.setLabel(entity.getLabel());
        model.setLine1(entity.getLine1());
        model.setLine2(entity.getLine2());
        model.setCity(entity.getCity());
        model.setState(entity.getState());
        model.setPostalCode(entity.getPostalCode());
        model.setCountry(entity.getCountry());
        model.setDefault(entity.isDefault());
        return model;
    }

    public AddressJpaEntity toEntity(Address model) {
        if (model == null) return null;
        AddressJpaEntity entity = new AddressJpaEntity();
        entity.setId(model.getId());
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setVersion(model.getVersion() != null ? model.getVersion() : 0L);

        entity.setLabel(model.getLabel());
        entity.setLine1(model.getLine1());
        entity.setLine2(model.getLine2());
        entity.setCity(model.getCity());
        entity.setState(model.getState());
        entity.setPostalCode(model.getPostalCode());
        entity.setCountry(model.getCountry());
        entity.setDefault(model.isDefault());
        return entity;
    }

    public Preferences toModel(PreferencesJpaEntity entity) {
        if (entity == null) return null;
        Preferences model = new Preferences();
        model.setLanguage(entity.getLanguage());
        model.setCurrency(entity.getCurrency());
        model.setEmailNotifications(entity.isEmailNotifications());
        model.setSmsNotifications(entity.isSmsNotifications());
        model.setTimezone(entity.getTimezone());
        return model;
    }

    public PreferencesJpaEntity toEntity(Preferences model) {
        if (model == null) return null;
        PreferencesJpaEntity entity = new PreferencesJpaEntity();
        entity.setLanguage(model.getLanguage());
        entity.setCurrency(model.getCurrency());
        entity.setEmailNotifications(model.isEmailNotifications());
        entity.setSmsNotifications(model.isSmsNotifications());
        entity.setTimezone(model.getTimezone());
        return entity;
    }

    public UserActivityLog toModel(UserActivityLogEntity entity) {
        if (entity == null) return null;
        UserActivityLog model = new UserActivityLog();
        model.setId(entity.getId());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setVersion(entity.getVersion());
        model.setActivityName(entity.getName());
        model.setActivityComment(entity.getComment());
        model.setActivitySuccess(entity.getSuccess());
        return model;
    }

    public UserActivityLogEntity toEntity(UserActivityLog model) {
        if (model == null) return null;
        UserActivityLogEntity entity = new UserActivityLogEntity();
        entity.setId(model.getId());
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setVersion(model.getVersion() != null ? model.getVersion() : 0L);
        entity.setName(model.getActivityName());
        entity.setComment(model.getActivityComment());
        entity.setSuccess(model.isActivitySuccess());
        return entity;
    }
}
