package com.homebase.ecom.product.domain.port;

import com.homebase.ecom.product.domain.model.MediaAsset;
import java.util.List;
import java.util.Optional;

public interface MediaRepository {
    Optional<MediaAsset> findById(String id);
    List<MediaAsset> findAllByIdIn(List<String> ids);
    void save(MediaAsset asset);
    void delete(String id);
}
