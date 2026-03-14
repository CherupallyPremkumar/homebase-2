package com.homebase.ecom.returnrequest.infrastructure.persistence.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import com.homebase.ecom.returnrequest.infrastructure.persistence.entity.ReturnrequestEntity;

public interface ReturnrequestJpaRepository extends JpaRepository<ReturnrequestEntity, String> {
}
