package com.homebase.ecom.product.api;

import com.homebase.ecom.product.dto.MediaAssetDto;
import java.util.List;

/**
 * Service interface for managing PIM Media Assets (Images, Videos, Documents).
 */
public interface MediaService {

    /**
     * Step 1: Request a presigned upload URL. Client uploads directly to object storage.
     *
     * @param productId the product this media will belong to
     * @param fileName  original file name (used for key generation)
     * @param mimeType  e.g., "image/jpeg"
     * @return upload instructions with presigned URL
     */
    UploadRequest requestUpload(String productId, String fileName, String mimeType);

    /**
     * Step 2: Confirm upload complete. Triggers image processing pipeline.
     * Called by client after successful upload to presigned URL.
     *
     * @param storageKey the storage key returned from requestUpload
     * @param productId  the product to associate with
     * @param sortOrder  display order in gallery
     * @param altText    accessibility text
     * @return the registered media asset (processing may still be in progress)
     */
    MediaAssetDto confirmUpload(String storageKey, String productId, int sortOrder, String altText);

    /**
     * Registers a media asset by URL (for migration or external CDN).
     */
    MediaAssetDto registerMedia(MediaAssetDto mediaAssetDto);

    /**
     * Retrieves a media asset by ID.
     */
    MediaAssetDto getMedia(String id);

    /**
     * Deletes a media asset and its storage objects.
     */
    void deleteMedia(String id);

    /**
     * Associates existing media with a product gallery.
     */
    void addMediaToProduct(String productId, String mediaId, int sortOrder);

    /**
     * Associates existing media with a variant gallery.
     */
    void addMediaToVariant(String variantId, String mediaId, int sortOrder);

    /**
     * Retrieves all media for a product (single query, no N+1).
     */
    List<MediaAssetDto> getProductGallery(String productId);

    /**
     * Retrieves all media for a variant.
     */
    List<MediaAssetDto> getVariantGallery(String variantId);

    /**
     * Sets the primary image for a product.
     */
    void setPrimaryImage(String productId, String mediaId);

    record UploadRequest(String uploadUrl, String storageKey, int expiresInSeconds) {}
}
