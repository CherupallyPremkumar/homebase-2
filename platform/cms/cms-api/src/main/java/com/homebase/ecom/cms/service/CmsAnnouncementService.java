package com.homebase.ecom.cms.service;

import com.homebase.ecom.cms.dto.request.CreateAnnouncementRequest;
import com.homebase.ecom.cms.dto.response.AnnouncementResult;

public interface CmsAnnouncementService {
    AnnouncementResult create(CreateAnnouncementRequest announcement);
    AnnouncementResult retrieve(String id);
    void delete(String id);
}
