package com.homebase.ecom.cms.media.service.impl;

import com.homebase.ecom.cms.dto.request.CreateMediaRequest;
import com.homebase.ecom.cms.dto.response.MediaResult;
import com.homebase.ecom.cms.model.CmsMedia;
import com.homebase.ecom.cms.model.port.CmsMediaRepository;
import com.homebase.ecom.cms.service.CmsMediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Service implementation for CmsMedia CRUD operations.
 * CmsMediaController routes to this via ControllerSupport / Chenile interceptor chain.
 */
public class CmsMediaServiceImpl implements CmsMediaService {

    private static final Logger log = LoggerFactory.getLogger(CmsMediaServiceImpl.class);

    private final CmsMediaRepository cmsMediaRepository;

    public CmsMediaServiceImpl(CmsMediaRepository cmsMediaRepository) {
        this.cmsMediaRepository = cmsMediaRepository;
    }

    @Override
    public MediaResult create(CreateMediaRequest request) {
        CmsMedia model = fromRequest(request);
        CmsMedia saved = cmsMediaRepository.save(model);
        log.info("Created CMS media id={} originalName={}",
                saved.getId(), saved.getOriginalName());
        return toResult(saved);
    }

    @Override
    public MediaResult retrieve(String id) {
        CmsMedia model = cmsMediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CMS media not found: " + id));
        return toResult(model);
    }

    @Override
    public void delete(String id) {
        cmsMediaRepository.delete(id);
        log.info("Deleted CMS media id={}", id);
    }

    private MediaResult toResult(CmsMedia model) {
        MediaResult result = new MediaResult();
        result.setId(model.getId());
        result.setFileKey(model.getFileKey());
        result.setOriginalName(model.getOriginalName());
        result.setCdnUrl(model.getCdnUrl());
        result.setMimeType(model.getMimeType());
        result.setFileSizeBytes(model.getFileSizeBytes() != null ? model.getFileSizeBytes() : 0L);
        result.setWidth(model.getWidth() != null ? model.getWidth() : 0);
        result.setHeight(model.getHeight() != null ? model.getHeight() : 0);
        result.setAltText(model.getAltText());
        result.setFolder(model.getFolder());
        return result;
    }

    private CmsMedia fromRequest(CreateMediaRequest request) {
        CmsMedia model = new CmsMedia();
        model.setId(request.getId());
        model.setFileKey(request.getFileKey());
        model.setOriginalName(request.getOriginalName());
        model.setCdnUrl(request.getCdnUrl());
        model.setMimeType(request.getMimeType());
        model.setFileSizeBytes(request.getFileSizeBytes());
        model.setWidth(request.getWidth());
        model.setHeight(request.getHeight());
        model.setAltText(request.getAltText());
        model.setFolder(request.getFolder());
        return model;
    }
}
