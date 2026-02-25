package com.homebase.ecom.supplierproduct.configuration.dao;

import com.homebase.ecom.supplierproduct.model.Supplierproduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  public interface SupplierproductRepository extends JpaRepository<Supplierproduct,String> {}
