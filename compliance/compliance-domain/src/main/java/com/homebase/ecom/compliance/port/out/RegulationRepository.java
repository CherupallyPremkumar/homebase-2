package com.homebase.ecom.compliance.port.out;

import com.homebase.ecom.compliance.model.Regulation;
import java.util.List;
import java.util.Optional;

public interface RegulationRepository {
    Optional<Regulation> findById(String id);
    List<Regulation> findByJurisdiction(String jurisdiction);
    List<Regulation> findActive();
    Regulation save(Regulation regulation);
}
