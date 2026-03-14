package com.homebase.ecom.product.service.impl;

import com.homebase.ecom.product.api.MediaService;
import com.homebase.ecom.product.domain.model.MediaAsset;
import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.domain.model.ProductMedia;
import com.homebase.ecom.product.domain.model.Variant;
import com.homebase.ecom.product.domain.model.VariantMedia;
import com.homebase.ecom.product.domain.port.MediaRepository;
import com.homebase.ecom.product.domain.port.ProductRepository;
import com.homebase.ecom.product.domain.port.VariantRepository;
import com.homebase.ecom.product.dto.MediaAssetDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final ProductRepository productRepository;
    private final VariantRepository variantRepository;

    public MediaServiceImpl(MediaRepository mediaRepository, ProductRepository productRepository, VariantRepository variantRepository) {
        this.mediaRepository = mediaRepository;
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
    }

    @Override
    public MediaAssetDto registerMedia(MediaAssetDto dto) {
        MediaAsset asset = toModel(dto);
        mediaRepository.save(asset);
        return toDto(asset);
    }

    @Override
    public MediaAssetDto getMedia(String id) {
        return mediaRepository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public void deleteMedia(String id) {
        mediaRepository.delete(id);
    }

    @Override
    public void addMediaToProduct(String productId, String mediaId, int sortOrder) {
        Product product = productRepository.findById(productId).orElseThrow();
        ProductMedia pm = new ProductMedia();
        pm.setProductId(productId);
        pm.setAssetId(mediaId);
        pm.setSortOrder(sortOrder);
        pm.setPrimary(product.getMedia().isEmpty());
        product.getMedia().add(pm);
        productRepository.save(product);
    }

    @Override
    public void addMediaToVariant(String variantId, String mediaId, int sortOrder) {
        Variant variant = variantRepository.findById(variantId).orElseThrow();
        VariantMedia vm = new VariantMedia();
        vm.setVariantId(variantId);
        vm.setAssetId(mediaId);
        vm.setSortOrder(sortOrder);
        vm.setPrimary(variant.getMedia().isEmpty());
        variant.getMedia().add(vm);
        variantRepository.save(variant);
    }

    @Override
    public List<MediaAssetDto> getProductGallery(String productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        List<String> assetIds = product.getMedia().stream()
                .map(ProductMedia::getAssetId)
                .collect(Collectors.toList());
        return assetIds.stream()
                .map(id -> mediaRepository.findById(id).orElse(null))
                .filter(a -> a != null)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MediaAssetDto> getVariantGallery(String variantId) {
        Variant variant = variantRepository.findById(variantId).orElseThrow();
        List<String> assetIds = variant.getMedia().stream()
                .map(VariantMedia::getAssetId)
                .collect(Collectors.toList());
        return assetIds.stream()
                .map(id -> mediaRepository.findById(id).orElse(null))
                .filter(a -> a != null)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private MediaAsset toModel(MediaAssetDto dto) {
        MediaAsset asset = new MediaAsset();
        asset.setId(dto.getId());
        asset.setOriginalUrl(dto.getOriginalUrl());
        asset.setCdnUrl(dto.getCdnUrl());
        if (dto.getType() != null) {
            asset.setType(MediaAsset.MediaType.valueOf(dto.getType().name()));
        }
        asset.setMimeType(dto.getMimeType());
        asset.setFileSizeBytes(dto.getFileSizeBytes());
        asset.setWidth(dto.getWidth());
        asset.setHeight(dto.getHeight());
        asset.setAltText(dto.getAltText());
        return asset;
    }

    private MediaAssetDto toDto(MediaAsset asset) {
        MediaAssetDto dto = new MediaAssetDto();
        dto.setId(asset.getId());
        dto.setOriginalUrl(asset.getOriginalUrl());
        dto.setCdnUrl(asset.getCdnUrl());
        if (asset.getType() != null) {
            dto.setType(MediaAssetDto.MediaTypeDto.valueOf(asset.getType().name()));
        }
        dto.setMimeType(asset.getMimeType());
        dto.setFileSizeBytes(asset.getFileSizeBytes());
        dto.setWidth(asset.getWidth());
        dto.setHeight(asset.getHeight());
        dto.setAltText(asset.getAltText());
        return dto;
    }
}
