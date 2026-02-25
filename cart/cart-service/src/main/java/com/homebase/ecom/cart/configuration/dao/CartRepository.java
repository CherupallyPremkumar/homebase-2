package com.homebase.ecom.cart.configuration.dao;

import com.homebase.ecom.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  public interface CartRepository extends JpaRepository<Cart,String> {}
