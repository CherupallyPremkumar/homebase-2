package com.homebase.ecom.product.domain.port;

import com.homebase.ecom.product.domain.model.AttributeDefinition;
import java.util.List;
import java.util.Optional;

public interface AttributeRepository {
    Optional<AttributeDefinition> findById(String id);
    Optional<AttributeDefinition> findByCode(String code);
    List<AttributeDefinition> findAll();
    List<AttributeDefinition> findAllById(List<String> ids);
    AttributeDefinition save(AttributeDefinition attribute);
    void delete(String id);
    void deleteById(String id);
}
