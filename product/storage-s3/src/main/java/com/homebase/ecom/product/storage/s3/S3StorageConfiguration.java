package com.homebase.ecom.product.storage.s3;

import com.homebase.ecom.product.domain.port.ObjectStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3StorageConfiguration {

    @Value("${media.storage.s3.bucket-name}")
    private String bucketName;

    @Value("${media.storage.s3.region:ap-south-1}")
    private String region;

    @Value("${media.storage.s3.cdn-base-url}")
    private String cdnBaseUrl;

    @Value("${media.storage.s3.expiry-seconds:900}")
    private int expirySeconds;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(region))
                .build();
    }

    @Bean
    public ObjectStoragePort objectStoragePort(S3Client s3Client, S3Presigner s3Presigner) {
        return new S3ObjectStorageAdapter(s3Client, s3Presigner, bucketName, cdnBaseUrl, expirySeconds);
    }
}
