package com.homebase.ecom.rulesengine.infrastructure.persistence.repository;

import com.homebase.ecom.rulesengine.infrastructure.persistence.entity.DecisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DecisionJpaRepository extends JpaRepository<DecisionEntity, String> {
    List<DecisionEntity> findByRuleSetId(String ruleSetId);
}
