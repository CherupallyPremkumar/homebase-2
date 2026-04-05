package com.homebase.ecom.cms.service.impl;

import com.homebase.ecom.cms.dto.request.CreateBlockRequest;
import com.homebase.ecom.cms.dto.response.BlockResult;
import com.homebase.ecom.cms.model.CmsBlock;
import com.homebase.ecom.cms.model.port.CmsBlockRepository;
import com.homebase.ecom.cms.service.CmsBlockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmsBlockServiceImpl implements CmsBlockService {

    private static final Logger log = LoggerFactory.getLogger(CmsBlockServiceImpl.class);
    private final CmsBlockRepository cmsBlockRepository;

    public CmsBlockServiceImpl(CmsBlockRepository cmsBlockRepository) {
        this.cmsBlockRepository = cmsBlockRepository;
    }

    @Override
    public BlockResult create(CreateBlockRequest request) {
        CmsBlock model = fromRequest(request);
        CmsBlock saved = cmsBlockRepository.save(model);
        log.info("Created CMS block id={} type={}", saved.getId(), saved.getBlockType());
        return toResult(saved);
    }

    @Override
    public BlockResult retrieve(String id) {
        CmsBlock model = cmsBlockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CMS block not found: " + id));
        return toResult(model);
    }

    @Override
    public BlockResult update(String id, CreateBlockRequest request) {
        cmsBlockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CMS block not found: " + id));
        CmsBlock model = fromRequest(request);
        model.setId(id);
        CmsBlock updated = cmsBlockRepository.save(model);
        log.info("Updated CMS block id={}", updated.getId());
        return toResult(updated);
    }

    @Override
    public void delete(String id) {
        cmsBlockRepository.delete(id);
        log.info("Deleted CMS block id={}", id);
    }

    private BlockResult toResult(CmsBlock model) {
        BlockResult result = new BlockResult();
        result.setId(model.getId());
        result.setPageId(model.getPageId());
        result.setBlockType(model.getBlockType());
        result.setData(model.getData());
        result.setSortOrder(model.getSortOrder());
        result.setActive(model.isActive());
        return result;
    }

    private CmsBlock fromRequest(CreateBlockRequest request) {
        CmsBlock model = new CmsBlock();
        model.setPageId(request.getPageId());
        model.setBlockType(request.getBlockType());
        model.setData(request.getData());
        model.setSortOrder(request.getSortOrder());
        model.setActive(request.isActive());
        return model;
    }
}
