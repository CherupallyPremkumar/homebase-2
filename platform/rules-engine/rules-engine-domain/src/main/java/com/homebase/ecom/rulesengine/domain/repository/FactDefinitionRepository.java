package com.homebase.ecom.rulesengine.domain.repository;

import com.homebase.ecom.rulesengine.domain.model.FactDefinition;
import java.util.List;

public interface FactDefinitionRepository {
    List<FactDefinition> findAll();
    List<FactDefinition> findByModule(String module);
    void saveAll(List<FactDefinition> factDefinitions);
}
