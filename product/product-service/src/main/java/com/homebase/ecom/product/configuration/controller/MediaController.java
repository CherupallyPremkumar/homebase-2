package com.homebase.ecom.product.configuration.controller;

import com.homebase.ecom.product.api.MediaService;
import com.homebase.ecom.product.dto.MediaAssetDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product/media")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping
    public ResponseEntity<MediaAssetDto> registerMedia(@RequestBody MediaAssetDto mediaAssetDto) {
        return ResponseEntity.ok(mediaService.registerMedia(mediaAssetDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaAssetDto> getMedia(@PathVariable String id) {
        return ResponseEntity.ok(mediaService.getMedia(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable String id) {
        mediaService.deleteMedia(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/products/{productId}/gallery/{mediaId}")
    public ResponseEntity<Void> addMediaToProduct(
            @PathVariable String productId,
            @PathVariable String mediaId,
            @RequestParam(defaultValue = "0") int sortOrder) {
        mediaService.addMediaToProduct(productId, mediaId, sortOrder);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/variants/{variantId}/gallery/{mediaId}")
    public ResponseEntity<Void> addMediaToVariant(
            @PathVariable String variantId,
            @PathVariable String mediaId,
            @RequestParam(defaultValue = "0") int sortOrder) {
        mediaService.addMediaToVariant(variantId, mediaId, sortOrder);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/products/{productId}/gallery")
    public ResponseEntity<List<MediaAssetDto>> getProductGallery(@PathVariable String productId) {
        return ResponseEntity.ok(mediaService.getProductGallery(productId));
    }

    @GetMapping("/variants/{variantId}/gallery")
    public ResponseEntity<List<MediaAssetDto>> getVariantGallery(@PathVariable String variantId) {
        return ResponseEntity.ok(mediaService.getVariantGallery(variantId));
    }
}
