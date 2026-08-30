package com.donut.hoardkeeper.domain.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    List<InventoryItem> findByUserId(String userId);

    Optional<InventoryItem> findByUserIdAndItemNameIgnoreCase(String userId, String itemName);
}
