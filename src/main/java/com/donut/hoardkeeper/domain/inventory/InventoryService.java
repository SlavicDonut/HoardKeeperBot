package com.donut.hoardkeeper.domain.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryItemRepository inventoryItemRepository;

    @Transactional
    public InventoryItem addItem(String userId, String itemName, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Ilość dodawanego przedmiotu musi być większa niż 0");
        }

        Optional<InventoryItem> existingItem = inventoryItemRepository
                .findByUserIdAndItemNameIgnoreCase(userId, itemName);

        if (existingItem.isPresent()) {
            InventoryItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            return inventoryItemRepository.save(item);
        }

        InventoryItem newItem = InventoryItem.builder()
                .userId(userId)
                .itemName(itemName)
                .quantity(quantity)
                .build();

        return inventoryItemRepository.save(newItem);
    }

    @Transactional(readOnly = true)
    public List<InventoryItem> getUserInventory(String userId) {
        return inventoryItemRepository.findAllByUserId(userId);
    }

}
