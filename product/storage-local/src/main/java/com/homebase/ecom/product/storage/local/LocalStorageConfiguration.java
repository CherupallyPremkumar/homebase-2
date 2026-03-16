package com.homebase.ecom.product.storage.local;

import com.homebase.ecom.product.domain.port.ImageProcessingPort;
import com.homebase.ecom.product.domain.port.ObjectStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Configuration
public class LocalStorageConfiguration {

    @Value("${media.storage.local.base-path:${java.io.tmpdir}/homebase-media}")
    private String basePath;

    @Value("${media.storage.local.base-url:http://localhost:8080}")
    private String baseUrl;

    @Value("${media.storage.local.expiry-seconds:900}")
    private int expirySeconds;

    @Bean
    public ObjectStoragePort objectStoragePort() {
        return new LocalObjectStorageAdapter(Path.of(basePath), baseUrl, expirySeconds);
    }

    @Bean
    public ImageProcessingPort imageProcessingPort() {
        return new LocalImageProcessingAdapter(Path.of(basePath), baseUrl);
    }
}
