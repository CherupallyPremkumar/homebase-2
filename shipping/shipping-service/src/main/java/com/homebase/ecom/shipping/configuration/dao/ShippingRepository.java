package com.homebase.ecom.shipping.configuration.dao;

import com.homebase.ecom.shipping.model.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  public interface ShippingRepository extends JpaRepository<Shipping,String> {}
