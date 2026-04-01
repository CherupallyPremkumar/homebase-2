package com.homebase.ecom.product.storage.local;

import com.homebase.ecom.product.domain.port.ObjectStoragePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Local filesystem implementation of ObjectStoragePort.
 * Stores files on disk and serves via local HTTP.
 * For development and testing only — NOT for production.
 */
public class LocalObjectStorageAdapter implements ObjectStoragePort {

    private static final Logger log = LoggerFactory.getLogger(LocalObjectStorageAdapter.class);

    private final Path basePath;
    private final String baseUrl;
    private final int expirySeconds;

    public LocalObjectStorageAdapter(Path basePath, String baseUrl, int expirySeconds) {
        this.basePath = basePath;
        this.baseUrl = baseUrl;
        this.expirySeconds = expirySeconds;
        ensureDirectoryExists(basePath);
    }

    @Override
    public PresignedUpload generatePresignedUpload(String key, String mimeType) {
        // In local mode, the "presigned URL" is just the local upload endpoint.
        // Frontend uploads to our own controller which writes to disk.
        Path filePath = basePath.resolve(key);
        ensureDirectoryExists(filePath.getParent());

        String uploadUrl = baseUrl + "/product/media/local-upload?key=" + key;
        log.info("Local storage: generated upload URL for key={}", key);
        return new PresignedUpload(uploadUrl, key, expirySeconds);
    }

    @Override
    public String getCdnUrl(String key) {
        return baseUrl + "/product/media/local-files/" + key;
    }

    @Override
    public void delete(String key) {
        try {
            Path filePath = basePath.resolve(key);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("Local storage: deleted key={}", key);
            }
        } catch (IOException e) {
            log.warn("Local storage: failed to delete key={}", key, e);
        }
    }

    public Path getBasePath() {
        return basePath;
    }

    private void ensureDirectoryExists(Path dir) {
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create storage directory: " + dir, e);
        }
    }
}
