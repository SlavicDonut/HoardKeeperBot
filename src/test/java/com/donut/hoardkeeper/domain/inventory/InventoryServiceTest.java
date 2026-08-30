package com.donut.hoardkeeper.domain.inventory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private InventoryItemRepository inventoryItemRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    @DisplayName("Powinien dodać nowy przedmiot, gdy ten nie istnieje jeszcze w ekwipunku")
    void shouldAddNewItemWhenItDoesntExist() {

        String userId = "test_user";
        String itemName = "Healing Potion";
        int quantity = 3;

        when(inventoryItemRepository.findByUserIdAndItemNameIgnoreCase(userId, itemName)).thenReturn(Optional.empty());
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InventoryItem result = inventoryService.addItem(userId, itemName, quantity);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getItemName()).isEqualTo(itemName);
        assertThat(result.getQuantity()).isEqualTo(quantity);

        verify(inventoryItemRepository).findByUserIdAndItemNameIgnoreCase(userId, itemName);
        verify(inventoryItemRepository).save(any(InventoryItem.class));
    }

    @Test
    @DisplayName("Powinien zwiększyć ilość istniejącego przedmiotu (stacking)")
    void shouldIncreaseQuantityWhenItemAlreadyExists() {
        // given
        String userId = "test_user";
        String itemName = "Gold Coin";

        InventoryItem existingItem = InventoryItem.builder()
                .id(1L)
                .userId(userId)
                .itemName(itemName)
                .quantity(10)
                .build();

        when(inventoryItemRepository.findByUserIdAndItemNameIgnoreCase(userId, itemName))
                .thenReturn(Optional.of(existingItem));

        when(inventoryItemRepository.save(any(InventoryItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        InventoryItem result = inventoryService.addItem(userId, itemName, 5);

        // then
        assertThat(result.getQuantity()).isEqualTo(15);
        verify(inventoryItemRepository).save(existingItem);
    }

    @Test
    @DisplayName("Powinien rzucić wyjątek, gdy ilość dodawanego przedmiotu wynosi 0 lub mniej")
    void shouldThrowExceptionWhenQuantityIsInvalid() {
        // when & then
        assertThatThrownBy(() -> inventoryService.addItem("test_user", "Sword", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Ilość dodawanego przedmiotu musi być większa niż 0");

        verifyNoInteractions(inventoryItemRepository);
    }

    @Test
    @DisplayName("Powinien zwrócić cały ekwipunek użytkownika")
    void shouldReturnUserInventory() {
        // given
        String userId = "test_user";
        List<InventoryItem> items = List.of(
                InventoryItem.builder().userId(userId).itemName("Item 1").quantity(1).build(),
                InventoryItem.builder().userId(userId).itemName("Item 2").quantity(2).build()
        );

        when(inventoryItemRepository.findAllByUserId(userId)).thenReturn(items);

        // when
        List<InventoryItem> result = inventoryService.getUserInventory(userId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(items);
        verify(inventoryItemRepository).findAllByUserId(userId);
    }
}

