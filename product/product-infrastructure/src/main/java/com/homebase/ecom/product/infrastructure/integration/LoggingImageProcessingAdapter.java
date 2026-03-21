package com.homebase.ecom.product.infrastructure.integration;

import com.homebase.ecom.product.domain.port.ImageProcessingPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logging adapter for image processing operations.
 * Returns the source key as-is for all variant URLs (no actual processing).
 *
 * <p>In production, replace with an adapter backed by AWS Lambda, imgproxy, or Cloudinary.</p>
 */
public class LoggingImageProcessingAdapter implements ImageProcessingPort {

    private static final Logger log = LoggerFactory.getLogger(LoggingImageProcessingAdapter.class);

    private static final String CDN_PREFIX = "https://cdn.homebase.com/";

    @Override
    public ProcessingResult process(String sourceKey) {
        log.info("IMAGE_PROCESSING: Processing image sourceKey={}", sourceKey);

        String cdnUrl = CDN_PREFIX + sourceKey;

        log.info("IMAGE_PROCESSING: Generated variants for sourceKey={} — thumbnail, medium, zoom, cdn", sourceKey);

        return new ProcessingResult(
                cdnUrl + "?w=150&h=150",   // thumbnail
                cdnUrl + "?w=600&h=600",   // medium
                cdnUrl + "?w=1200&h=1200", // zoom
                cdnUrl,                     // original on CDN
                1200,                       // default width
                1200,                       // default height
                0L,                         // unknown file size
                "image/jpeg"                // default mime type
        );
    }
}
