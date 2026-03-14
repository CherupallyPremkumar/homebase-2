package com.homebase.ecom.product.service.impl;

import com.homebase.ecom.product.api.TaxonomyService;
import com.homebase.ecom.product.domain.model.AttributeDefinition;
import com.homebase.ecom.product.domain.model.Category;
import com.homebase.ecom.product.domain.port.AttributeRepository;
import com.homebase.ecom.product.domain.port.CategoryRepository;
import com.homebase.ecom.product.dto.AttributeDefinitionDto;
import com.homebase.ecom.product.dto.CategoryDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaxonomyServiceImpl implements TaxonomyService {

    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;

    public TaxonomyServiceImpl(CategoryRepository categoryRepository, AttributeRepository attributeRepository) {
        this.categoryRepository = categoryRepository;
        this.attributeRepository = attributeRepository;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = toCategory(categoryDto);
        Category saved = categoryRepository.save(category);
        return toCategoryDto(saved);
    }

    @Override
    public CategoryDto updateCategory(String id, CategoryDto categoryDto) {
        Category category = toCategory(categoryDto);
        category.setId(id);
        Category saved = categoryRepository.save(category);
        return toCategoryDto(saved);
    }

    @Override
    public CategoryDto getCategory(String id) {
        return categoryRepository.findById(id).map(this::toCategoryDto).orElse(null);
    }

    @Override
    public List<CategoryDto> getSubCategories(String parentId) {
        return categoryRepository.findByParentId(parentId).stream()
                .map(this::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    public List<CategoryDto> getCategoryTree() {
        // Simple tree fetch: find all roots and recurse (or just return flat list for now)
        return categoryRepository.findAll().stream()
                .map(this::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public AttributeDefinitionDto createAttribute(AttributeDefinitionDto attributeDto) {
        AttributeDefinition attribute = toAttribute(attributeDto);
        AttributeDefinition saved = attributeRepository.save(attribute);
        return toAttributeDto(saved);
    }

    @Override
    public AttributeDefinitionDto updateAttribute(String id, AttributeDefinitionDto attributeDto) {
        AttributeDefinition attribute = toAttribute(attributeDto);
        attribute.setId(id);
        AttributeDefinition saved = attributeRepository.save(attribute);
        return toAttributeDto(saved);
    }

    @Override
    public AttributeDefinitionDto getAttribute(String id) {
        return attributeRepository.findById(id).map(this::toAttributeDto).orElse(null);
    }

    @Override
    public List<AttributeDefinitionDto> getAttributesByCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null || category.getAttributeIds().isEmpty()) {
            return new ArrayList<>();
        }
        return attributeRepository.findAllById(category.getAttributeIds()).stream()
                .map(this::toAttributeDto).collect(Collectors.toList());
    }

    @Override
    public void deleteAttribute(String id) {
        attributeRepository.deleteById(id);
    }

    // Basic Mappers (could be moved to a dedicated Mapper class)
    private Category toCategory(CategoryDto dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setSlug(dto.getSlug());
        category.setParentId(dto.getParentId());
        category.setPath(dto.getPath());
        category.setDepth(dto.getDepth());
        category.setDisplayOrder(dto.getDisplayOrder());
        category.setActive(dto.isActive());
        category.setAttributeIds(dto.getAttributeIds());
        return category;
    }

    private CategoryDto toCategoryDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        dto.setParentId(category.getParentId());
        dto.setPath(category.getPath());
        dto.setDepth(category.getDepth());
        dto.setDisplayOrder(category.getDisplayOrder());
        dto.setActive(category.isActive());
        dto.setAttributeIds(category.getAttributeIds());
        return dto;
    }

    private AttributeDefinition toAttribute(AttributeDefinitionDto dto) {
        AttributeDefinition attribute = new AttributeDefinition();
        attribute.setId(dto.getId());
        attribute.setCode(dto.getCode());
        attribute.setLabel(dto.getLabel());
        if (dto.getInputType() != null) {
            attribute.setInputType(AttributeDefinition.InputType.valueOf(dto.getInputType().name()));
        }
        attribute.setFilterable(dto.isFilterable());
        attribute.setSearchable(dto.isSearchable());
        attribute.setRequired(dto.isRequired());
        attribute.setDisplayOrder(dto.getDisplayOrder());
        return attribute;
    }

    private AttributeDefinitionDto toAttributeDto(AttributeDefinition attribute) {
        AttributeDefinitionDto dto = new AttributeDefinitionDto();
        dto.setId(attribute.getId());
        dto.setCode(attribute.getCode());
        dto.setLabel(attribute.getLabel());
        if (attribute.getInputType() != null) {
            dto.setInputType(AttributeDefinitionDto.InputTypeDto.valueOf(attribute.getInputType().name()));
        }
        dto.setFilterable(attribute.isFilterable());
        dto.setSearchable(attribute.isSearchable());
        dto.setRequired(attribute.isRequired());
        dto.setDisplayOrder(attribute.getDisplayOrder());
        return dto;
    }
}
