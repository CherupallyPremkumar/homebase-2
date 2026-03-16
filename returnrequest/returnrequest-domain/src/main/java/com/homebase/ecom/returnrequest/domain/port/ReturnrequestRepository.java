package com.homebase.ecom.returnrequest.domain.port;

import java.util.Optional;
import java.util.List;
import com.homebase.ecom.returnrequest.model.Returnrequest;

public interface ReturnrequestRepository {
    Optional<Returnrequest> findById(String id);
    List<Returnrequest> findByOrderId(String orderId);
    List<Returnrequest> findByCustomerId(String customerId);
    void save(Returnrequest returnrequest);
    void delete(String id);
}
