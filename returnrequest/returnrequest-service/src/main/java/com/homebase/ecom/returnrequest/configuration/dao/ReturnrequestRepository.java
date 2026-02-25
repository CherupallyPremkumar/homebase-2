package com.homebase.ecom.returnrequest.configuration.dao;

import com.homebase.ecom.returnrequest.model.Returnrequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  public interface ReturnrequestRepository extends JpaRepository<Returnrequest,String> {}
