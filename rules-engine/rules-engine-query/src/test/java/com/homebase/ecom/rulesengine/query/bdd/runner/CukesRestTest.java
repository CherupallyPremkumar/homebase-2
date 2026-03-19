package com.homebase.ecom.rulesengine.query.bdd.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/rest/features",
    glue = {"classpath:org/chenile/cucumber/rest",
            "classpath:org/chenile/cucumber/security/rest",
            "classpath:com/homebase/ecom/rulesengine/query/bdd"},
    plugin = {"pretty"})
@ActiveProfiles("unittest")
public class CukesRestTest {
}
