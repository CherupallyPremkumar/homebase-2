package com.homebase.ecom.product.domain.port;

/**
 * Outbound port for image processing (resize, convert, generate thumbnails).
 * Implementations can be local (ImageIO), or external (AWS Lambda, imgproxy, Cloudinary).
 */
public interface ImageProcessingPort {

    /**
     * Processes an uploaded image: generates thumbnail, medium, and zoom variants,
     * converts to WebP if needed, and stores all variants in object storage.
     *
     * @param sourceKey the storage key of the original uploaded image
     * @return result with URLs for all generated variants
     */
    ProcessingResult process(String sourceKey);

    record ProcessingResult(
            String thumbnailUrl,   // ~150x150
            String mediumUrl,      // ~600x600
            String zoomUrl,        // ~1200x1200
            String cdnUrl,         // original on CDN
            int width,
            int height,
            long fileSizeBytes,
            String mimeType
    ) {}
}
