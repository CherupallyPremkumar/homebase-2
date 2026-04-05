package com.homebase.ecom.product.media.configuration;

import com.homebase.ecom.product.api.MediaService;
import com.homebase.ecom.product.domain.port.ImageProcessingPort;
import com.homebase.ecom.product.domain.port.MediaRepository;
import com.homebase.ecom.product.domain.port.ObjectStoragePort;
import com.homebase.ecom.product.domain.port.ProductRepository;
import com.homebase.ecom.product.domain.port.VariantRepository;
import com.homebase.ecom.product.media.service.impl.MediaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class MediaConfiguration {

    @Bean
    public MediaService mediaService(
            MediaRepository mediaRepository,
            ProductRepository productRepository,
            VariantRepository variantRepository,
            ObjectStoragePort objectStoragePort,
            ImageProcessingPort imageProcessingPort,
            @Autowired(required = false) Map<String, Object> mediaConfig) {
        return new MediaServiceImpl(
                mediaRepository,
                productRepository,
                variantRepository,
                objectStoragePort,
                imageProcessingPort,
                mediaConfig
        );
    }
}
