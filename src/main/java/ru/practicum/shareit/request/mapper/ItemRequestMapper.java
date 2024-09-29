package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.dto.ItemEmbeddedInItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemRequestMapper {
    ItemRequestDto toDto(ItemRequest itemRequest);

    List<ItemRequestDto> toDto(List<ItemRequest> itemRequests);

    @Mapping(target = "ownerId", source = "owner.id")
    ItemEmbeddedInItemRequestDto toDto(Item item);

    ItemRequestWithItemsDto toWithItemsDto(ItemRequest itemRequest);

    List<ItemRequestWithItemsDto> toWithItemsDto(List<ItemRequest> itemRequests);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "items", ignore = true)
    ItemRequest toItemRequest(NewItemRequestDto newItemRequestDto, User requester);
}