package com.homebase.ecom.rulesengine.bdd.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"classpath:com/homebase/ecom/rulesengine/bdd",
        "classpath:com/homebase/ecom/rulesengine/bdd/steps",
        "classpath:org/chenile/cucumber/rest",
        "classpath:org/chenile/cucumber/workflow"},
    plugin = {"pretty", "html:target/cucumber-reports.html"}
)
public class CukesRestTest {
}
