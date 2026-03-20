package com.homebase.ecom.compliance.port.out;

import com.homebase.ecom.compliance.model.ComplianceMapping;
import java.util.List;

public interface ComplianceMappingRepository {
    ComplianceMapping save(ComplianceMapping mapping);
    List<ComplianceMapping> findByRegulationId(String regulationId);
    List<ComplianceMapping> findByTargetId(String targetId);
}
