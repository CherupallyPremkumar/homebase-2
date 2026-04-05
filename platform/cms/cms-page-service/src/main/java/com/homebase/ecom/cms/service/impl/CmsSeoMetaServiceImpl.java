package com.homebase.ecom.cms.service.impl;

import com.homebase.ecom.cms.dto.request.CreateSeoMetaRequest;
import com.homebase.ecom.cms.dto.response.SeoMetaResult;
import com.homebase.ecom.cms.model.CmsSeoMeta;
import com.homebase.ecom.cms.model.port.CmsSeoMetaRepository;
import com.homebase.ecom.cms.service.CmsSeoMetaService;


public class CmsSeoMetaServiceImpl implements CmsSeoMetaService {

    private final CmsSeoMetaRepository cmsSeoMetaRepository;

    public CmsSeoMetaServiceImpl(CmsSeoMetaRepository cmsSeoMetaRepository) {
        this.cmsSeoMetaRepository = cmsSeoMetaRepository;
    }

    @Override
    public SeoMetaResult create(CreateSeoMetaRequest request) {
        CmsSeoMeta model = fromRequest(request);
        CmsSeoMeta saved = cmsSeoMetaRepository.save(model);
        return toResult(saved);
    }

    @Override
    public SeoMetaResult retrieve(String id) {
        CmsSeoMeta model = cmsSeoMetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found: " + id));
        return toResult(model);
    }

    @Override
    public void delete(String id) {
        cmsSeoMetaRepository.delete(id);
    }

    private SeoMetaResult toResult(CmsSeoMeta model) {
        SeoMetaResult result = new SeoMetaResult();
        result.setId(model.getId());
        result.setPageId(model.getPageId());
        result.setOgTitle(model.getOgTitle());
        result.setOgDescription(model.getOgDescription());
        result.setOgImageUrl(model.getOgImageUrl());
        result.setOgType(model.getOgType());
        result.setTwitterCard(model.getTwitterCard());
        result.setStructuredData(model.getStructuredData());
        result.setCanonicalUrl(model.getCanonicalUrl());
        result.setRobots(model.getRobots());
        result.setHreflang(model.getHreflang());
        result.setKeywords(model.getKeywords());
        return result;
    }

    private CmsSeoMeta fromRequest(CreateSeoMetaRequest request) {
        CmsSeoMeta model = new CmsSeoMeta();
        model.setId(request.getId());
        model.setPageId(request.getPageId());
        model.setOgTitle(request.getOgTitle());
        model.setOgDescription(request.getOgDescription());
        model.setOgImageUrl(request.getOgImageUrl());
        model.setOgType(request.getOgType());
        model.setTwitterCard(request.getTwitterCard());
        model.setStructuredData(request.getStructuredData());
        model.setCanonicalUrl(request.getCanonicalUrl());
        model.setRobots(request.getRobots());
        model.setHreflang(request.getHreflang());
        model.setKeywords(request.getKeywords());
        return model;
    }
}
