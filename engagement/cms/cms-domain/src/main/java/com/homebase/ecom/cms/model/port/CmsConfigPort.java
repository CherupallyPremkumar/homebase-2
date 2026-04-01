package com.homebase.ecom.cms.model.port;

/**
 * Domain port for CMS policy configuration.
 * Infrastructure adapter reads from cconfig; domain sees typed methods only.
 */
public interface CmsConfigPort {

    // Page policies
    int getPageTitleMaxLength();
    int getPageBodyMaxLength();
    int getMaxBlocksPerPage();

    // Banner policies
    int getBannerTitleMaxLength();
    int getMaxBannersPerPosition();
    int getBannerImageMinWidth();
    int getBannerImageMinHeight();
    int getBannerImageMaxSizeKb();

    // Announcement policies
    int getAnnouncementTitleMaxLength();
    int getAnnouncementMessageMaxLength();
    int getMaxActiveAnnouncements();

    // Media policies
    int getMediaMaxSizeMb();
    String getAllowedMimeTypes();
}
