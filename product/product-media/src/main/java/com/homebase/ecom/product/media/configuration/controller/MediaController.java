package com.homebase.ecom.product.media.configuration.controller;

import com.homebase.ecom.product.api.MediaService;
import com.homebase.ecom.product.api.MediaService.UploadRequest;
import com.homebase.ecom.product.dto.MediaAssetDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/product/media")
@Tag(name = "Product - Media", description = "Product media asset management")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    // ─── Presigned Upload Flow ─────────────────────────────────────────

    @PostMapping("/upload-url")
    public ResponseEntity<UploadRequest> requestUpload(
            @RequestParam String productId,
            @RequestParam String fileName,
            @RequestParam String mimeType) {
        return ResponseEntity.ok(mediaService.requestUpload(productId, fileName, mimeType));
    }

    @PostMapping("/confirm")
    public ResponseEntity<MediaAssetDto> confirmUpload(
            @RequestParam String storageKey,
            @RequestParam String productId,
            @RequestParam(defaultValue = "0") int sortOrder,
            @RequestParam(required = false) String altText) {
        return ResponseEntity.ok(mediaService.confirmUpload(storageKey, productId, sortOrder, altText));
    }

    // ─── Register by URL (migration / external CDN) ────────────────────

    @PostMapping
    public ResponseEntity<MediaAssetDto> registerMedia(@RequestBody MediaAssetDto mediaAssetDto) {
        return ResponseEntity.ok(mediaService.registerMedia(mediaAssetDto));
    }

    // ─── CRUD ──────────────────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<MediaAssetDto> getMedia(@PathVariable String id) {
        return ResponseEntity.ok(mediaService.getMedia(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable String id) {
        mediaService.deleteMedia(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Product Gallery ───────────────────────────────────────────────

    @PostMapping("/products/{productId}/gallery/{mediaId}")
    public ResponseEntity<Void> addMediaToProduct(
            @PathVariable String productId,
            @PathVariable String mediaId,
            @RequestParam(defaultValue = "0") int sortOrder) {
        mediaService.addMediaToProduct(productId, mediaId, sortOrder);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/products/{productId}/gallery")
    public ResponseEntity<List<MediaAssetDto>> getProductGallery(@PathVariable String productId) {
        return ResponseEntity.ok(mediaService.getProductGallery(productId));
    }

    @PutMapping("/products/{productId}/primary/{mediaId}")
    public ResponseEntity<Void> setPrimaryImage(
            @PathVariable String productId,
            @PathVariable String mediaId) {
        mediaService.setPrimaryImage(productId, mediaId);
        return ResponseEntity.ok().build();
    }

    // ─── Variant Gallery ───────────────────────────────────────────────

    @PostMapping("/variants/{variantId}/gallery/{mediaId}")
    public ResponseEntity<Void> addMediaToVariant(
            @PathVariable String variantId,
            @PathVariable String mediaId,
            @RequestParam(defaultValue = "0") int sortOrder) {
        mediaService.addMediaToVariant(variantId, mediaId, sortOrder);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/variants/{variantId}/gallery")
    public ResponseEntity<List<MediaAssetDto>> getVariantGallery(@PathVariable String variantId) {
        return ResponseEntity.ok(mediaService.getVariantGallery(variantId));
    }
}
