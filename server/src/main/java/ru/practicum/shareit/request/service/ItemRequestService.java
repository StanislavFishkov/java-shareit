package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(Long userId, NewItemRequestDto newItemRequestDto);

    ItemRequestWithItemsDto get(Long userId, Long requestId);

    List<ItemRequestWithItemsDto> getAllByRequesterId(Long userId);

    List<ItemRequestDto> getAll(Long userId);
}