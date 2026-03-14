package com.homebase.ecom.returnrequest.domain.port;

import java.util.Optional;
import com.homebase.ecom.returnrequest.model.Returnrequest;

public interface ReturnrequestRepository {
    Optional<Returnrequest> findById(String id);
    void save(Returnrequest returnrequest);
    void delete(String id);
}
