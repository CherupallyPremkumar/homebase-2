package com.homebase.ecom.promo.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import com.homebase.ecom.promo.SpringTestConfig;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.chenile.security.test.BaseSecurityTest;
import io.cucumber.java.en.Given;


/**
* This "steps" file hooks up the SpringConfig to the test case.
* Custom BDD steps for Promo module can be added here.
*/
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringTestConfig.class)
@AutoConfigureMockMvc
@CucumberContextConfiguration
@ActiveProfiles("unittest")
public class CukesSteps extends org.chenile.security.test.BaseSecurityTest {
	@Given("dummy") public void dummy(){}
}
