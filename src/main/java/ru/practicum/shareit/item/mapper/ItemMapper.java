package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.booking.dto.BookingEmbeddedInItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, CommentMapper.class})
public interface ItemMapper {
    ItemDto toDto(Item item);

    List<ItemDto> toDto(List<Item> items);

    BookingEmbeddedInItemDto toDto(Booking booking);

    @Mapping(target = "id", source = "item.id")
    ItemWithLastAndNextBookingsAndCommentsDto toDto(Item item, List<Comment> comments, Booking lastBooking, Booking nextBooking);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    Item toItem(NewItemDto newItemDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "newItemDto.name")
    @Mapping(target = "owner", source = "owner")
    Item toItem(NewItemDto newItemDto, User owner);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Item updateItem(@MappingTarget Item item, UpdateItemDto updateItemDto);
}