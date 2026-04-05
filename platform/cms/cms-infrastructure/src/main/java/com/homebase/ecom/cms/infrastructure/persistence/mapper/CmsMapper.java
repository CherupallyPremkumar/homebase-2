package com.homebase.ecom.cms.infrastructure.persistence.mapper;

import com.homebase.ecom.cms.model.*;
import com.homebase.ecom.cms.infrastructure.persistence.entity.*;

import org.chenile.workflow.activities.model.ActivityLog;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CmsMapper {

    // ---- CmsPage (StateEntity) ----

    public CmsPage toModel(CmsPageEntity entity) {
        if (entity == null) return null;

        CmsPage model = new CmsPage();
        // Base entity fields
        model.setId(entity.getId());
        model.setSlug(entity.getSlug());
        model.setTitle(entity.getTitle());
        model.setBody(entity.getBody());
        model.setPageType(entity.getPageType());
        model.setLayoutType(entity.getLayoutType());
        model.setMetaTitle(entity.getMetaTitle());
        model.setMetaDescription(entity.getMetaDescription());
        model.setPublished(entity.isPublished());
        model.setPublishedAt(entity.getPublishedAt());
        model.setTenant(entity.tenant);

        // STM state fields
        model.setCurrentState(entity.getCurrentState());
        model.setStateEntryTime(entity.getStateEntryTime());
        model.setSlaTendingLate(entity.getSlaTendingLate());
        model.setSlaLate(entity.getSlaLate());

        // Audit fields
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setLastModifiedBy(entity.getLastModifiedBy());
        model.setCreatedBy(entity.getCreatedBy());
        model.setVersion(entity.getVersion() != null ? entity.getVersion() : 0L);

        // Activities
        if (entity.getActivities() != null) {
            List<ActivityLog> activityLogs = new ArrayList<>();
            for (CmsPageActivityLogEntity actEntity : entity.getActivities()) {
                CmsPageActivityLog actLog = new CmsPageActivityLog();
                actLog.actionType = actEntity.getName();
                actLog.activityComment = actEntity.getComment();
                actLog.activitySuccess = actEntity.getSuccess();
                activityLogs.add(actLog);
            }
            model.setActivities(activityLogs);
        }

        return model;
    }

    public CmsPageEntity toEntity(CmsPage model) {
        if (model == null) return null;

        CmsPageEntity entity = new CmsPageEntity();
        if (model.getId() == null || model.getId().trim().isEmpty()) {
            entity.setId(UUID.randomUUID().toString());
        } else {
            entity.setId(model.getId());
        }
        entity.setSlug(model.getSlug());
        entity.setTitle(model.getTitle());
        entity.setBody(model.getBody());
        entity.setPageType(model.getPageType());
        entity.setLayoutType(model.getLayoutType());
        entity.setMetaTitle(model.getMetaTitle());
        entity.setMetaDescription(model.getMetaDescription());
        entity.setPublished(model.isPublished());
        entity.setPublishedAt(model.getPublishedAt());
        entity.tenant = model.getTenant();

        // STM state fields
        entity.setCurrentState(model.getCurrentState());
        entity.setStateEntryTime(model.getStateEntryTime());
        entity.setSlaTendingLate(model.getSlaTendingLate());
        entity.setSlaLate(model.getSlaLate());

        // Audit fields
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setLastModifiedBy(model.getLastModifiedBy());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setVersion(model.getVersion() != null ? model.getVersion() : 0L);

        // Activities
        if (model.obtainActivities() != null) {
            entity.setActivities(
                model.obtainActivities().stream()
                    .map(this::toCmsPageActivityEntity)
                    .collect(Collectors.toList())
            );
        }

        return entity;
    }

    public void mergeEntity(CmsPageEntity existing, CmsPageEntity updated) {
        // STM state (critical)
        existing.setCurrentState(updated.getCurrentState());
        existing.setStateEntryTime(updated.getStateEntryTime());
        existing.setSlaTendingLate(updated.getSlaTendingLate());
        existing.setSlaLate(updated.getSlaLate());

        // Domain fields
        existing.setSlug(updated.getSlug());
        existing.setTitle(updated.getTitle());
        existing.setBody(updated.getBody());
        existing.setPageType(updated.getPageType());
        existing.setLayoutType(updated.getLayoutType());
        existing.setMetaTitle(updated.getMetaTitle());
        existing.setMetaDescription(updated.getMetaDescription());
        existing.setPublished(updated.isPublished());
        existing.setPublishedAt(updated.getPublishedAt());
        existing.tenant = updated.tenant;

        // Audit
        existing.setLastModifiedBy(updated.getLastModifiedBy());
        existing.setLastModifiedTime(updated.getLastModifiedTime());

        // Activities -- replace collection contents
        existing.getActivities().clear();
        if (updated.getActivities() != null) {
            existing.getActivities().addAll(updated.getActivities());
        }
    }

    private CmsPageActivityLogEntity toCmsPageActivityEntity(ActivityLog activityLog) {
        CmsPageActivityLogEntity entity = new CmsPageActivityLogEntity();
        entity.setEventId(activityLog.getName());
        entity.setComment(activityLog.getComment());
        entity.setSuccess(activityLog.getSuccess());
        return entity;
    }

    // ---- Banner (StateEntity) ----

    public Banner toModel(BannerEntity entity) {
        if (entity == null) return null;

        Banner model = new Banner();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setTitle(entity.getTitle());
        model.setImageUrl(entity.getImageUrl());
        model.setMobileImageUrl(entity.getMobileImageUrl());
        model.setMediaId(entity.getMediaId());
        model.setMobileMediaId(entity.getMobileMediaId());
        model.setLinkUrl(entity.getLinkUrl());
        model.setPosition(entity.getPosition());
        model.setDisplayOrder(entity.getDisplayOrder());
        model.setActiveFrom(entity.getActiveFrom());
        model.setActiveTo(entity.getActiveTo());
        model.setActive(entity.isActive());
        model.setClickCount(entity.getClickCount());
        model.setTenant(entity.tenant);

        // STM state fields
        model.setCurrentState(entity.getCurrentState());
        model.setStateEntryTime(entity.getStateEntryTime());
        model.setSlaTendingLate(entity.getSlaTendingLate());
        model.setSlaLate(entity.getSlaLate());

        // Audit fields
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setLastModifiedBy(entity.getLastModifiedBy());
        model.setCreatedBy(entity.getCreatedBy());
        model.setVersion(entity.getVersion() != null ? entity.getVersion() : 0L);

        // Activities
        if (entity.getActivities() != null) {
            List<ActivityLog> activityLogs = new ArrayList<>();
            for (BannerActivityLogEntity actEntity : entity.getActivities()) {
                BannerActivityLog actLog = new BannerActivityLog();
                actLog.actionType = actEntity.getName();
                actLog.activityComment = actEntity.getComment();
                actLog.activitySuccess = actEntity.getSuccess();
                activityLogs.add(actLog);
            }
            model.setActivities(activityLogs);
        }

        return model;
    }

    public BannerEntity toEntity(Banner model) {
        if (model == null) return null;

        BannerEntity entity = new BannerEntity();
        if (model.getId() == null || model.getId().trim().isEmpty()) {
            entity.setId(UUID.randomUUID().toString());
        } else {
            entity.setId(model.getId());
        }
        entity.setName(model.getName());
        entity.setTitle(model.getTitle());
        entity.setImageUrl(model.getImageUrl());
        entity.setMobileImageUrl(model.getMobileImageUrl());
        entity.setMediaId(model.getMediaId());
        entity.setMobileMediaId(model.getMobileMediaId());
        entity.setLinkUrl(model.getLinkUrl());
        entity.setPosition(model.getPosition());
        entity.setDisplayOrder(model.getDisplayOrder());
        entity.setActiveFrom(model.getActiveFrom());
        entity.setActiveTo(model.getActiveTo());
        entity.setActive(model.isActive());
        entity.setClickCount(model.getClickCount());
        entity.tenant = model.getTenant();

        // STM state fields
        entity.setCurrentState(model.getCurrentState());
        entity.setStateEntryTime(model.getStateEntryTime());
        entity.setSlaTendingLate(model.getSlaTendingLate());
        entity.setSlaLate(model.getSlaLate());

        // Audit fields
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setLastModifiedBy(model.getLastModifiedBy());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setVersion(model.getVersion() != null ? model.getVersion() : 0L);

        // Activities
        if (model.obtainActivities() != null) {
            entity.setActivities(
                model.obtainActivities().stream()
                    .map(this::toBannerActivityEntity)
                    .collect(Collectors.toList())
            );
        }

        return entity;
    }

    public void mergeBannerEntity(BannerEntity existing, BannerEntity updated) {
        // STM state (critical)
        existing.setCurrentState(updated.getCurrentState());
        existing.setStateEntryTime(updated.getStateEntryTime());
        existing.setSlaTendingLate(updated.getSlaTendingLate());
        existing.setSlaLate(updated.getSlaLate());

        // Domain fields
        existing.setName(updated.getName());
        existing.setTitle(updated.getTitle());
        existing.setImageUrl(updated.getImageUrl());
        existing.setMobileImageUrl(updated.getMobileImageUrl());
        existing.setMediaId(updated.getMediaId());
        existing.setMobileMediaId(updated.getMobileMediaId());
        existing.setLinkUrl(updated.getLinkUrl());
        existing.setPosition(updated.getPosition());
        existing.setDisplayOrder(updated.getDisplayOrder());
        existing.setActiveFrom(updated.getActiveFrom());
        existing.setActiveTo(updated.getActiveTo());
        existing.setActive(updated.isActive());
        existing.setClickCount(updated.getClickCount());
        existing.tenant = updated.tenant;

        // Audit
        existing.setLastModifiedBy(updated.getLastModifiedBy());
        existing.setLastModifiedTime(updated.getLastModifiedTime());

        // Activities -- replace collection contents
        existing.getActivities().clear();
        if (updated.getActivities() != null) {
            existing.getActivities().addAll(updated.getActivities());
        }
    }

    private BannerActivityLogEntity toBannerActivityEntity(ActivityLog activityLog) {
        BannerActivityLogEntity entity = new BannerActivityLogEntity();
        entity.setEventId(activityLog.getName());
        entity.setComment(activityLog.getComment());
        entity.setSuccess(activityLog.getSuccess());
        return entity;
    }

    // ---- Announcement (StateEntity) ----

    public Announcement toModel(AnnouncementEntity entity) {
        if (entity == null) return null;

        Announcement model = new Announcement();
        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        model.setMessage(entity.getMessage());
        model.setAnnouncementType(entity.getAnnouncementType());
        model.setTargetAudience(entity.getTargetAudience());
        model.setPriority(entity.getPriority());
        model.setStatus(entity.getStatus());
        model.setScheduledDate(entity.getScheduledDate());
        model.setExpiryDate(entity.getExpiryDate());
        model.setActionUrl(entity.getActionUrl());
        model.setActionLabel(entity.getActionLabel());
        model.setDismissible(entity.isDismissible());
        model.setTenant(entity.tenant);

        // STM state fields
        model.setCurrentState(entity.getCurrentState());
        model.setStateEntryTime(entity.getStateEntryTime());
        model.setSlaTendingLate(entity.getSlaTendingLate());
        model.setSlaLate(entity.getSlaLate());

        // Audit fields
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setLastModifiedBy(entity.getLastModifiedBy());
        model.setCreatedBy(entity.getCreatedBy());
        model.setVersion(entity.getVersion() != null ? entity.getVersion() : 0L);

        // Activities
        if (entity.getActivities() != null) {
            List<ActivityLog> activityLogs = new ArrayList<>();
            for (AnnouncementActivityLogEntity actEntity : entity.getActivities()) {
                AnnouncementActivityLog actLog = new AnnouncementActivityLog();
                actLog.actionType = actEntity.getName();
                actLog.activityComment = actEntity.getComment();
                actLog.activitySuccess = actEntity.getSuccess();
                activityLogs.add(actLog);
            }
            model.setActivities(activityLogs);
        }

        return model;
    }

    public AnnouncementEntity toEntity(Announcement model) {
        if (model == null) return null;

        AnnouncementEntity entity = new AnnouncementEntity();
        if (model.getId() == null || model.getId().trim().isEmpty()) {
            entity.setId(UUID.randomUUID().toString());
        } else {
            entity.setId(model.getId());
        }
        entity.setTitle(model.getTitle());
        entity.setMessage(model.getMessage());
        entity.setAnnouncementType(model.getAnnouncementType());
        entity.setTargetAudience(model.getTargetAudience());
        entity.setPriority(model.getPriority());
        entity.setStatus(model.getStatus());
        entity.setScheduledDate(model.getScheduledDate());
        entity.setExpiryDate(model.getExpiryDate());
        entity.setActionUrl(model.getActionUrl());
        entity.setActionLabel(model.getActionLabel());
        entity.setDismissible(model.isDismissible());
        entity.tenant = model.getTenant();

        // STM state fields
        entity.setCurrentState(model.getCurrentState());
        entity.setStateEntryTime(model.getStateEntryTime());
        entity.setSlaTendingLate(model.getSlaTendingLate());
        entity.setSlaLate(model.getSlaLate());

        // Audit fields
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setLastModifiedBy(model.getLastModifiedBy());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setVersion(model.getVersion() != null ? model.getVersion() : 0L);

        // Activities
        if (model.obtainActivities() != null) {
            entity.setActivities(
                model.obtainActivities().stream()
                    .map(this::toAnnouncementActivityEntity)
                    .collect(Collectors.toList())
            );
        }

        return entity;
    }

    public void mergeAnnouncementEntity(AnnouncementEntity existing, AnnouncementEntity updated) {
        // STM state (critical)
        existing.setCurrentState(updated.getCurrentState());
        existing.setStateEntryTime(updated.getStateEntryTime());
        existing.setSlaTendingLate(updated.getSlaTendingLate());
        existing.setSlaLate(updated.getSlaLate());

        // Domain fields
        existing.setTitle(updated.getTitle());
        existing.setMessage(updated.getMessage());
        existing.setAnnouncementType(updated.getAnnouncementType());
        existing.setTargetAudience(updated.getTargetAudience());
        existing.setPriority(updated.getPriority());
        existing.setStatus(updated.getStatus());
        existing.setScheduledDate(updated.getScheduledDate());
        existing.setExpiryDate(updated.getExpiryDate());
        existing.setActionUrl(updated.getActionUrl());
        existing.setActionLabel(updated.getActionLabel());
        existing.setDismissible(updated.isDismissible());
        existing.tenant = updated.tenant;

        // Audit
        existing.setLastModifiedBy(updated.getLastModifiedBy());
        existing.setLastModifiedTime(updated.getLastModifiedTime());

        // Activities -- replace collection contents
        existing.getActivities().clear();
        if (updated.getActivities() != null) {
            existing.getActivities().addAll(updated.getActivities());
        }
    }

    private AnnouncementActivityLogEntity toAnnouncementActivityEntity(ActivityLog activityLog) {
        AnnouncementActivityLogEntity entity = new AnnouncementActivityLogEntity();
        entity.setEventId(activityLog.getName());
        entity.setComment(activityLog.getComment());
        entity.setSuccess(activityLog.getSuccess());
        return entity;
    }

    // ---- CmsBlock ----

    public CmsBlock toModel(CmsBlockEntity entity) {
        if (entity == null) return null;

        CmsBlock model = new CmsBlock();
        model.setId(entity.getId());
        model.setPageId(entity.getPageId());
        model.setBlockType(entity.getBlockType());
        model.setData(entity.getData());
        model.setSortOrder(entity.getSortOrder());
        model.setActive(entity.isActive());
        model.setTenant(entity.tenant);
        return model;
    }

    public CmsBlockEntity toEntity(CmsBlock model) {
        if (model == null) return null;

        CmsBlockEntity entity = new CmsBlockEntity();
        if (model.getId() == null || model.getId().trim().isEmpty()) {
            entity.setId(UUID.randomUUID().toString());
        } else {
            entity.setId(model.getId());
        }
        entity.setPageId(model.getPageId());
        entity.setBlockType(model.getBlockType());
        entity.setData(model.getData());
        entity.setSortOrder(model.getSortOrder());
        entity.setActive(model.isActive());
        entity.tenant = model.getTenant();
        return entity;
    }

    // ---- CmsPageVersion ----

    public CmsPageVersion toModel(CmsPageVersionEntity entity) {
        if (entity == null) return null;

        CmsPageVersion model = new CmsPageVersion();
        model.setId(entity.getId());
        model.setPageId(entity.getPageId());
        model.setVersionNumber(entity.getVersionNumber());
        model.setBlocksSnapshot(entity.getBlocksSnapshot());
        model.setPublishedBy(entity.getPublishedBy());
        model.setPublishedAt(entity.getPublishedAt());
        model.setChangeSummary(entity.getChangeSummary());
        model.setTenant(entity.tenant);
        return model;
    }

    public CmsPageVersionEntity toEntity(CmsPageVersion model) {
        if (model == null) return null;

        CmsPageVersionEntity entity = new CmsPageVersionEntity();
        if (model.getId() == null || model.getId().trim().isEmpty()) {
            entity.setId(UUID.randomUUID().toString());
        } else {
            entity.setId(model.getId());
        }
        entity.setPageId(model.getPageId());
        entity.setVersionNumber(model.getVersionNumber());
        entity.setBlocksSnapshot(model.getBlocksSnapshot());
        entity.setPublishedBy(model.getPublishedBy());
        entity.setPublishedAt(model.getPublishedAt());
        entity.setChangeSummary(model.getChangeSummary());
        entity.tenant = model.getTenant();
        return entity;
    }

    // ---- CmsSchedule ----

    public CmsSchedule toModel(CmsScheduleEntity entity) {
        if (entity == null) return null;

        CmsSchedule model = new CmsSchedule();
        model.setId(entity.getId());
        model.setPageId(entity.getPageId());
        model.setPublishAt(entity.getPublishAt());
        model.setUnpublishAt(entity.getUnpublishAt());
        model.setTimezone(entity.getTimezone());
        model.setActive(entity.isActive());
        model.setExecuted(entity.isExecuted());
        model.setTenant(entity.tenant);
        return model;
    }

    public CmsScheduleEntity toEntity(CmsSchedule model) {
        if (model == null) return null;

        CmsScheduleEntity entity = new CmsScheduleEntity();
        if (model.getId() == null || model.getId().trim().isEmpty()) {
            entity.setId(UUID.randomUUID().toString());
        } else {
            entity.setId(model.getId());
        }
        entity.setPageId(model.getPageId());
        entity.setPublishAt(model.getPublishAt());
        entity.setUnpublishAt(model.getUnpublishAt());
        entity.setTimezone(model.getTimezone());
        entity.setActive(model.isActive());
        entity.setExecuted(model.isExecuted());
        entity.tenant = model.getTenant();
        return entity;
    }

    // ---- CmsSeoMeta ----

    public CmsSeoMeta toModel(CmsSeoMetaEntity entity) {
        if (entity == null) return null;

        CmsSeoMeta model = new CmsSeoMeta();
        model.setId(entity.getId());
        model.setPageId(entity.getPageId());
        model.setOgTitle(entity.getOgTitle());
        model.setOgDescription(entity.getOgDescription());
        model.setOgImageUrl(entity.getOgImageUrl());
        model.setOgType(entity.getOgType());
        model.setTwitterCard(entity.getTwitterCard());
        model.setStructuredData(entity.getStructuredData());
        model.setCanonicalUrl(entity.getCanonicalUrl());
        model.setRobots(entity.getRobots());
        model.setHreflang(entity.getHreflang());
        model.setKeywords(entity.getKeywords());
        model.setTenant(entity.tenant);
        return model;
    }

    public CmsSeoMetaEntity toEntity(CmsSeoMeta model) {
        if (model == null) return null;

        CmsSeoMetaEntity entity = new CmsSeoMetaEntity();
        if (model.getId() == null || model.getId().trim().isEmpty()) {
            entity.setId(UUID.randomUUID().toString());
        } else {
            entity.setId(model.getId());
        }
        entity.setPageId(model.getPageId());
        entity.setOgTitle(model.getOgTitle());
        entity.setOgDescription(model.getOgDescription());
        entity.setOgImageUrl(model.getOgImageUrl());
        entity.setOgType(model.getOgType());
        entity.setTwitterCard(model.getTwitterCard());
        entity.setStructuredData(model.getStructuredData());
        entity.setCanonicalUrl(model.getCanonicalUrl());
        entity.setRobots(model.getRobots());
        entity.setHreflang(model.getHreflang());
        entity.setKeywords(model.getKeywords());
        entity.tenant = model.getTenant();
        return entity;
    }

    // ---- CmsMedia ----

    public CmsMedia toModel(CmsMediaEntity entity) {
        if (entity == null) return null;

        CmsMedia model = new CmsMedia();
        model.setId(entity.getId());
        model.setFileKey(entity.getFileKey());
        model.setOriginalName(entity.getOriginalName());
        model.setCdnUrl(entity.getCdnUrl());
        model.setMimeType(entity.getMimeType());
        model.setFileSizeBytes(entity.getFileSizeBytes());
        model.setWidth(entity.getWidth());
        model.setHeight(entity.getHeight());
        model.setAltText(entity.getAltText());
        model.setFolder(entity.getFolder());
        model.setTags(entity.getTags());
        model.setTenant(entity.tenant);
        return model;
    }

    public CmsMediaEntity toEntity(CmsMedia model) {
        if (model == null) return null;

        CmsMediaEntity entity = new CmsMediaEntity();
        if (model.getId() == null || model.getId().trim().isEmpty()) {
            entity.setId(UUID.randomUUID().toString());
        } else {
            entity.setId(model.getId());
        }
        entity.setFileKey(model.getFileKey());
        entity.setOriginalName(model.getOriginalName());
        entity.setCdnUrl(model.getCdnUrl());
        entity.setMimeType(model.getMimeType());
        entity.setFileSizeBytes(model.getFileSizeBytes());
        entity.setWidth(model.getWidth());
        entity.setHeight(model.getHeight());
        entity.setAltText(model.getAltText());
        entity.setFolder(model.getFolder());
        entity.setTags(model.getTags());
        entity.tenant = model.getTenant();
        return entity;
    }
}
