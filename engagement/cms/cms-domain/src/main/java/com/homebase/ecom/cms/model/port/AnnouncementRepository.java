package com.homebase.ecom.cms.model.port;

import com.homebase.ecom.cms.model.Announcement;

import java.util.List;
import java.util.Optional;

public interface AnnouncementRepository {
    Optional<Announcement> findById(String id);
    List<Announcement> findAllActive();
    List<Announcement> findByType(String announcementType);
    Announcement save(Announcement announcement);
    void delete(String id);
}
