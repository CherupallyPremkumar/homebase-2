package com.homebase.ecom.compliance.port.out;

import com.homebase.ecom.compliance.model.AgreementAcceptance;
import java.util.List;
import java.util.Optional;

public interface AgreementAcceptanceRepository {
    AgreementAcceptance save(AgreementAcceptance acceptance);
    Optional<AgreementAcceptance> findByUserIdAndAgreementId(String userId, String agreementId);
    List<AgreementAcceptance> findByAgreementId(String agreementId);
    List<AgreementAcceptance> findByUserId(String userId);
}
