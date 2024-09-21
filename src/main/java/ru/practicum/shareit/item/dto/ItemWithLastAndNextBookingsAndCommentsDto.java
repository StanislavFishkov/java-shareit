package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingEmbeddedInItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Data
public class ItemWithLastAndNextBookingsAndCommentsDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private UserDto owner;
    private BookingEmbeddedInItemDto lastBooking;
    private BookingEmbeddedInItemDto nextBooking;
    private List<CommentDto> comments;
}