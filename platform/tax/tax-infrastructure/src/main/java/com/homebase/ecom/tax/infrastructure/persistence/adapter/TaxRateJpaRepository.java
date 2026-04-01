package com.homebase.ecom.tax.infrastructure.persistence.adapter;

import com.homebase.ecom.tax.infrastructure.persistence.entity.TaxRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TaxRateJpaRepository extends JpaRepository<TaxRateEntity, String> {
    Optional<TaxRateEntity> findByHsnCode(String hsnCode);
    Optional<TaxRateEntity> findByHsnCodeAndActiveTrue(String hsnCode);
    List<TaxRateEntity> findByActiveTrue();
}
