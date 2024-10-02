package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    BookingDto create(Long userId, NewBookingDto newBookingDto);

    BookingDto approve(Long userId, Long bookingId, Boolean approved);

    BookingDto get(Long userId, Long bookingId);

    List<BookingDto> getByBookerId(Long bookerId, BookingState state);

    List<BookingDto> getByOwnerId(Long ownerId, BookingState state);
}