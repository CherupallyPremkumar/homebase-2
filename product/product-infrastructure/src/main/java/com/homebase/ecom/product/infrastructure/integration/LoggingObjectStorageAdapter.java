package com.homebase.ecom.product.infrastructure.integration;

import com.homebase.ecom.product.domain.port.ObjectStoragePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logging adapter for object storage operations.
 * Returns mock presigned URLs and CDN URLs for development/testing.
 *
 * <p>In production, replace with an adapter backed by AWS S3, GCS, or MinIO.</p>
 */
public class LoggingObjectStorageAdapter implements ObjectStoragePort {

    private static final Logger log = LoggerFactory.getLogger(LoggingObjectStorageAdapter.class);

    private static final String MOCK_UPLOAD_PREFIX = "https://storage.homebase.com/upload/";
    private static final String CDN_PREFIX = "https://cdn.homebase.com/";
    private static final int DEFAULT_EXPIRY_SECONDS = 3600;

    @Override
    public PresignedUpload generatePresignedUpload(String key, String mimeType) {
        log.info("OBJECT_STORAGE: Generating presigned upload URL for key={}, mimeType={}", key, mimeType);

        String uploadUrl = MOCK_UPLOAD_PREFIX + key + "?mimeType=" + mimeType;

        return new PresignedUpload(uploadUrl, key, DEFAULT_EXPIRY_SECONDS);
    }

    @Override
    public String getCdnUrl(String key) {
        log.info("OBJECT_STORAGE: Resolving CDN URL for key={}", key);
        return CDN_PREFIX + key;
    }

    @Override
    public void delete(String key) {
        log.info("OBJECT_STORAGE: Deleting object key={}", key);
    }
}
