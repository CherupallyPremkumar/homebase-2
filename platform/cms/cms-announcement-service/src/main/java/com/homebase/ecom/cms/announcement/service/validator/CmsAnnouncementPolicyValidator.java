package com.homebase.ecom.cms.announcement.service.validator;

import com.homebase.ecom.cms.model.Announcement;
import com.homebase.ecom.cms.model.port.CmsConfigPort;
import org.chenile.base.exception.BadRequestException;

/**
 * Validates CMS announcement operations against cconfig policies.
 */
public class CmsAnnouncementPolicyValidator {

    public static final int ANNOUNCEMENT_TITLE_REQUIRED = 32001;
    public static final int ANNOUNCEMENT_TITLE_TOO_LONG = 32002;
    public static final int ANNOUNCEMENT_MESSAGE_TOO_LONG = 32003;

    private final CmsConfigPort configPort;

    public CmsAnnouncementPolicyValidator(CmsConfigPort configPort) {
        this.configPort = configPort;
    }

    public void validateCreate(Announcement announcement) {
        if (announcement.getTitle() == null || announcement.getTitle().isBlank()) {
            throw new BadRequestException(ANNOUNCEMENT_TITLE_REQUIRED, new Object[]{});
        }
        if (announcement.getTitle().length() > configPort.getAnnouncementTitleMaxLength()) {
            throw new BadRequestException(ANNOUNCEMENT_TITLE_TOO_LONG, new Object[]{configPort.getAnnouncementTitleMaxLength()});
        }
        if (announcement.getMessage() != null && announcement.getMessage().length() > configPort.getAnnouncementMessageMaxLength()) {
            throw new BadRequestException(ANNOUNCEMENT_MESSAGE_TOO_LONG, new Object[]{configPort.getAnnouncementMessageMaxLength()});
        }
    }

    public int getMaxActiveAnnouncements() {
        return configPort.getMaxActiveAnnouncements();
    }
}
