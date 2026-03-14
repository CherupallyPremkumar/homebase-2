package com.homebase.ecom.product.domain.port;

import com.homebase.ecom.product.domain.model.MediaAsset;
import java.util.Optional;

public interface MediaRepository {
    Optional<MediaAsset> findById(String id);
    void save(MediaAsset asset);
    void delete(String id);
}
