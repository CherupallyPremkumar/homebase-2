package com.homebase.ecom.product.media.service.impl;

import com.homebase.ecom.product.api.MediaService;
import com.homebase.ecom.product.domain.model.MediaAsset;
import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.domain.model.ProductMedia;
import com.homebase.ecom.product.domain.model.Variant;
import com.homebase.ecom.product.domain.model.VariantMedia;
import com.homebase.ecom.product.domain.port.ImageProcessingPort;
import com.homebase.ecom.product.domain.port.MediaRepository;
import com.homebase.ecom.product.domain.port.ObjectStoragePort;
import com.homebase.ecom.product.domain.port.ProductRepository;
import com.homebase.ecom.product.domain.port.VariantRepository;
import com.homebase.ecom.product.dto.MediaAssetDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class MediaServiceImpl implements MediaService {

    private static final Logger log = LoggerFactory.getLogger(MediaServiceImpl.class);

    private final MediaRepository mediaRepository;
    private final ProductRepository productRepository;
    private final VariantRepository variantRepository;
    private final ObjectStoragePort objectStoragePort;
    private final ImageProcessingPort imageProcessingPort;
    private final Map<String, Object> mediaConfig;

    public MediaServiceImpl(MediaRepository mediaRepository,
                            ProductRepository productRepository,
                            VariantRepository variantRepository,
                            ObjectStoragePort objectStoragePort,
                            ImageProcessingPort imageProcessingPort,
                            Map<String, Object> mediaConfig) {
        this.mediaRepository = mediaRepository;
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.objectStoragePort = objectStoragePort;
        this.imageProcessingPort = imageProcessingPort;
        this.mediaConfig = mediaConfig != null ? mediaConfig : Map.of();
    }

    // ─── Step 1: Presigned Upload URL ──────────────────────────────────

    @Override
    public UploadRequest requestUpload(String productId, String fileName, String mimeType) {
        validateMimeType(mimeType);

        String storageKey = "products/" + productId + "/" + UUID.randomUUID() + "/" + sanitizeFileName(fileName);
        ObjectStoragePort.PresignedUpload presigned = objectStoragePort.generatePresignedUpload(storageKey, mimeType);

        log.info("Generated presigned upload URL for product={}, key={}", productId, storageKey);
        return new UploadRequest(presigned.uploadUrl(), presigned.storageKey(), presigned.expiresInSeconds());
    }

    // ─── Step 2: Confirm Upload → Trigger Processing ───────────────────

    @Override
    public MediaAssetDto confirmUpload(String storageKey, String productId, int sortOrder, String altText) {
        // Create media asset in PENDING state
        MediaAsset asset = new MediaAsset();
        asset.setId(UUID.randomUUID().toString());
        asset.setOriginalUrl(objectStoragePort.getCdnUrl(storageKey));
        asset.setType(MediaAsset.MediaType.IMAGE);
        asset.setAltText(altText);
        asset.setProcessingStatus(MediaAsset.ProcessingStatus.PROCESSING);
        mediaRepository.save(asset);

        // Trigger image processing (resize, thumbnails, WebP)
        try {
            ImageProcessingPort.ProcessingResult result = imageProcessingPort.process(storageKey);

            asset.setThumbnailUrl(result.thumbnailUrl());
            asset.setMediumUrl(result.mediumUrl());
            asset.setZoomUrl(result.zoomUrl());
            asset.setCdnUrl(result.cdnUrl());
            asset.setWidth(result.width());
            asset.setHeight(result.height());
            asset.setFileSizeBytes(result.fileSizeBytes());
            asset.setMimeType(result.mimeType());
            asset.setProcessingStatus(MediaAsset.ProcessingStatus.COMPLETED);

            validateDimensions(result.width(), result.height());
            validateFileSize(result.fileSizeBytes());
        } catch (Exception e) {
            log.error("Image processing failed for key={}", storageKey, e);
            asset.setProcessingStatus(MediaAsset.ProcessingStatus.FAILED);
        }

        mediaRepository.save(asset);

        // Auto-associate with product gallery
        addMediaToProduct(productId, asset.getId(), sortOrder);

        log.info("Upload confirmed: asset={}, product={}, status={}", asset.getId(), productId, asset.getProcessingStatus());
        return toDto(asset);
    }

    // ─── Register by URL (migration / external CDN) ────────────────────

    @Override
    public MediaAssetDto registerMedia(MediaAssetDto dto) {
        MediaAsset asset = toModel(dto);
        if (asset.getId() == null) {
            asset.setId(UUID.randomUUID().toString());
        }
        asset.setProcessingStatus(MediaAsset.ProcessingStatus.COMPLETED);
        mediaRepository.save(asset);
        return toDto(asset);
    }

    // ─── CRUD ──────────────────────────────────────────────────────────

    @Override
    public MediaAssetDto getMedia(String id) {
        return mediaRepository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public void deleteMedia(String id) {
        mediaRepository.findById(id).ifPresent(asset -> {
            // Delete from object storage
            if (asset.getOriginalUrl() != null) {
                try {
                    objectStoragePort.delete(extractKeyFromUrl(asset.getOriginalUrl()));
                } catch (Exception e) {
                    log.warn("Failed to delete storage object for asset={}", id, e);
                }
            }
        });
        mediaRepository.delete(id);
    }

    // ─── Gallery Association ───────────────────────────────────────────

    @Override
    public void addMediaToProduct(String productId, String mediaId, int sortOrder) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("Product not found: " + productId));

        validateMaxImagesPerProduct(product.getMedia().size());

        ProductMedia pm = new ProductMedia();
        pm.setProductId(productId);
        pm.setAssetId(mediaId);
        pm.setSortOrder(sortOrder);
        pm.setPrimary(product.getMedia().isEmpty());
        product.getMedia().add(pm);
        productRepository.save(product);
    }

    @Override
    public void addMediaToVariant(String variantId, String mediaId, int sortOrder) {
        Variant variant = variantRepository.findById(variantId).orElseThrow(
                () -> new IllegalArgumentException("Variant not found: " + variantId));

        validateMaxImagesPerVariant(variant.getMedia().size());

        VariantMedia vm = new VariantMedia();
        vm.setVariantId(variantId);
        vm.setAssetId(mediaId);
        vm.setSortOrder(sortOrder);
        vm.setPrimary(variant.getMedia().isEmpty());
        variant.getMedia().add(vm);
        variantRepository.save(variant);
    }

    // ─── Gallery Retrieval (N+1 fixed — single batch query) ────────────

    @Override
    public List<MediaAssetDto> getProductGallery(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("Product not found: " + productId));

        List<String> assetIds = product.getMedia().stream()
                .sorted((a, b) -> Integer.compare(a.getSortOrder(), b.getSortOrder()))
                .map(ProductMedia::getAssetId)
                .toList();

        if (assetIds.isEmpty()) return List.of();

        // Single batch query — no N+1
        List<MediaAsset> assets = mediaRepository.findAllByIdIn(assetIds);

        // Preserve sort order from ProductMedia
        java.util.Map<String, MediaAsset> assetMap = assets.stream()
                .collect(Collectors.toMap(MediaAsset::getId, a -> a));

        return assetIds.stream()
                .map(assetMap::get)
                .filter(a -> a != null)
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<MediaAssetDto> getVariantGallery(String variantId) {
        Variant variant = variantRepository.findById(variantId).orElseThrow(
                () -> new IllegalArgumentException("Variant not found: " + variantId));

        List<String> assetIds = variant.getMedia().stream()
                .sorted((a, b) -> Integer.compare(a.getSortOrder(), b.getSortOrder()))
                .map(VariantMedia::getAssetId)
                .toList();

        if (assetIds.isEmpty()) return List.of();

        List<MediaAsset> assets = mediaRepository.findAllByIdIn(assetIds);
        java.util.Map<String, MediaAsset> assetMap = assets.stream()
                .collect(Collectors.toMap(MediaAsset::getId, a -> a));

        return assetIds.stream()
                .map(assetMap::get)
                .filter(a -> a != null)
                .map(this::toDto)
                .toList();
    }

    // ─── Primary Image ────────────────────────────────────────────────

    @Override
    public void setPrimaryImage(String productId, String mediaId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("Product not found: " + productId));

        for (ProductMedia pm : product.getMedia()) {
            pm.setPrimary(pm.getAssetId().equals(mediaId));
        }
        productRepository.save(product);
        log.info("Set primary image: product={}, media={}", productId, mediaId);
    }

    // ─── Validation ───────────────────────────────────────────────────

    private void validateMimeType(String mimeType) {
        @SuppressWarnings("unchecked")
        List<String> allowed = (List<String>) mediaConfig.getOrDefault("allowedMimeTypes",
                List.of("image/jpeg", "image/png", "image/webp", "image/gif"));
        if (!allowed.contains(mimeType)) {
            throw new IllegalArgumentException("Unsupported MIME type: " + mimeType + ". Allowed: " + allowed);
        }
    }

    private void validateFileSize(long fileSizeBytes) {
        long maxSize = ((Number) mediaConfig.getOrDefault("maxFileSizeBytes", 10485760)).longValue();
        if (fileSizeBytes > maxSize) {
            throw new IllegalArgumentException("File size " + fileSizeBytes + " exceeds maximum " + maxSize + " bytes");
        }
    }

    private void validateDimensions(int width, int height) {
        int minW = ((Number) mediaConfig.getOrDefault("minWidth", 200)).intValue();
        int minH = ((Number) mediaConfig.getOrDefault("minHeight", 200)).intValue();
        int maxW = ((Number) mediaConfig.getOrDefault("maxWidth", 8000)).intValue();
        int maxH = ((Number) mediaConfig.getOrDefault("maxHeight", 8000)).intValue();

        if (width < minW || height < minH) {
            throw new IllegalArgumentException("Image too small: " + width + "x" + height + ". Minimum: " + minW + "x" + minH);
        }
        if (width > maxW || height > maxH) {
            throw new IllegalArgumentException("Image too large: " + width + "x" + height + ". Maximum: " + maxW + "x" + maxH);
        }
    }

    private void validateMaxImagesPerProduct(int currentCount) {
        int max = ((Number) mediaConfig.getOrDefault("maxImagesPerProduct", 20)).intValue();
        if (currentCount >= max) {
            throw new IllegalArgumentException("Product already has maximum " + max + " images");
        }
    }

    private void validateMaxImagesPerVariant(int currentCount) {
        int max = ((Number) mediaConfig.getOrDefault("maxImagesPerVariant", 10)).intValue();
        if (currentCount >= max) {
            throw new IllegalArgumentException("Variant already has maximum " + max + " images");
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private String extractKeyFromUrl(String url) {
        // Extract storage key from CDN URL — adapter-specific, simplified here
        int idx = url.lastIndexOf("/products/");
        return idx >= 0 ? url.substring(idx + 1) : url;
    }

    private MediaAsset toModel(MediaAssetDto dto) {
        MediaAsset asset = new MediaAsset();
        asset.setId(dto.getId());
        asset.setOriginalUrl(dto.getOriginalUrl());
        asset.setCdnUrl(dto.getCdnUrl());
        asset.setThumbnailUrl(dto.getThumbnailUrl());
        asset.setMediumUrl(dto.getMediumUrl());
        asset.setZoomUrl(dto.getZoomUrl());
        if (dto.getType() != null) {
            asset.setType(MediaAsset.MediaType.valueOf(dto.getType().name()));
        }
        asset.setMimeType(dto.getMimeType());
        asset.setFileSizeBytes(dto.getFileSizeBytes());
        asset.setWidth(dto.getWidth());
        asset.setHeight(dto.getHeight());
        asset.setAltText(dto.getAltText());
        if (dto.getProcessingStatus() != null) {
            asset.setProcessingStatus(MediaAsset.ProcessingStatus.valueOf(dto.getProcessingStatus().name()));
        }
        return asset;
    }

    private MediaAssetDto toDto(MediaAsset asset) {
        MediaAssetDto dto = new MediaAssetDto();
        dto.setId(asset.getId());
        dto.setOriginalUrl(asset.getOriginalUrl());
        dto.setCdnUrl(asset.getCdnUrl());
        dto.setThumbnailUrl(asset.getThumbnailUrl());
        dto.setMediumUrl(asset.getMediumUrl());
        dto.setZoomUrl(asset.getZoomUrl());
        if (asset.getType() != null) {
            dto.setType(MediaAssetDto.MediaTypeDto.valueOf(asset.getType().name()));
        }
        dto.setMimeType(asset.getMimeType());
        dto.setFileSizeBytes(asset.getFileSizeBytes());
        dto.setWidth(asset.getWidth());
        dto.setHeight(asset.getHeight());
        dto.setAltText(asset.getAltText());
        if (asset.getProcessingStatus() != null) {
            dto.setProcessingStatus(MediaAssetDto.ProcessingStatusDto.valueOf(asset.getProcessingStatus().name()));
        }
        return dto;
    }
}
