package com.homebase.ecom.user.infrastructure.persistence.mapper;

import com.homebase.ecom.user.domain.model.Address;
import com.homebase.ecom.user.domain.model.Preferences;
import com.homebase.ecom.user.domain.model.User;
import com.homebase.ecom.user.domain.model.UserActivityLog;
import com.homebase.ecom.user.infrastructure.persistence.entity.AddressJpaEntity;
import com.homebase.ecom.user.infrastructure.persistence.entity.PreferencesJpaEntity;
import com.homebase.ecom.user.infrastructure.persistence.entity.UserActivityLogEntity;
import com.homebase.ecom.user.infrastructure.persistence.entity.UserJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserMapper bidirectional mapping.
 */
class UserMapperTest {

    private UserMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserMapper();
    }

    // --- User: Domain -> JPA ---

    @Test
    void toEntity_mapsAllUserFields() {
        User model = createSampleUser();

        UserJpaEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals("user-123", entity.getId());
        assertEquals("kc-456", entity.getKeycloakId());
        assertEquals("prem@homebase.com", entity.getEmail());
        assertEquals("Prem", entity.getFirstName());
        assertEquals("Kumar", entity.getLastName());
        assertEquals("+91-9876543210", entity.getPhone());
        assertEquals("CUSTOMER", entity.getRole());
        assertEquals("addr-1", entity.getDefaultAddressId());
        assertEquals("KYC_VERIFIED", entity.getKycStatus());
        assertEquals(0, entity.getLoginAttempts());
        assertNotNull(entity.getLastLoginAt());
        assertNull(entity.getLockReason());
        assertNull(entity.getSuspendReason());
    }

    @Test
    void toEntity_mapsPreferences() {
        User model = createSampleUser();

        UserJpaEntity entity = mapper.toEntity(model);

        assertNotNull(entity.getPreferences());
        assertEquals("en", entity.getPreferences().getLanguage());
        assertEquals("INR", entity.getPreferences().getCurrency());
        assertTrue(entity.getPreferences().isEmailNotifications());
    }

    @Test
    void toEntity_mapsAddresses() {
        User model = createSampleUser();

        UserJpaEntity entity = mapper.toEntity(model);

        assertNotNull(entity.getAddresses());
        assertEquals(1, entity.getAddresses().size());
        AddressJpaEntity addrEntity = entity.getAddresses().get(0);
        assertEquals("addr-1", addrEntity.getId());
        assertEquals("Home", addrEntity.getLabel());
        assertEquals("123 Main St", addrEntity.getLine1());
        assertEquals("Bangalore", addrEntity.getCity());
        assertEquals("560001", addrEntity.getPostalCode());
        assertTrue(addrEntity.isDefault());
        assertEquals(entity, addrEntity.getUser()); // back-reference set
    }

    // --- User: JPA -> Domain ---

    @Test
    void toModel_mapsAllUserFields() {
        UserJpaEntity entity = createSampleJpaUser();

        User model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals("user-123", model.getId());
        assertEquals("kc-456", model.getKeycloakId());
        assertEquals("prem@homebase.com", model.getEmail());
        assertEquals("Prem", model.getFirstName());
        assertEquals("Kumar", model.getLastName());
        assertEquals("+91-9876543210", model.getPhone());
        assertEquals("SELLER", model.getRole());
        assertEquals("addr-1", model.getDefaultAddressId());
        assertEquals("KYC_PENDING", model.getKycStatus());
        assertEquals(2, model.getLoginAttempts());
    }

    @Test
    void toModel_mapsPreferences() {
        UserJpaEntity entity = createSampleJpaUser();

        User model = mapper.toModel(entity);

        assertNotNull(model.getPreferences());
        assertEquals("hi", model.getPreferences().getLanguage());
        assertEquals("USD", model.getPreferences().getCurrency());
    }

    @Test
    void toModel_mapsAddresses() {
        UserJpaEntity entity = createSampleJpaUser();

        User model = mapper.toModel(entity);

        assertNotNull(model.getAddresses());
        assertEquals(1, model.getAddresses().size());
        Address addr = model.getAddresses().get(0);
        assertEquals("addr-1", addr.getId());
        assertEquals("Office", addr.getLabel());
        assertEquals("456 MG Road", addr.getLine1());
        assertEquals("Mumbai", addr.getCity());
        assertEquals("400001", addr.getPostalCode());
    }

    // --- Null handling ---

    @Test
    void toEntity_returnsNullForNullInput() {
        assertNull(mapper.toEntity((User) null));
    }

    @Test
    void toModel_returnsNullForNullInput() {
        assertNull(mapper.toModel((UserJpaEntity) null));
    }

    // --- Address bidirectional ---

    @Test
    void address_roundTrip() {
        Address original = new Address();
        original.setId("a-1");
        original.setLabel("Home");
        original.setLine1("123 St");
        original.setLine2("Apt 4");
        original.setCity("Delhi");
        original.setState("DL");
        original.setPostalCode("110001");
        original.setCountry("IN");
        original.setDefault(true);

        AddressJpaEntity entity = mapper.toEntity(original);
        Address roundTripped = mapper.toModel(entity);

        assertEquals(original.getId(), roundTripped.getId());
        assertEquals(original.getLabel(), roundTripped.getLabel());
        assertEquals(original.getLine1(), roundTripped.getLine1());
        assertEquals(original.getLine2(), roundTripped.getLine2());
        assertEquals(original.getCity(), roundTripped.getCity());
        assertEquals(original.getState(), roundTripped.getState());
        assertEquals(original.getPostalCode(), roundTripped.getPostalCode());
        assertEquals(original.getCountry(), roundTripped.getCountry());
        assertEquals(original.isDefault(), roundTripped.isDefault());
    }

    // --- Preferences bidirectional ---

    @Test
    void preferences_roundTrip() {
        Preferences original = Preferences.of("fr", "EUR", false, true, "Europe/Paris");

        PreferencesJpaEntity entity = mapper.toEntity(original);
        Preferences roundTripped = mapper.toModel(entity);

        assertEquals("fr", roundTripped.getLanguage());
        assertEquals("EUR", roundTripped.getCurrency());
        assertFalse(roundTripped.isEmailNotifications());
        assertTrue(roundTripped.isSmsNotifications());
        assertEquals("Europe/Paris", roundTripped.getTimezone());
    }

    // --- Activity Log bidirectional ---

    @Test
    void activityLog_roundTrip() {
        UserActivityLog original = new UserActivityLog();
        original.setId("log-1");
        original.setActivityName("verifyEmail");
        original.setActivityComment("Email verified");
        original.setActivitySuccess(true);

        UserActivityLogEntity entity = mapper.toEntity(original);
        UserActivityLog roundTripped = mapper.toModel(entity);

        assertEquals("log-1", roundTripped.getId());
        assertEquals("verifyEmail", roundTripped.getActivityName());
        assertEquals("Email verified", roundTripped.getActivityComment());
        assertTrue(roundTripped.isActivitySuccess());
    }

    // --- Helper methods ---

    private User createSampleUser() {
        User user = new User();
        user.setId("user-123");
        user.setKeycloakId("kc-456");
        user.setEmail("prem@homebase.com");
        user.setFirstName("Prem");
        user.setLastName("Kumar");
        user.setPhone("+91-9876543210");
        user.setRole("CUSTOMER");
        user.setDefaultAddressId("addr-1");
        user.setKycStatus("KYC_VERIFIED");
        user.setLoginAttempts(0);
        user.setLastLoginAt(Instant.now());

        Preferences prefs = Preferences.of("en", "INR", true, true, "Asia/Kolkata");
        user.setPreferences(prefs);

        Address addr = new Address();
        addr.setId("addr-1");
        addr.setLabel("Home");
        addr.setLine1("123 Main St");
        addr.setCity("Bangalore");
        addr.setState("KA");
        addr.setPostalCode("560001");
        addr.setCountry("IN");
        addr.setDefault(true);
        user.setAddresses(List.of(addr));

        return user;
    }

    private UserJpaEntity createSampleJpaUser() {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId("user-123");
        entity.setKeycloakId("kc-456");
        entity.setEmail("prem@homebase.com");
        entity.setFirstName("Prem");
        entity.setLastName("Kumar");
        entity.setPhone("+91-9876543210");
        entity.setRole("SELLER");
        entity.setDefaultAddressId("addr-1");
        entity.setKycStatus("KYC_PENDING");
        entity.setLoginAttempts(2);

        PreferencesJpaEntity prefs = new PreferencesJpaEntity();
        prefs.setLanguage("hi");
        prefs.setCurrency("USD");
        prefs.setEmailNotifications(true);
        prefs.setSmsNotifications(false);
        prefs.setTimezone("Asia/Kolkata");
        entity.setPreferences(prefs);

        AddressJpaEntity addr = new AddressJpaEntity();
        addr.setId("addr-1");
        addr.setLabel("Office");
        addr.setLine1("456 MG Road");
        addr.setCity("Mumbai");
        addr.setState("MH");
        addr.setPostalCode("400001");
        addr.setCountry("IN");
        addr.setDefault(false);
        addr.setUser(entity);
        entity.setAddresses(List.of(addr));

        return entity;
    }
}
