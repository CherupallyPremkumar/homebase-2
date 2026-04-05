package com.homebase.ecom.cms.model.port;

import com.homebase.ecom.cms.model.CmsSchedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CmsScheduleRepository {
    Optional<CmsSchedule> findById(String id);
    List<CmsSchedule> findActiveByPageId(String pageId);
    List<CmsSchedule> findPendingExecution(LocalDateTime now);
    CmsSchedule save(CmsSchedule schedule);
    void delete(String id);
}
