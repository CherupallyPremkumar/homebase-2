package com.homebase.ecom.inventory.configuration.dao;

import com.homebase.ecom.inventory.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  public interface InventoryRepository extends JpaRepository<Inventory,String> {}
