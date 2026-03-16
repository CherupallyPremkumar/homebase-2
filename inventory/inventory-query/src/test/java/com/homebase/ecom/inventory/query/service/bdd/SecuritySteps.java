package com.homebase.ecom.inventory.query.service.bdd;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.cucumber.java.en.When;
import org.chenile.cucumber.CukesContext;
import org.chenile.security.test.BaseSecurityTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Local Cucumber 7 step definitions for security token injection.
 * Replaces the cucumber-sec-utils RestCukesSecSteps which uses old cucumber.api imports
 * and Spring 5 API (HttpHeaders implementing MultiValueMap).
 */
public class SecuritySteps {
    @Autowired
    private MockMvc mvc;

    CukesContext context = CukesContext.CONTEXT;

    @When("I construct a REST request with authorization header in realm {string} for user {string} and password {string}")
    public void i_construct_an_authorized_REST_request_in_realm_for_user_and_password(
            String realm, String user, String password) {
        @SuppressWarnings("unchecked")
        Map<String, String> headers = context.get("headers");
        if (headers == null) {
            headers = new HashMap<>();
            context.set("headers", headers);
        }
        String headerName = "Authorization";
        String headerValue = "Bearer " + getToken(realm, user, password);
        headers.put(headerName, headerValue);
    }

    /**
     * Fetch an OAuth2 token from the Keycloak testcontainer.
     * Re-implemented here to avoid IncompatibleClassChangeError from
     * BaseSecurityTest compiled against older Spring where HttpHeaders
     * implemented MultiValueMap.
     */
    private static String getToken(String realm, String user, String password) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("grant_type", Collections.singletonList("password"));
        map.put("client_id", Collections.singletonList("authz-servlet"));
        map.put("client_secret", Collections.singletonList("secret"));
        map.put("username", Collections.singletonList(user));
        map.put("password", Collections.singletonList(password));

        String authServerUrl = BaseSecurityTest.getUrl() + "/realms/" + realm +
                "/protocol/openid-connect/token";
        var request = new HttpEntity<>(map, httpHeaders);
        KeyCloakToken token = restTemplate.postForObject(
                authServerUrl,
                request,
                KeyCloakToken.class
        );

        assert token != null;
        return token.accessToken();
    }

    record KeyCloakToken(@JsonProperty("access_token") String accessToken) {
    }
}
