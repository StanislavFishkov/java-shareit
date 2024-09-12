package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long idCounter = 0L;

    @Override
    public Item create(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> getById(long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Item checkAndGetById(long itemId) {
        Optional<Item> existingItem = getById(itemId);
        if (existingItem.isEmpty()) {
            throw new NotFoundException("item doesn't exist with id: " + itemId);
        }
        return existingItem.get();
    }

    @Override
    public List<Item> getAllByOwnerId(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .toList();
    }

    @Override
    public List<Item> find(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        final String textLowerCase = text.toLowerCase();
        return items.values().stream()
                .filter(item -> item.getAvailable() && (item.getName().toLowerCase().contains(textLowerCase)
                        || item.getDescription().toLowerCase().contains(textLowerCase)))
                .toList();
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    private long getNextId() {
        return ++idCounter;
    }
}
