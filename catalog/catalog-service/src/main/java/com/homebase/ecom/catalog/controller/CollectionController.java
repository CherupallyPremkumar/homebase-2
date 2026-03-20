package com.homebase.ecom.catalog.controller;

import com.homebase.ecom.catalog.model.Collection;
import com.homebase.ecom.catalog.service.CollectionService;
import org.chenile.base.response.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Collection CRUD operations.
 * Catalog is a read model -- collections are managed here for the materialized view.
 */
@RestController
@RequestMapping("/collection")
public class CollectionController {

    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @PostMapping
    public ResponseEntity<GenericResponse<Collection>> create(@RequestBody Collection collection) {
        Collection created = collectionService.createCollection(collection);
        return ResponseEntity.ok(successResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<Collection>> retrieve(@PathVariable String id) {
        Collection collection = collectionService.getCollectionById(id)
                .orElseThrow(() -> new RuntimeException("Collection not found: " + id));
        return ResponseEntity.ok(successResponse(collection));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<Collection>> update(@PathVariable String id, @RequestBody Collection collection) {
        Collection updated = collectionService.updateCollection(id, collection);
        return ResponseEntity.ok(successResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> delete(@PathVariable String id) {
        collectionService.deleteCollection(id);
        GenericResponse<Void> response = new GenericResponse<>();
        response.setSuccess(true);
        response.setCode(200);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<GenericResponse<List<Collection>>> getActive() {
        List<Collection> active = collectionService.getActiveCollections();
        return ResponseEntity.ok(successResponse(active));
    }

    @GetMapping("/_dynamic")
    public ResponseEntity<GenericResponse<List<Collection>>> getDynamic() {
        List<Collection> dynamic = collectionService.getDynamicCollections();
        return ResponseEntity.ok(successResponse(dynamic));
    }

    private <T> GenericResponse<T> successResponse(T data) {
        GenericResponse<T> response = new GenericResponse<>();
        response.setSuccess(true);
        response.setCode(200);
        response.setData(data);
        return response;
    }
}
