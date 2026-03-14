package com.homebase.ecom.cms.service.impl;

import com.homebase.ecom.cms.dto.BannerDto;
import com.homebase.ecom.cms.dto.CmsPageDto;
import com.homebase.ecom.cms.infrastructure.persistence.mapper.CmsMapper;
import com.homebase.ecom.cms.model.Banner;
import com.homebase.ecom.cms.model.CmsPage;
import com.homebase.ecom.cms.model.port.BannerRepository;
import com.homebase.ecom.cms.model.port.CmsPageRepository;
import com.homebase.ecom.cms.service.CmsService;

import java.util.List;
import java.util.stream.Collectors;

public class CmsServiceImpl implements CmsService {

    private final CmsPageRepository pageRepository;
    private final BannerRepository bannerRepository;
    private final CmsMapper mapper;

    public CmsServiceImpl(CmsPageRepository pageRepository, BannerRepository bannerRepository, CmsMapper mapper) {
        this.pageRepository = pageRepository;
        this.bannerRepository = bannerRepository;
        this.mapper = mapper;
    }

    @Override
    public CmsPageDto getPage(String slug) {
        CmsPage page = pageRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("CMS page not found for slug: " + slug));
        return toDto(page);
    }

    @Override
    public CmsPageDto savePage(CmsPageDto dto) {
        CmsPage page = fromDto(dto);
        CmsPage saved = pageRepository.save(page);
        return toDto(saved);
    }

    @Override
    public List<BannerDto> getBanners(String position) {
        List<Banner> banners;
        if (position != null && !position.isEmpty()) {
            banners = bannerRepository.findByPosition(position);
        } else {
            banners = bannerRepository.findAllActive();
        }
        return banners.stream().map(this::toBannerDto).collect(Collectors.toList());
    }

    private CmsPageDto toDto(CmsPage page) {
        CmsPageDto dto = new CmsPageDto();
        dto.setId(page.getId());
        dto.setSlug(page.getSlug());
        dto.setTitle(page.getTitle());
        dto.setPageType(page.getPageType());
        dto.setPublished(page.isPublished());
        dto.setPublishedAt(page.getPublishedAt());
        return dto;
    }

    private CmsPage fromDto(CmsPageDto dto) {
        CmsPage page = new CmsPage();
        page.setId(dto.getId());
        page.setSlug(dto.getSlug());
        page.setTitle(dto.getTitle());
        page.setPageType(dto.getPageType());
        page.setPublished(dto.isPublished());
        page.setPublishedAt(dto.getPublishedAt());
        return page;
    }

    private BannerDto toBannerDto(Banner banner) {
        BannerDto dto = new BannerDto();
        dto.setId(banner.getId());
        dto.setName(banner.getName());
        dto.setImageUrl(banner.getImageUrl());
        dto.setPosition(banner.getPosition());
        dto.setActive(banner.isActive());
        return dto;
    }
}
