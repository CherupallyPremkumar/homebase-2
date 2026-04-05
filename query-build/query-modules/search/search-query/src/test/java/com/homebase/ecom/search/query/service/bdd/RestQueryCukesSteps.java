package com.homebase.ecom.search.query.service.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.chenile.security.test.BaseSecurityTest;
import org.chenile.security.KeycloakConnectionDetails;
import org.chenile.cucumber.CukesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringTestConfig.class)
@AutoConfigureMockMvc
@CucumberContextConfiguration
@ActiveProfiles("unittest")
public class RestQueryCukesSteps {

    @Autowired
    private KeycloakConnectionDetails connectionDetails;
    CukesContext context = CukesContext.CONTEXT;

    @DynamicPropertySource
    static void keycloakProps(DynamicPropertyRegistry registry) {
        registry.add("KEYCLOAK_HOST", BaseSecurityTest::getUrl);
        registry.add("KEYCLOAK_PORT", BaseSecurityTest::getHttpPort);
        registry.add("KEYCLOAK_REALM", () -> "tenant0");
        BaseSecurityTest.keycloakProperties(registry);
    }

    @Before
    public void beforeScenario() {
        connectionDetails.host = BaseSecurityTest.getUrl();
        connectionDetails.httpPort = BaseSecurityTest.getHttpPort();
    }

    @When("I construct a REST request with authorization header in realm {string} for user {string} and password {string}")
    public void i_construct_an_authorized_REST_request(String realm, String user, String password) {
        Map<String, String> headers = context.get("headers");
        if (headers == null) { headers = new HashMap<>(); context.set("headers", headers); }
        headers.put("Authorization", "Bearer " + fetchToken(realm, user, password));
    }

    private static String fetchToken(String realm, String user, String password) {
        RestTemplate rt = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.put("grant_type", Collections.singletonList("password"));
        form.put("client_id", Collections.singletonList("authz-servlet"));
        form.put("client_secret", Collections.singletonList("secret"));
        form.put("username", Collections.singletonList(user));
        form.put("password", Collections.singletonList(password));
        String url = "http://" + BaseSecurityTest.getHost() + ":" + BaseSecurityTest.getHttpPort()
                + "/realms/" + realm + "/protocol/openid-connect/token";
        org.springframework.http.HttpHeaders h = new org.springframework.http.HttpHeaders();
        h.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        TokenResponse t = rt.postForObject(url, new org.springframework.http.HttpEntity<>(form, h), TokenResponse.class);
        return t.accessToken();
    }

    record TokenResponse(@JsonProperty("access_token") String accessToken) {}

    @Given("dummy") public void dummy() {}
}
