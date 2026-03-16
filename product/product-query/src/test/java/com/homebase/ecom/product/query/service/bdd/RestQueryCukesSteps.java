package com.homebase.ecom.product.query.service.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.chenile.security.test.BaseSecurityTest;
import org.chenile.cucumber.CukesContext;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringTestConfig.class)
@AutoConfigureMockMvc
@CucumberContextConfiguration
@ActiveProfiles("unittest")
public class RestQueryCukesSteps {

    @DynamicPropertySource
    static void keycloakProps(DynamicPropertyRegistry registry) {
        registry.add("KEYCLOAK_HOST", BaseSecurityTest::getUrl);
        registry.add("KEYCLOAK_PORT", BaseSecurityTest::getHttpPort);
        registry.add("KEYCLOAK_REALM", () -> "tenant0");
        BaseSecurityTest.keycloakProperties(registry);
    }

    @When("I construct a REST request with authorization header in realm {string} for user {string} and password {string}")
    @SuppressWarnings("unchecked")
    public void i_construct_an_authorized_REST_request(String realm, String user, String password) {
        CukesContext context = CukesContext.CONTEXT;
        Map<String, String> headers = (Map<String, String>) context.get("headers");
        if (headers == null) {
            headers = new HashMap<>();
            context.set("headers", headers);
        }
        String token = getKeycloakToken(realm, user, password);
        headers.put("Authorization", "Bearer " + token);
    }

    /**
     * Fetches a token from Keycloak using the password grant.
     * Replaces BaseSecurityTest.getToken() which is incompatible with Spring 7
     * due to HttpHeaders no longer implementing MultiValueMap.
     */
    @SuppressWarnings("unchecked")
    private String getKeycloakToken(String realm, String user, String password) {
        String baseUrl = BaseSecurityTest.getUrl();
        // getUrl() returns full URL like http://localhost:32769, no need to append port
        String tokenUrl = baseUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", "admin-cli");
        body.add("username", user);
        body.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);
        Map<String, Object> response = restTemplate.postForObject(tokenUrl, request, Map.class);
        return (String) response.get("access_token");
    }

    @Given("dummy")
    public void dummy() {
    }
}
