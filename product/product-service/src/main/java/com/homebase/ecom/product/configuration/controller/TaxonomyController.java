package com.homebase.ecom.product.configuration.controller;

import com.homebase.ecom.product.api.TaxonomyService;
import com.homebase.ecom.product.dto.AttributeDefinitionDto;
import com.homebase.ecom.product.dto.CategoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product/taxonomy")
public class TaxonomyController {

    private final TaxonomyService taxonomyService;

    public TaxonomyController(TaxonomyService taxonomyService) {
        this.taxonomyService = taxonomyService;
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(taxonomyService.createCategory(categoryDto));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable String id, @RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(taxonomyService.updateCategory(id, categoryDto));
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable String id) {
        return ResponseEntity.ok(taxonomyService.getCategory(id));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategoryTree() {
        return ResponseEntity.ok(taxonomyService.getCategoryTree());
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        taxonomyService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/attributes")
    public ResponseEntity<AttributeDefinitionDto> createAttribute(@RequestBody AttributeDefinitionDto attributeDto) {
        return ResponseEntity.ok(taxonomyService.createAttribute(attributeDto));
    }

    @GetMapping("/attributes/{id}")
    public ResponseEntity<AttributeDefinitionDto> getAttribute(@PathVariable String id) {
        return ResponseEntity.ok(taxonomyService.getAttribute(id));
    }

    @GetMapping("/categories/{categoryId}/attributes")
    public ResponseEntity<List<AttributeDefinitionDto>> getAttributesByCategory(@PathVariable String categoryId) {
        return ResponseEntity.ok(taxonomyService.getAttributesByCategory(categoryId));
    }
}
