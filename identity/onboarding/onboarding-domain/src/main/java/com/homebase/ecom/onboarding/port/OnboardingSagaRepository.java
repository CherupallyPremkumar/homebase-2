package com.homebase.ecom.onboarding.port;

import com.homebase.ecom.onboarding.model.OnboardingSaga;
import java.util.Optional;

public interface OnboardingSagaRepository {
    Optional<OnboardingSaga> findById(String id);
    Optional<OnboardingSaga> findBySupplierId(String supplierId);
    void save(OnboardingSaga saga);
    void delete(String id);
}
