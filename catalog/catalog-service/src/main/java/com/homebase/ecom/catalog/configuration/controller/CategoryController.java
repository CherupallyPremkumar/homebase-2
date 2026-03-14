package com.homebase.ecom.catalog.configuration.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.homebase.ecom.catalog.model.Category;

import java.util.List;

@RestController
@ChenileController(value = "categoryService", serviceName = "categoryServiceImpl")
public class CategoryController extends ControllerSupport {

    @PostMapping("/category")
    public ResponseEntity<GenericResponse<Category>> createCategory(
            HttpServletRequest request,
            @RequestBody Category category) {
        return process(request, category);
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<GenericResponse<Category>> updateCategory(
            HttpServletRequest request,
            @PathVariable String id,
            @RequestBody Category category) {
        return process(request, id, category);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<GenericResponse<Category>> getCategoryById(
            HttpServletRequest request,
            @PathVariable String id) {
        return process(request, id);
    }

    @GetMapping("/category/_roots")
    public ResponseEntity<GenericResponse<List<Category>>> getRootCategories(
            HttpServletRequest request) {
        return process(request);
    }

    @GetMapping("/category/_tree")
    public ResponseEntity<GenericResponse<List<Category>>> getCategoryTree(
            HttpServletRequest request) {
        return process(request);
    }

    @GetMapping("/category/{id}/_children")
    public ResponseEntity<GenericResponse<List<Category>>> getChildCategories(
            HttpServletRequest request,
            @PathVariable String id) {
        return process(request, id);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<GenericResponse<Void>> deleteCategory(
            HttpServletRequest request,
            @PathVariable String id) {
        return process(request, id);
    }
}
