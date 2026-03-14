package com.homebase.ecom.product.api;

import com.homebase.ecom.product.dto.MediaAssetDto;
import java.util.List;

/**
 * Service interface for managing PIM Media Assets (Images, Videos, Documents).
 */
public interface MediaService {

    /**
     * Registers a new media asset.
     */
    MediaAssetDto registerMedia(MediaAssetDto mediaAssetDto);

    /**
     * Retrieves a media asset by ID.
     */
    MediaAssetDto getMedia(String id);

    /**
     * Deletes a media asset.
     */
    void deleteMedia(String id);

    /**
     * Associates media with a product gallery.
     */
    void addMediaToProduct(String productId, String mediaId, int sortOrder);

    /**
     * Associates media with a specific variant.
     */
    void addMediaToVariant(String variantId, String mediaId, int sortOrder);

    /**
     * Retrieves all media for a product.
     */
    List<MediaAssetDto> getProductGallery(String productId);

    /**
     * Retrieves all media for a variant.
     */
    List<MediaAssetDto> getVariantGallery(String variantId);
}
