package com.homebase.ecom.rulesengine.infrastructure.persistence.adapter;

import com.homebase.ecom.rulesengine.infrastructure.persistence.entity.FactDefinitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactDefinitionJpaRepository extends JpaRepository<FactDefinitionEntity, String> {
    List<FactDefinitionEntity> findByModule(String module);
}
