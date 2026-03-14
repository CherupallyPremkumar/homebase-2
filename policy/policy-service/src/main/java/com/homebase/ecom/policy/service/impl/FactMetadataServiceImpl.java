package com.homebase.ecom.policy.service.impl;

import com.homebase.ecom.policy.api.dto.FactDefinitionDto;
import com.homebase.ecom.policy.api.service.FactMetadataService;
import com.homebase.ecom.policy.domain.model.FactDefinition;
import com.homebase.ecom.policy.domain.repository.FactDefinitionRepository;
import java.util.List;
import java.util.stream.Collectors;

public class FactMetadataServiceImpl implements FactMetadataService {

    private final FactDefinitionRepository repository;

    public FactMetadataServiceImpl(FactDefinitionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<FactDefinitionDto> getAllFacts() {
        return repository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FactDefinitionDto> getFactsByModule(String module) {
        return repository.findByModule(module).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private FactDefinitionDto toDto(FactDefinition model) {
        FactDefinitionDto dto = new FactDefinitionDto();
        dto.setId(model.getId());
        dto.setModule(model.getModule());
        dto.setEntityName(model.getEntityName());
        dto.setDisplayName(model.getDisplayName());
        dto.setAttribute(model.getAttribute());
        dto.setDataType(model.getDataType());
        return dto;
    }
}
