package com.homebase.ecom.order.configuration.dao;

import com.homebase.ecom.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  public interface OrderRepository extends JpaRepository<Order,String> {}
