package com.homebase.ecom.product.storage.s3;

import com.homebase.ecom.product.domain.port.ObjectStoragePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;

/**
 * AWS S3 implementation of ObjectStoragePort.
 * Uses S3Presigner for presigned upload URLs and CloudFront for CDN delivery.
 */
public class S3ObjectStorageAdapter implements ObjectStoragePort {

    private static final Logger log = LoggerFactory.getLogger(S3ObjectStorageAdapter.class);

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucketName;
    private final String cdnBaseUrl;
    private final int expirySeconds;

    public S3ObjectStorageAdapter(S3Client s3Client, S3Presigner s3Presigner,
                                  String bucketName, String cdnBaseUrl, int expirySeconds) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
        this.cdnBaseUrl = cdnBaseUrl;
        this.expirySeconds = expirySeconds;
    }

    @Override
    public PresignedUpload generatePresignedUpload(String key, String mimeType) {
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(mimeType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(expirySeconds))
                .putObjectRequest(putRequest)
                .build();

        PresignedPutObjectRequest presigned = s3Presigner.presignPutObject(presignRequest);

        log.info("S3: generated presigned upload URL for bucket={}, key={}", bucketName, key);
        return new PresignedUpload(presigned.url().toString(), key, expirySeconds);
    }

    @Override
    public String getCdnUrl(String key) {
        return cdnBaseUrl + "/" + key;
    }

    @Override
    public void delete(String key) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
        log.info("S3: deleted object bucket={}, key={}", bucketName, key);
    }
}
