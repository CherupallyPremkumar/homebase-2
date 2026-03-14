package com.homebase.ecom.supplierlifecycle.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import com.homebase.ecom.supplierlifecycle.SpringTestConfig;
import org.chenile.security.test.BaseSecurityTest;

import io.cucumber.java.en.Given;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringTestConfig.class)
@AutoConfigureMockMvc
@CucumberContextConfiguration
@ActiveProfiles("unittest")
public class CukesSteps {

    @DynamicPropertySource
    static void keycloakProps(DynamicPropertyRegistry registry) {
        BaseSecurityTest.startContainer();
        registry.add("KEYCLOAK_HOST", BaseSecurityTest::getUrl);
        registry.add("KEYCLOAK_PORT", BaseSecurityTest::getHttpPort);
        registry.add("KEYCLOAK_REALM", () -> "tenant0");
        // Also set the standard properties that BaseSecurityTest sets
        BaseSecurityTest.keycloakProperties(registry);
    }

    @Given("dummy") public void dummy() {}
}
