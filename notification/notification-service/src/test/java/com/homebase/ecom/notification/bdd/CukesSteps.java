package com.homebase.ecom.notification.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import com.homebase.ecom.notification.SpringTestConfig;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.chenile.security.test.BaseSecurityTest;
import io.cucumber.java.en.Given;

/**
 * Hooks up the SpringConfig to the Cucumber test.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringTestConfig.class)
@AutoConfigureMockMvc
@CucumberContextConfiguration
@ActiveProfiles("unittest")
public class CukesSteps extends org.chenile.security.test.BaseSecurityTest {
    static {
        BaseSecurityTest.startContainer();
    }

    @DynamicPropertySource
    static void keycloakProps(DynamicPropertyRegistry registry) {
        BaseSecurityTest.keycloakProperties(registry);
        registry.add("chenile.security.client.id", () -> "authz-servlet");
        registry.add("chenile.security.client.secret", () -> "secret");
    }

    @Given("dummy") public void dummy() {}
}
