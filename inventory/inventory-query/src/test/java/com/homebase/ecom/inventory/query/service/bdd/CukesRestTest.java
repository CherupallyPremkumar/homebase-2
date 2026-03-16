package com.homebase.ecom.inventory.query.service.bdd;

import org.springframework.test.context.ActiveProfiles;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/rest/features",
    glue = {"classpath:com/homebase/ecom/inventory/query/service/bdd",
    "classpath:org/chenile/cucumber/rest"},
    plugin = {"pretty"}
)
@ActiveProfiles("unittest")
public class CukesRestTest {
}
