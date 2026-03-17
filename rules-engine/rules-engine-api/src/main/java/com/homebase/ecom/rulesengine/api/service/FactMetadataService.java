package com.homebase.ecom.rulesengine.api.service;

import com.homebase.ecom.rulesengine.api.dto.FactDefinitionDto;
import java.util.List;

public interface FactMetadataService {
    /**
     * Retrieves all registered fact definitions across all modules.
     */
    List<FactDefinitionDto> getAllFacts();

    /**
     * Retrieves fact definitions for a specific module (e.g. "Catalog").
     */
    List<FactDefinitionDto> getFactsByModule(String module);
}
