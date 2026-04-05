package com.homebase.ecom.cms.query.service.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/rest/features",
    glue = {"org/chenile/cucumber/rest", "org/chenile/cucumber/security/rest", "com/homebase/ecom/cms/query/service/bdd"})
public class CukesRestTest {
}
