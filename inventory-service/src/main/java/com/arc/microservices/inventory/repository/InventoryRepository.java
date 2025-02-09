package com.arc.microservices.inventory.repository;

import com.arc.microservices.inventory.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    boolean existsBySkuCodeAndQuantityIsGreaterThanEqual(String skuCode, Integer quantity);
            //existsBySkuCodeAndQuantityIsGreaterThanEqual
}
