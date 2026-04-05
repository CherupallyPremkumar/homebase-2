package com.homebase.ecom.catalog.controller;

import com.homebase.ecom.catalog.model.Category;
import com.homebase.ecom.catalog.service.CategoryService;
import org.chenile.base.response.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Category CRUD operations.
 * Catalog is a read model -- categories are managed here for the materialized view.
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<GenericResponse<Category>> create(@RequestBody Category category) {
        Category created = categoryService.createCategory(category);
        return ResponseEntity.ok(successResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<Category>> retrieve(@PathVariable String id) {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Category not found: " + id));
        return ResponseEntity.ok(successResponse(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<Category>> update(@PathVariable String id, @RequestBody Category category) {
        Category updated = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(successResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> delete(@PathVariable String id) {
        categoryService.deleteCategory(id);
        GenericResponse<Void> response = new GenericResponse<>();
        response.setSuccess(true);
        response.setCode(200);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/_roots")
    public ResponseEntity<GenericResponse<List<Category>>> getRoots() {
        List<Category> roots = categoryService.getRootCategories();
        return ResponseEntity.ok(successResponse(roots));
    }

    @GetMapping("/_tree")
    public ResponseEntity<GenericResponse<List<Category>>> getTree() {
        List<Category> tree = categoryService.getCategoryTree();
        return ResponseEntity.ok(successResponse(tree));
    }

    @GetMapping("/{id}/_children")
    public ResponseEntity<GenericResponse<List<Category>>> getChildren(@PathVariable String id) {
        List<Category> children = categoryService.getChildCategories(id);
        return ResponseEntity.ok(successResponse(children));
    }

    private <T> GenericResponse<T> successResponse(T data) {
        GenericResponse<T> response = new GenericResponse<>();
        response.setSuccess(true);
        response.setCode(200);
        response.setData(data);
        return response;
    }
}
