package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto create(long userId, NewItemDto newItemDto) {
        User owner = userRepository.checkAndGetById(userId);

        Item createdItem = itemRepository.create(itemMapper.toItem(newItemDto, owner));
        log.info("item is created: {}", createdItem);
        return itemMapper.toDto(createdItem);
    }

    @Override
    public ItemDto get(long userId, long itemId) {
        userRepository.checkAndGetById(userId); // check requesting user existence

        Item item = itemRepository.checkAndGetById(itemId);
        log.trace("Item is requested by user with id {} by item id: {}", userId, item);
        return itemMapper.toDto(item);
    }

    @Override
    public List<ItemDto> getAllByOwnerId(long userId) {
        userRepository.checkAndGetById(userId); // check requesting user existence

        List<Item> items = itemRepository.getAllByOwnerId(userId);
        log.trace("Items are requested by owner with id: {}", userId);
        return itemMapper.toDto(items);
    }

    @Override
    public List<ItemDto> find(long userId, String text) {
        userRepository.checkAndGetById(userId); // check requesting user existence

        List<Item> items = itemRepository.find(text);
        log.trace("Items are requested by user with id {} by search text: {}", userId, text);
        return itemMapper.toDto(items);

    }

    @Override
    public ItemDto update(long userId, long itemId, UpdateItemDto updateItemDto) {
        userRepository.checkAndGetById(userId); // check requesting user existence

        Item item = itemRepository.checkAndGetById(itemId);

        item = itemRepository.update(itemMapper.updateItem(item, updateItemDto));
        log.info("Item is updated: {}", item);
        return itemMapper.toDto(item);
    }
}