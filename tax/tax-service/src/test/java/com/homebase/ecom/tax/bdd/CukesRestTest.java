package com.homebase.ecom.tax.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features",
    glue = {"classpath:com/homebase/ecom/tax/bdd",
    "classpath:org/chenile/cucumber/rest",
    "classpath:org/chenile/cucumber/security/rest"},
    plugin = {"pretty"}
)
@ActiveProfiles("unittest")
public class CukesRestTest {
}
