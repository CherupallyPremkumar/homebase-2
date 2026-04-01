package com.homebase.ecom.user.domain.valueobject;

import java.util.Objects;

/**
 * Value Object: Keycloak subject ID (the `sub` claim from JWT).
 * This is the canonical identity key linking User BC ↔ Keycloak.
 */
public class KeycloakId {

    private final String value;

    public KeycloakId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("KeycloakId cannot be blank");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeycloakId)) return false;
        return value.equals(((KeycloakId) o).value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
