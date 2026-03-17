package com.homebase.ecom.rulesengine.service.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.domain.repository.RuleSetRepository;
import org.chenile.core.context.ContextContainer;
import org.chenile.core.context.HeaderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;

public class RuleSetDataLoader implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(RuleSetDataLoader.class);

    private final RuleSetRepository ruleSetRepository;
    private final ObjectMapper objectMapper;
    private final ContextContainer contextContainer;

    public RuleSetDataLoader(RuleSetRepository ruleSetRepository, ObjectMapper objectMapper, ContextContainer contextContainer) {
        this.ruleSetRepository = ruleSetRepository;
        this.objectMapper = objectMapper;
        this.contextContainer = contextContainer;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Checking if default rule sets need to be loaded...");

        // Load default rule sets if the table is empty
        if (ruleSetRepository.findAll().isEmpty()) {
            log.info("No rule sets found in database. Loading defaults from policy-rules.json");
            try (InputStream is = new ClassPathResource("policy-rules.json").getInputStream()) {
                JsonNode rootNode = objectMapper.readTree(is);
                JsonNode policiesNode = rootNode.get("policies");

                if (policiesNode != null && policiesNode.isArray()) {
                    List<RuleSet> defaultRuleSets = objectMapper.convertValue(
                            policiesNode,
                            new TypeReference<List<RuleSet>>() {}
                    );

                    for (RuleSet ruleSet : defaultRuleSets) {
                        try {
                            // Set tenant in ContextContainer so @PrePersist picks it up
                            String tenant = ruleSet.getTenant();
                            if (tenant != null && !tenant.isBlank()) {
                                contextContainer.put(HeaderUtils.TENANT_ID_KEY, tenant);
                            }
                            ruleSetRepository.save(ruleSet);
                            log.info("Successfully loaded rule set: {} ({}) for tenant: {}", ruleSet.getId(), ruleSet.getName(), tenant);
                        } catch (Exception e) {
                            log.error("Failed to save rule set: {}", ruleSet.getId(), e);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Error reading or parsing policy-rules.json", e);
            }
        } else {
            log.info("Rule sets already exist in the database. Skipping default load.");
        }
    }
}
