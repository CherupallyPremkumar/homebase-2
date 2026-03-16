package com.homebase.ecom.cart.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import com.homebase.ecom.cart.SpringTestConfig;
import org.chenile.security.test.BaseSecurityTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.chenile.cucumber.CukesContext;
import org.chenile.cucumber.VariableHelper;
import org.chenile.security.KeycloakConnectionDetails;
import org.chenile.stm.impl.ConfigProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringTestConfig.class)
@AutoConfigureMockMvc
@CucumberContextConfiguration
@ActiveProfiles("unittest")
public class CukesSteps {

    @Autowired
    private KeycloakConnectionDetails connectionDetails;

    @Autowired
    @Qualifier("cartConfigProvider")
    private ConfigProviderImpl cartConfigProvider;

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
        if (headers == null) {
            headers = new HashMap<>();
            context.set("headers", headers);
        }
        headers.put("Authorization", "Bearer " + fetchToken(realm, user, password));
    }

    /**
     * Spring 7 compatible token fetch (HttpHeaders no longer implements MultiValueMap).
     */
    private static String fetchToken(String realm, String user, String password) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.put("grant_type", Collections.singletonList("password"));
        form.put("client_id", Collections.singletonList("authz-servlet"));
        form.put("client_secret", Collections.singletonList("secret"));
        form.put("username", Collections.singletonList(user));
        form.put("password", Collections.singletonList(password));

        String authUrl = "http://" + BaseSecurityTest.getHost() + ":" + BaseSecurityTest.getHttpPort()
                + "/realms/" + realm + "/protocol/openid-connect/token";

        org.springframework.http.HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        org.springframework.http.HttpEntity<MultiValueMap<String, String>> request =
                new org.springframework.http.HttpEntity<>(form, httpHeaders);
        TokenResponse token = restTemplate.postForObject(authUrl, request, TokenResponse.class);
        assert token != null;
        return token.accessToken();
    }

    record TokenResponse(@JsonProperty("access_token") String accessToken) {}

    /**
     * Workaround for Chenile BDD step "that a new state ... is added to flow ..."
     * which writes property in wrong format (prefix.state.add.{stateId}.in={flowId})
     * vs what ConfigBasedEnablementStrategy expects (prefix.{flowId}.state.add.{stateId}=true).
     */
    @Given("that dynamic state {string} is added to flow {string} with prefix {string}")
    public void addDynamicState(String stateId, String flowId, String prefix) throws Exception {
        stateId = VariableHelper.substituteVariables(stateId);
        flowId = VariableHelper.substituteVariables(flowId);
        prefix = VariableHelper.substituteVariables(prefix);
        String propKey = prefix + "." + flowId + ".state.add." + stateId;
        cartConfigProvider.setProperty(propKey, "true");
    }

    @Given("dummy") public void dummy() {}
}
