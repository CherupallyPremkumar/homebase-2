package com.homebase.ecom.cms.service;

import com.homebase.ecom.cms.dto.request.CreateBlockRequest;
import com.homebase.ecom.cms.dto.response.BlockResult;

import java.util.List;

public interface CmsBlockService {
    BlockResult create(CreateBlockRequest block);
    BlockResult retrieve(String id);
    BlockResult update(String id, CreateBlockRequest block);
    void delete(String id);
}
