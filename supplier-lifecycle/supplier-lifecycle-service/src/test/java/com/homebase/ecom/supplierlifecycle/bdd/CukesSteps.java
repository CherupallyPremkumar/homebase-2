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
public class CukesSteps extends BaseSecurityTest {

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
