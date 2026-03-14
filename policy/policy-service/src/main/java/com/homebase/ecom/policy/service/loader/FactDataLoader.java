package com.homebase.ecom.policy.service.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebase.ecom.policy.domain.model.FactDefinition;
import com.homebase.ecom.policy.domain.repository.FactDefinitionRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;

public class FactDataLoader {
    
    private static final Logger log = LoggerFactory.getLogger(FactDataLoader.class);

    private final FactDefinitionRepository factDefinitionRepository;
    private final ObjectMapper objectMapper;

    public FactDataLoader(FactDefinitionRepository factDefinitionRepository, ObjectMapper objectMapper) {
        this.factDefinitionRepository = factDefinitionRepository;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadFacts() {
        try {
            List<FactDefinition> existing = factDefinitionRepository.findAll();
            if (existing != null && !existing.isEmpty()) {
                log.info("Fact definitions already exist in the database. Skipping load.");
                return;
            }

            log.info("Loading initial fact definitions from fact-definitions.json...");
            InputStream inputStream = new ClassPathResource("fact-definitions.json").getInputStream();
            List<FactDefinition> facts = objectMapper.readValue(inputStream, new TypeReference<List<FactDefinition>>() {});
            factDefinitionRepository.saveAll(facts);
            log.info("Successfully loaded {} fact definitions.", facts.size());
            
        } catch (Exception e) {
            log.error("Failed to load fact definitions", e);
        }
    }
}
