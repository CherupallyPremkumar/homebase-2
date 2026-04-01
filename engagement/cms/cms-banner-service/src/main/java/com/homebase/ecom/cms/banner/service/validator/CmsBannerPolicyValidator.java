package com.homebase.ecom.cms.banner.service.validator;

import com.homebase.ecom.cms.model.Banner;
import com.homebase.ecom.cms.model.port.CmsConfigPort;
import org.chenile.base.exception.BadRequestException;

/**
 * Validates CMS banner operations against cconfig policies.
 */
public class CmsBannerPolicyValidator {

    public static final int BANNER_TITLE_TOO_LONG = 31001;
    public static final int BANNER_NAME_REQUIRED = 31002;

    private final CmsConfigPort configPort;

    public CmsBannerPolicyValidator(CmsConfigPort configPort) {
        this.configPort = configPort;
    }

    public void validateCreate(Banner banner) {
        if (banner.getName() == null || banner.getName().isBlank()) {
            throw new BadRequestException(BANNER_NAME_REQUIRED, new Object[]{});
        }
        if (banner.getTitle() != null && banner.getTitle().length() > configPort.getBannerTitleMaxLength()) {
            throw new BadRequestException(BANNER_TITLE_TOO_LONG, new Object[]{configPort.getBannerTitleMaxLength()});
        }
    }

    public int getMaxBannersPerPosition() {
        return configPort.getMaxBannersPerPosition();
    }
}
