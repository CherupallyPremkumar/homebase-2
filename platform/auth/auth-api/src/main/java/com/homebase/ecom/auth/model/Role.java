package com.homebase.ecom.auth.model;

/**
 * Platform roles — enum name matches Keycloak realm role name exactly.
 * No ROLE_ prefix — Keycloak stores plain names.
 * Spring Security adds SCOPE_ at runtime via Chenile's converter.
 *
 * Keycloak role → Chenile ACL match
 * CUSTOMER      → meta-acls="CUSTOMER"
 */
public enum Role {
    CUSTOMER,
    SELLER,
    SUPPLIER,
    ADMIN,
    WAREHOUSE,
    AGENT,
    SYSTEM,
    QUALITY_CHECKER,
    EDITOR,
    FINANCE
}
