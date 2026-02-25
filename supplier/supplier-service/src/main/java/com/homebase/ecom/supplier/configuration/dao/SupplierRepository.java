package com.homebase.ecom.supplier.configuration.dao;

import com.homebase.ecom.supplier.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  public interface SupplierRepository extends JpaRepository<Supplier,String> {}
