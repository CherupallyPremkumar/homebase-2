package com.homebase.ecom.tax.infrastructure.persistence.adapter;

import com.homebase.ecom.tax.infrastructure.persistence.entity.TaxRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxRateJpaRepository extends JpaRepository<TaxRateEntity, String> {

    List<TaxRateEntity> findByRegionCode(String regionCode);
}
