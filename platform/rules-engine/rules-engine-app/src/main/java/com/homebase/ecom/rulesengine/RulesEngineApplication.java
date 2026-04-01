package com.homebase.ecom.rulesengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.homebase.ecom.rulesengine", "org.chenile"})
public class RulesEngineApplication {
    public static void main(String[] args) {
        SpringApplication.run(RulesEngineApplication.class, args);
    }
}
