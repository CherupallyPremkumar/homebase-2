package com.homebase.ecom.user.infrastructure.adapter;

import com.homebase.ecom.user.domain.port.IdentityProviderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

/**
 * Infrastructure adapter for IdentityProviderPort.
 * Delegates to Keycloak Admin API for identity operations.
 *
 * Obtains an admin access token via client credentials grant, then calls
 * Keycloak Admin REST endpoints for user management.
 *
 * No @Component -- wired in UserInfrastructureConfiguration.
 */
public class IdentityProviderAdapter implements IdentityProviderPort {

    private static final Logger log = LoggerFactory.getLogger(IdentityProviderAdapter.class);

    private final RestClient restClient;
    private final String keycloakBaseUrl;
    private final String realm;
    private final String clientId;
    private final String clientSecret;

    /**
     * @param keycloakBaseUrl Keycloak server URL (e.g. http://localhost:8180)
     * @param realm           Keycloak realm name (e.g. homebase)
     * @param clientId        Service account client ID with admin privileges
     * @param clientSecret    Service account client secret
     */
    public IdentityProviderAdapter(String keycloakBaseUrl, String realm,
                                   String clientId, String clientSecret) {
        this.keycloakBaseUrl = keycloakBaseUrl;
        this.realm = realm;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.restClient = RestClient.builder()
                .baseUrl(keycloakBaseUrl)
                .build();
    }

    @Override
    public void sendVerificationEmail(String userId, String email) {
        log.info("Identity provider: sending verification email to {} for user {}", email, userId);
        // TODO: Keycloak Admin API -- trigger verify-email required action
        try {
            String token = obtainAdminToken();
            restClient.put()
                    .uri("/admin/realms/{realm}/users/{userId}/execute-actions-email", realm, userId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(List.of("VERIFY_EMAIL"))
                    .retrieve()
                    .toBodilessEntity();
            log.info("Verification email sent successfully for user {}", userId);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("User {} not found in Keycloak realm {}, cannot send verification email",
                        userId, realm);
            } else {
                log.error("Failed to send verification email for user {}: {} {}",
                        userId, e.getStatusCode(), e.getResponseBodyAsString(), e);
            }
        } catch (Exception e) {
            log.error("Failed to send verification email for user {}: {}", userId, e.getMessage(), e);
        }
    }

    @Override
    public void revokeAllSessions(String userId) {
        log.info("Identity provider: revoking all sessions for user {}", userId);
        // TODO: Keycloak Admin API -- DELETE /users/{id}/sessions
        try {
            String token = obtainAdminToken();
            restClient.delete()
                    .uri("/admin/realms/{realm}/users/{userId}/sessions", realm, userId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .toBodilessEntity();
            log.info("All sessions revoked successfully for user {}", userId);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("User {} not found in Keycloak realm {}, cannot revoke sessions",
                        userId, realm);
            } else {
                log.error("Failed to revoke sessions for user {}: {} {}",
                        userId, e.getStatusCode(), e.getResponseBodyAsString(), e);
            }
        } catch (Exception e) {
            log.error("Failed to revoke sessions for user {}: {}", userId, e.getMessage(), e);
        }
    }

    @Override
    public void disableUser(String userId) {
        log.info("Identity provider: disabling user {}", userId);
        // TODO: Keycloak Admin API -- PUT /users/{id} with enabled=false
        setUserEnabled(userId, false);
    }

    @Override
    public void enableUser(String userId) {
        log.info("Identity provider: enabling user {}", userId);
        // TODO: Keycloak Admin API -- PUT /users/{id} with enabled=true
        setUserEnabled(userId, true);
    }

    /**
     * Sets the enabled flag on a Keycloak user.
     */
    private void setUserEnabled(String userId, boolean enabled) {
        try {
            String token = obtainAdminToken();
            restClient.put()
                    .uri("/admin/realms/{realm}/users/{userId}", realm, userId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("enabled", enabled))
                    .retrieve()
                    .toBodilessEntity();
            log.info("User {} {} successfully in Keycloak", userId, enabled ? "enabled" : "disabled");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("User {} not found in Keycloak realm {}, cannot {} user",
                        userId, realm, enabled ? "enable" : "disable");
            } else {
                log.error("Failed to {} user {}: {} {}",
                        enabled ? "enable" : "disable", userId,
                        e.getStatusCode(), e.getResponseBodyAsString(), e);
            }
        } catch (Exception e) {
            log.error("Failed to {} user {}: {}", enabled ? "enable" : "disable",
                    userId, e.getMessage(), e);
        }
    }

    /**
     * Obtains an admin access token from Keycloak using client credentials grant.
     * The client must have the 'realm-management' role or equivalent admin privileges.
     *
     * @return bearer access token
     * @throws RuntimeException if token acquisition fails
     */
    @SuppressWarnings("unchecked")
    private String obtainAdminToken() {
        try {
            Map<String, Object> tokenResponse = restClient.post()
                    .uri("/realms/{realm}/protocol/openid-connect/token", realm)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body("grant_type=client_credentials&client_id=" + clientId
                            + "&client_secret=" + clientSecret)
                    .retrieve()
                    .body(Map.class);

            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                throw new RuntimeException("Keycloak token response missing access_token");
            }
            return (String) tokenResponse.get("access_token");
        } catch (HttpClientErrorException e) {
            log.error("Failed to obtain Keycloak admin token: {} {}", e.getStatusCode(),
                    e.getResponseBodyAsString());
            throw new RuntimeException("Failed to obtain Keycloak admin token", e);
        }
    }
}
