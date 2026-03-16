package com.homebase.ecom.product.domain.port;

/**
 * Outbound port for object storage (S3, GCS, MinIO, etc.).
 * Generates presigned URLs for direct client uploads and serves CDN URLs.
 */
public interface ObjectStoragePort {

    /**
     * Generates a presigned upload URL for the client to upload directly to storage.
     *
     * @param key       the storage key (e.g., "products/{productId}/{filename}")
     * @param mimeType  the expected MIME type (e.g., "image/jpeg")
     * @return a presigned URL valid for a limited time
     */
    PresignedUpload generatePresignedUpload(String key, String mimeType);

    /**
     * Returns the public CDN URL for a stored object.
     */
    String getCdnUrl(String key);

    /**
     * Deletes an object from storage.
     */
    void delete(String key);

    record PresignedUpload(String uploadUrl, String storageKey, int expiresInSeconds) {}
}
