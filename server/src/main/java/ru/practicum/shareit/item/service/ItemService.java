package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, NewItemDto newItemDto);

    ItemWithLastAndNextBookingsAndCommentsDto get(Long userId, Long itemId);

    List<ItemWithLastAndNextBookingsAndCommentsDto> getAllByOwnerId(Long userId);

    List<ItemDto> find(Long userId, String text);

    ItemDto update(Long userId, Long itemId, UpdateItemDto updateItemDto);

    CommentDto createComment(Long userId, Long itemId, NewCommentDto newCommentDto);
}