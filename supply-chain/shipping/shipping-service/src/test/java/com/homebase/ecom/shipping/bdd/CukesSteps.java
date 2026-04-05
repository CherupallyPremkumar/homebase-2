package com.homebase.ecom.shipping.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import com.homebase.ecom.shipping.SpringTestConfig;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.chenile.cucumber.CukesContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Cucumber Spring context configuration for Shipping BDD tests.
 *
 * Security interceptor is disabled in test config (chenile.post.processors=),
 * so we don't need a real Keycloak testcontainer. The authorization header
 * step just sets a dummy Bearer token to satisfy header parsing.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringTestConfig.class)
@AutoConfigureMockMvc
@CucumberContextConfiguration
@ActiveProfiles("unittest")
public class CukesSteps {

    CukesContext context = CukesContext.CONTEXT;

    @When("I construct a REST request with authorization header in realm {string} for user {string} and password {string}")
    public void i_construct_an_authorized_REST_request(String realm, String user, String password) {
        // Security interceptor is disabled in unit tests (chenile.post.processors=)
        // Just set a dummy token so header parsing doesn't fail
        Map<String, String> headers = context.get("headers");
        if (headers == null) {
            headers = new HashMap<>();
            context.set("headers", headers);
        }
        headers.put("Authorization", "Bearer dummy-test-token");
    }

    @Given("dummy") public void dummy() {}


}
