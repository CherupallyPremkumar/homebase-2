package com.homebase.ecom.cms.service;

import com.homebase.ecom.cms.dto.request.CreateMediaRequest;
import com.homebase.ecom.cms.dto.response.MediaResult;

public interface CmsMediaService {
    MediaResult create(CreateMediaRequest media);
    MediaResult retrieve(String id);
    void delete(String id);
}
