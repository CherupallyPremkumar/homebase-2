package com.homebase.ecom.catalog.configuration.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.homebase.ecom.catalog.model.Collection;

import java.util.List;

@RestController
@ChenileController(value = "collectionService", serviceName = "collectionServiceImpl")
public class CollectionController extends ControllerSupport {

    @PostMapping("/collection")
    public ResponseEntity<GenericResponse<Collection>> createCollection(
            HttpServletRequest request,
            @RequestBody Collection collection) {
        return process(request, collection);
    }

    @PutMapping("/collection/{id}")
    public ResponseEntity<GenericResponse<Collection>> updateCollection(
            HttpServletRequest request,
            @PathVariable String id,
            @RequestBody Collection collection) {
        return process(request, id, collection);
    }

    @GetMapping("/collection/{id}")
    public ResponseEntity<GenericResponse<Collection>> getCollectionById(
            HttpServletRequest request,
            @PathVariable String id) {
        return process(request, id);
    }

    @GetMapping("/collection")
    public ResponseEntity<GenericResponse<List<Collection>>> getActiveCollections(
            HttpServletRequest request) {
        return process(request);
    }

    @GetMapping("/collection/_dynamic")
    public ResponseEntity<GenericResponse<List<Collection>>> getDynamicCollections(
            HttpServletRequest request) {
        return process(request);
    }

    @DeleteMapping("/collection/{id}")
    public ResponseEntity<GenericResponse<Void>> deleteCollection(
            HttpServletRequest request,
            @PathVariable String id) {
        return process(request, id);
    }
}
