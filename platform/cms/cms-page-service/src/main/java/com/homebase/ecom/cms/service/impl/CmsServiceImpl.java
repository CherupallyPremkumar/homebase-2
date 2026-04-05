package com.homebase.ecom.cms.service.impl;

import com.homebase.ecom.cms.dto.request.CreateBlockRequest;
import com.homebase.ecom.cms.dto.request.CreateMediaRequest;
import com.homebase.ecom.cms.dto.request.CreatePageRequest;
import com.homebase.ecom.cms.dto.request.CreateScheduleRequest;
import com.homebase.ecom.cms.dto.response.*;
import com.homebase.ecom.cms.infrastructure.persistence.mapper.CmsMapper;
import com.homebase.ecom.cms.model.Banner;
import com.homebase.ecom.cms.model.CmsPage;
import com.homebase.ecom.cms.model.port.BannerRepository;
import com.homebase.ecom.cms.model.port.CmsPageRepository;
import com.homebase.ecom.cms.service.CmsService;

import java.util.*;
import java.util.stream.Collectors;

public class CmsServiceImpl implements CmsService {

    private final CmsPageRepository pageRepository;
    private final BannerRepository bannerRepository;
    private final CmsMapper mapper;

    // In-memory stores for block, version, media, schedule — will be backed by JPA repositories
    // once the corresponding infrastructure entities are wired.
    private final Map<String, List<BlockResult>> blocksByPage = new LinkedHashMap<>();
    private final Map<String, BlockResult> blocksById = new LinkedHashMap<>();
    private final Map<String, List<PageVersionResult>> versionsByPage = new LinkedHashMap<>();
    private final Map<String, MediaResult> mediaById = new LinkedHashMap<>();
    private final Map<String, List<ScheduleResult>> schedulesByPage = new LinkedHashMap<>();

    public CmsServiceImpl(CmsPageRepository pageRepository, BannerRepository bannerRepository, CmsMapper mapper) {
        this.pageRepository = pageRepository;
        this.bannerRepository = bannerRepository;
        this.mapper = mapper;
    }

    // ================================================================
    // Existing page/banner methods
    // ================================================================

    @Override
    public PageResult getPage(String slug) {
        CmsPage page = pageRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("CMS page not found for slug: " + slug));
        return toPageResult(page);
    }

    @Override
    public PageResult savePage(CreatePageRequest request) {
        CmsPage page = fromPageRequest(request);
        CmsPage saved = pageRepository.save(page);
        return toPageResult(saved);
    }

    @Override
    public List<BannerResult> getBanners(String position) {
        List<Banner> banners;
        if (position != null && !position.isEmpty()) {
            banners = bannerRepository.findByPosition(position);
        } else {
            banners = bannerRepository.findAllActive();
        }
        return banners.stream().map(this::toBannerResult).collect(Collectors.toList());
    }

    // ================================================================
    // Block management
    // ================================================================

    @Override
    public List<BlockResult> getBlocksByPageId(String pageId) {
        List<BlockResult> blocks = blocksByPage.getOrDefault(pageId, new ArrayList<>());
        blocks.sort(Comparator.comparingInt(BlockResult::getSortOrder));
        return blocks;
    }

    @Override
    public BlockResult addBlock(String pageId, CreateBlockRequest request) {
        BlockResult block = toBlockResult(request);
        if (block.getId() == null || block.getId().isEmpty()) {
            block.setId(UUID.randomUUID().toString());
        }
        block.setPageId(pageId);

        blocksByPage.computeIfAbsent(pageId, k -> new ArrayList<>()).add(block);
        blocksById.put(block.getId(), block);
        return block;
    }

    @Override
    public BlockResult updateBlock(String blockId, CreateBlockRequest request) {
        BlockResult existing = blocksById.get(blockId);
        if (existing == null) {
            throw new RuntimeException("Block not found: " + blockId);
        }

        existing.setBlockType(request.getBlockType());
        existing.setData(request.getData());
        existing.setSortOrder(request.getSortOrder());
        existing.setActive(request.isActive());
        return existing;
    }

    @Override
    public void removeBlock(String blockId) {
        BlockResult block = blocksById.remove(blockId);
        if (block != null) {
            List<BlockResult> pageBlocks = blocksByPage.get(block.getPageId());
            if (pageBlocks != null) {
                pageBlocks.removeIf(b -> b.getId().equals(blockId));
            }
        }
    }

    @Override
    public void reorderBlocks(String pageId, List<String> blockIds) {
        List<BlockResult> pageBlocks = blocksByPage.getOrDefault(pageId, new ArrayList<>());
        for (int i = 0; i < blockIds.size(); i++) {
            String bid = blockIds.get(i);
            BlockResult block = blocksById.get(bid);
            if (block != null && block.getPageId().equals(pageId)) {
                block.setSortOrder(i);
            }
        }
        pageBlocks.sort(Comparator.comparingInt(BlockResult::getSortOrder));
    }

    // ================================================================
    // Version history
    // ================================================================

    @Override
    public List<PageVersionResult> getVersionHistory(String pageId) {
        List<PageVersionResult> versions = versionsByPage.getOrDefault(pageId, new ArrayList<>());
        versions.sort((a, b) -> Integer.compare(b.getVersionNumber(), a.getVersionNumber()));
        return versions;
    }

    @Override
    public PageResult restoreVersion(String pageId, String versionId) {
        // Find the version
        List<PageVersionResult> versions = versionsByPage.getOrDefault(pageId, new ArrayList<>());
        PageVersionResult targetVersion = versions.stream()
                .filter(v -> v.getId().equals(versionId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Version not found: " + versionId));

        // In a full implementation this would:
        //   1. Load the block snapshot from the version record
        //   2. Replace all current blocks with the snapshot
        //   3. Set the page back to DRAFT state
        // For now, reload the page to return its current state
        CmsPage page = pageRepository.findById(pageId)
                .orElseThrow(() -> new RuntimeException("Page not found: " + pageId));
        return toPageResult(page);
    }

    // ================================================================
    // Media library
    // ================================================================

    @Override
    public List<MediaResult> getMedia(String folder) {
        if (folder != null && !folder.isEmpty()) {
            return mediaById.values().stream()
                    .filter(m -> folder.equals(m.getFolder()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(mediaById.values());
    }

    @Override
    public MediaResult uploadMedia(CreateMediaRequest request) {
        MediaResult media = toMediaResult(request);
        if (media.getId() == null || media.getId().isEmpty()) {
            media.setId(UUID.randomUUID().toString());
        }
        mediaById.put(media.getId(), media);
        return media;
    }

    @Override
    public void deleteMedia(String mediaId) {
        mediaById.remove(mediaId);
    }

    // ================================================================
    // Scheduling
    // ================================================================

    @Override
    public List<ScheduleResult> getSchedules(String pageId) {
        return schedulesByPage.getOrDefault(pageId, new ArrayList<>());
    }

    @Override
    public ScheduleResult createSchedule(String pageId, CreateScheduleRequest request) {
        ScheduleResult schedule = toScheduleResult(request);
        if (schedule.getId() == null || schedule.getId().isEmpty()) {
            schedule.setId(UUID.randomUUID().toString());
        }
        schedule.setPageId(pageId);
        schedulesByPage.computeIfAbsent(pageId, k -> new ArrayList<>()).add(schedule);
        return schedule;
    }

    // ================================================================
    // Mapping helpers
    // ================================================================

    private PageResult toPageResult(CmsPage page) {
        PageResult result = new PageResult();
        result.setId(page.getId());
        result.setSlug(page.getSlug());
        result.setTitle(page.getTitle());
        result.setPageType(page.getPageType());
        result.setPublished(page.isPublished());
        result.setPublishedAt(page.getPublishedAt());
        return result;
    }

    private CmsPage fromPageRequest(CreatePageRequest request) {
        CmsPage page = new CmsPage();
        page.setId(request.getId());
        page.setSlug(request.getSlug());
        page.setTitle(request.getTitle());
        page.setPageType(request.getPageType());
        page.setPublished(request.isPublished());
        page.setPublishedAt(request.getPublishedAt());
        return page;
    }

    private BannerResult toBannerResult(Banner banner) {
        BannerResult result = new BannerResult();
        result.setId(banner.getId());
        result.setName(banner.getName());
        result.setImageUrl(banner.getImageUrl());
        result.setPosition(banner.getPosition());
        result.setActive(banner.isActive());
        return result;
    }

    private BlockResult toBlockResult(CreateBlockRequest request) {
        BlockResult result = new BlockResult();
        result.setId(request.getId());
        result.setPageId(request.getPageId());
        result.setBlockType(request.getBlockType());
        result.setData(request.getData());
        result.setSortOrder(request.getSortOrder());
        result.setActive(request.isActive());
        return result;
    }

    private MediaResult toMediaResult(CreateMediaRequest request) {
        MediaResult result = new MediaResult();
        result.setId(request.getId());
        result.setFileKey(request.getFileKey());
        result.setOriginalName(request.getOriginalName());
        result.setCdnUrl(request.getCdnUrl());
        result.setMimeType(request.getMimeType());
        result.setFileSizeBytes(request.getFileSizeBytes());
        result.setWidth(request.getWidth());
        result.setHeight(request.getHeight());
        result.setAltText(request.getAltText());
        result.setFolder(request.getFolder());
        return result;
    }

    private ScheduleResult toScheduleResult(CreateScheduleRequest request) {
        ScheduleResult result = new ScheduleResult();
        result.setId(request.getId());
        result.setPageId(request.getPageId());
        result.setPublishAt(request.getPublishAt());
        result.setUnpublishAt(request.getUnpublishAt());
        result.setTimezone(request.getTimezone());
        result.setActive(request.isActive());
        return result;
    }
}
