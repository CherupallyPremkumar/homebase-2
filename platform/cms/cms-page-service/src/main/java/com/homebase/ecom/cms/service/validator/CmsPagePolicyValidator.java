package com.homebase.ecom.cms.service.validator;

import com.homebase.ecom.cms.model.CmsPage;
import com.homebase.ecom.cms.model.port.CmsConfigPort;
import org.chenile.base.exception.BadRequestException;

/**
 * Validates CMS page operations against cconfig policies.
 */
public class CmsPagePolicyValidator {

    public static final int PAGE_TITLE_REQUIRED = 30001;
    public static final int PAGE_TITLE_TOO_LONG = 30002;
    public static final int PAGE_SLUG_REQUIRED = 30003;
    public static final int PAGE_BODY_TOO_LONG = 30004;
    public static final int PAGE_MAX_BLOCKS_EXCEEDED = 30005;

    private final CmsConfigPort configPort;

    public CmsPagePolicyValidator(CmsConfigPort configPort) {
        this.configPort = configPort;
    }

    public void validateCreate(CmsPage page) {
        if (page.getTitle() == null || page.getTitle().isBlank()) {
            throw new BadRequestException(PAGE_TITLE_REQUIRED, new Object[]{});
        }
        if (page.getTitle().length() > configPort.getPageTitleMaxLength()) {
            throw new BadRequestException(PAGE_TITLE_TOO_LONG, new Object[]{configPort.getPageTitleMaxLength()});
        }
        if (page.getSlug() == null || page.getSlug().isBlank()) {
            throw new BadRequestException(PAGE_SLUG_REQUIRED, new Object[]{});
        }
    }

    public void validateBody(CmsPage page) {
        if (page.getBody() != null && page.getBody().length() > configPort.getPageBodyMaxLength()) {
            throw new BadRequestException(PAGE_BODY_TOO_LONG, new Object[]{configPort.getPageBodyMaxLength()});
        }
    }

    public int getMaxBlocksPerPage() {
        return configPort.getMaxBlocksPerPage();
    }
}
