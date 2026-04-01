package com.homebase.ecom.cms.infrastructure.integration;

import com.homebase.ecom.cms.model.port.CmsConfigPort;
import com.homebase.ecom.cconfig.sdk.CconfigClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Infrastructure adapter: reads CMS policy config from cconfig.
 * Returns typed values with sensible defaults if cconfig is unavailable.
 */
public class CconfigCmsConfigAdapter implements CmsConfigPort {

    private static final Logger log = LoggerFactory.getLogger(CconfigCmsConfigAdapter.class);

    private final CconfigClient cconfigClient;
    private final String moduleName;
    private final ObjectMapper mapper = new ObjectMapper();

    public CconfigCmsConfigAdapter(CconfigClient cconfigClient, String moduleName) {
        this.cconfigClient = cconfigClient;
        this.moduleName = moduleName;
    }

    // ---- Page ----

    @Override public int getPageTitleMaxLength() { return configInt("/policies/page/titleMaxLength", 200); }
    @Override public int getPageBodyMaxLength() { return configInt("/policies/page/bodyMaxLength", 50000); }
    @Override public int getMaxBlocksPerPage() { return configInt("/policies/page/maxBlocksPerPage", 20); }

    // ---- Banner ----

    @Override public int getBannerTitleMaxLength() { return configInt("/policies/banner/titleMaxLength", 100); }
    @Override public int getMaxBannersPerPosition() { return configInt("/policies/banner/maxPerPosition", 5); }
    @Override public int getBannerImageMinWidth() { return configInt("/policies/banner/imageMinWidth", 800); }
    @Override public int getBannerImageMinHeight() { return configInt("/policies/banner/imageMinHeight", 400); }
    @Override public int getBannerImageMaxSizeKb() { return configInt("/policies/banner/imageMaxSizeKb", 500); }

    // ---- Announcement ----

    @Override public int getAnnouncementTitleMaxLength() { return configInt("/policies/announcement/titleMaxLength", 200); }
    @Override public int getAnnouncementMessageMaxLength() { return configInt("/policies/announcement/messageMaxLength", 2000); }
    @Override public int getMaxActiveAnnouncements() { return configInt("/policies/announcement/maxActive", 3); }

    // ---- Media ----

    @Override public int getMediaMaxSizeMb() { return configInt("/policies/media/maxSizeMb", 5); }
    @Override public String getAllowedMimeTypes() { return configString("/policies/media/allowedMimeTypes", "image/jpeg,image/png,image/webp,image/gif"); }

    // ---- Internal ----

    private JsonNode getCmsConfig() {
        try {
            if (cconfigClient != null) {
                Map<String, Object> map = cconfigClient.value(moduleName, null);
                if (map != null) {
                    return mapper.valueToTree(map);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to load cms config from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    private int configInt(String path, int defaultVal) {
        JsonNode node = getCmsConfig().at(path);
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : defaultVal;
    }

    private String configString(String path, String defaultVal) {
        JsonNode node = getCmsConfig().at(path);
        return (!node.isMissingNode() && node.isTextual()) ? node.asText() : defaultVal;
    }
}
