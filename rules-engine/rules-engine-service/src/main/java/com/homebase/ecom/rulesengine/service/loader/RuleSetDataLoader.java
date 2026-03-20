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
        log.info("Loading rule sets from policy-rules.json (merge by ID)...");

        try (InputStream is = new ClassPathResource("policy-rules.json").getInputStream()) {
            JsonNode rootNode = objectMapper.readTree(is);
            JsonNode policiesNode = rootNode.get("policies");

            if (policiesNode != null && policiesNode.isArray()) {
                List<RuleSet> defaultRuleSets = objectMapper.convertValue(
                        policiesNode,
                        new TypeReference<List<RuleSet>>() {}
                );

                int loaded = 0;
                int skipped = 0;
                for (RuleSet ruleSet : defaultRuleSets) {
                    try {
                        // Skip if this specific rule set already exists by ID
                        if (ruleSet.getId() != null && ruleSetRepository.findById(ruleSet.getId()).isPresent()) {
                            skipped++;
                            continue;
                        }

                        // Set tenant in ContextContainer so @PrePersist picks it up
                        String tenant = ruleSet.getTenant();
                        if (tenant != null && !tenant.isBlank()) {
                            contextContainer.put(HeaderUtils.TENANT_ID_KEY, tenant);
                        }
                        ruleSetRepository.save(ruleSet);
                        loaded++;
                        log.info("Loaded rule set: {} ({}) for tenant: {}", ruleSet.getId(), ruleSet.getName(), tenant);
                    } catch (Exception e) {
                        log.error("Failed to save rule set: {}", ruleSet.getId(), e);
                    }
                }
                log.info("Rule set loading complete: {} loaded, {} skipped (already exist)", loaded, skipped);
            }
        } catch (Exception e) {
            log.error("Error reading or parsing policy-rules.json", e);
        }
    }
}
