package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(long userId, NewItemDto newItemDto);

    ItemDto get(long userId, long itemId);

    List<ItemDto> getAllByOwnerId(long userId);

    List<ItemDto> find(long userId, String text);

    ItemDto update(long userId, long itemId, UpdateItemDto updateItemDto);
}