package com.homebase.ecom.catalog.query.bdd;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;


@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/rest/features",
		glue = {"classpath:org/chenile/cucumber/rest", "classpath:com/homebase/ecom/catalog/query/bdd"},
        plugin = {"pretty"}
        )
@ActiveProfiles("unittest")
public class CukesRestTest {

}
