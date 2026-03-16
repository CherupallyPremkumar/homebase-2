package com.homebase.ecom.offer.service.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.homebase.ecom.offer.api.dto.OfferDTO;
import com.homebase.ecom.offer.api.dto.OfferSearchRequest;
import org.chenile.query.model.SearchResponse;

@RestController
@ChenileController(value = "offerService", serviceName = "offerService")
@Tag(name = "Offer", description = "Product offer management")
public class OfferController extends ControllerSupport {

    @PostMapping("/offer")
    public ResponseEntity<GenericResponse<OfferDTO>> createOffer(
            HttpServletRequest request,
            @RequestBody OfferDTO offerDTO) {
        return process(request, offerDTO);
    }

    @GetMapping("/offer/{id}")
    public ResponseEntity<GenericResponse<OfferDTO>> getOffer(
            HttpServletRequest request,
            @PathVariable String id) {
        return process(request, id);
    }

    @PutMapping("/offer/{id}")
    public ResponseEntity<GenericResponse<OfferDTO>> updateOffer(
            HttpServletRequest request,
            @PathVariable String id,
            @RequestBody OfferDTO offerDTO) {
        return process(request, id, offerDTO);
    }

    @DeleteMapping("/offer/{id}")
    public ResponseEntity<GenericResponse<Void>> deleteOffer(
            HttpServletRequest request,
            @PathVariable String id) {
        return process(request, id);
    }

    @PostMapping("/offer/{id}/_approve")
    public ResponseEntity<GenericResponse<OfferDTO>> approveOffer(
            HttpServletRequest request,
            @PathVariable String id) {
        return process(request, id);
    }

    @PostMapping("/offer/{id}/_reject")
    public ResponseEntity<GenericResponse<OfferDTO>> rejectOffer(
            HttpServletRequest request,
            @PathVariable String id,
            @RequestBody(required = false) String reason) {
        return process(request, id, reason);
    }

    @PostMapping("/offer/_search")
    public ResponseEntity<GenericResponse<SearchResponse>> searchOffers(
            HttpServletRequest request,
            @RequestBody OfferSearchRequest searchRequest) {
        return process(request, searchRequest);
    }
}
