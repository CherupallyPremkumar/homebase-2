package com.homebase.ecom.policy.bdd.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.homebase.ecom.policy.service.bdd.steps",
    plugin = {"pretty", "html:target/cucumber-reports.html"}
)
public class CucumberIT {
}
