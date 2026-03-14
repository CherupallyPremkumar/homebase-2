package com.homebase.ecom.cms.model.port;

import com.homebase.ecom.cms.model.Banner;

import java.util.List;
import java.util.Optional;

public interface BannerRepository {
    Optional<Banner> findById(String id);
    List<Banner> findByPosition(String position);
    List<Banner> findAllActive();
    Banner save(Banner banner);
    void delete(String id);
}
