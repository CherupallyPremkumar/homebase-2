package com.homebase.ecom.cms.service;

import com.homebase.ecom.cms.dto.request.CreateBlockRequest;
import com.homebase.ecom.cms.dto.request.CreateMediaRequest;
import com.homebase.ecom.cms.dto.request.CreatePageRequest;
import com.homebase.ecom.cms.dto.request.CreateScheduleRequest;
import com.homebase.ecom.cms.dto.response.*;

import java.util.List;

public interface CmsService {
    PageResult getPage(String slug);
    PageResult savePage(CreatePageRequest page);
    List<BannerResult> getBanners(String position);

    // Block management
    List<BlockResult> getBlocksByPageId(String pageId);
    BlockResult addBlock(String pageId, CreateBlockRequest block);
    BlockResult updateBlock(String blockId, CreateBlockRequest block);
    void removeBlock(String blockId);
    void reorderBlocks(String pageId, List<String> blockIds);

    // Version history
    List<PageVersionResult> getVersionHistory(String pageId);
    PageResult restoreVersion(String pageId, String versionId);

    // Media library
    List<MediaResult> getMedia(String folder);
    MediaResult uploadMedia(CreateMediaRequest media);
    void deleteMedia(String mediaId);

    // Scheduling
    List<ScheduleResult> getSchedules(String pageId);
    ScheduleResult createSchedule(String pageId, CreateScheduleRequest schedule);
}
