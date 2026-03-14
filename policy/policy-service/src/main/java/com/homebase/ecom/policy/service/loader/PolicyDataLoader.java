package com.homebase.ecom.policy.service.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebase.ecom.policy.domain.model.Policy;
import com.homebase.ecom.policy.domain.repository.PolicyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;

public class PolicyDataLoader implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(PolicyDataLoader.class);
    
    private final PolicyRepository policyRepository;
    private final ObjectMapper objectMapper;

    public PolicyDataLoader(PolicyRepository policyRepository, ObjectMapper objectMapper) {
        this.policyRepository = policyRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Checking if default policies need to be loaded...");
        
        // Load default policies if the table is empty
        if (policyRepository.findAll().isEmpty()) {
            log.info("No policies found in database. Loading defaults from policy-rules.json");
            try (InputStream is = new ClassPathResource("policy-rules.json").getInputStream()) {
                JsonNode rootNode = objectMapper.readTree(is);
                JsonNode policiesNode = rootNode.get("policies");
                
                if (policiesNode != null && policiesNode.isArray()) {
                    List<Policy> defaultPolicies = objectMapper.convertValue(
                            policiesNode, 
                            new TypeReference<List<Policy>>() {}
                    );
                    
                    for (Policy policy : defaultPolicies) {
                        try {
                            policyRepository.save(policy);
                            log.info("Successfully loaded policy: {} ({})", policy.getId(), policy.getName());
                        } catch (Exception e) {
                            log.error("Failed to save policy: {}", policy.getId(), e);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Error reading or parsing policy-rules.json", e);
            }
        } else {
            log.info("Policies already exist in the database. Skipping default load.");
        }
    }
}
