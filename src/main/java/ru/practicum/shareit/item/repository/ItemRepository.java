package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item create(Item item);

    Optional<Item> getById(long itemId);

    Item checkAndGetById(long itemId);

    List<Item> getAllByOwnerId(long userId);

    List<Item> find(String text);

    Item update(Item item);
}