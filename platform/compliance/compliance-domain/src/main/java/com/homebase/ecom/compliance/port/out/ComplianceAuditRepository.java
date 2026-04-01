package com.homebase.ecom.compliance.port.out;

import com.homebase.ecom.compliance.model.ComplianceAudit;
import java.util.List;

public interface ComplianceAuditRepository {
    ComplianceAudit save(ComplianceAudit audit);
    List<ComplianceAudit> findByRegulationId(String regulationId);
}
