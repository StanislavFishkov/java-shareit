package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private User checkAndGetUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("On Booking operations - User doesn't exist with id: " + userId));
    }

    private Item checkAndGetItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("On Booking operations - Item doesn't exist with id: " + itemId));
    }

    @Override
    @Transactional
    public BookingDto create(Long userId, NewBookingDto newBookingDto) {
        User booker = checkAndGetUserById(userId);

        Item item = checkAndGetItemById(newBookingDto.getItemId());
        if (!item.getAvailable()) throw new ValidationException("Item is not available for booking with id: " +
                newBookingDto.getItemId());

        Booking createdBooking = bookingRepository.save(bookingMapper.toBooking(newBookingDto, item, booker));
        log.info("Booking is created: {}", createdBooking);
        return bookingMapper.toDto(createdBooking);
    }

    @Override
    @Transactional
    public BookingDto approve(Long userId, Long bookingId, Boolean approved) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("On Booking operations - User doesn't exist with id: " + userId));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking doesn't exist on approve with id: " + bookingId));

        if (booking.getStatus() != BookingStatus.WAITING)
            throw new ValidationException(String.format("Booking with id %d can't be approved. Current status is %s",
                    bookingId, booking.getStatus()));

        if (!booking.getItem().getOwner().getId().equals(owner.getId()))
            throw new ValidationException(String.format("Booking with id %d can't be approved by user with id: %d",
                    bookingId, userId));

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        booking = bookingRepository.save(booking);
        log.info("Booking status is changed: {}", booking);

        return bookingMapper.toDto(booking);
    }

    @Override
    public BookingDto get(Long userId, Long bookingId) {
        checkAndGetUserById(userId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking doesn't exist on get with id: " + bookingId));

        if (!(booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId)))
            throw new ValidationException(String.format("Booking with id %d can't be retrieved by user with id: %d",
                bookingId, userId));

        log.trace("Booking is requested by user with id {} by booking id: {}", userId, bookingId);
        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDto> getByBookerId(Long bookerId, BookingState state) {
        checkAndGetUserById(bookerId);

        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findByBookerIdOrderByStartDesc(bookerId);
            case CURRENT -> bookingRepository.findByBookerIdAndCurrentOrderByStartDesc(bookerId, currentTime);
            case PAST -> bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(bookerId, currentTime);
            case FUTURE -> bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(bookerId, currentTime);
            case WAITING -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED);
        };

        log.trace("Bookings are requested by booker with id {} by state: {}", bookerId, state);
        return bookingMapper.toDto(bookings);
    }

    @Override
    public List<BookingDto> getByOwnerId(Long ownerId, BookingState state) {
        checkAndGetUserById(ownerId);

        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findByItemOwnerIdOrderByStartDesc(ownerId);
            case CURRENT -> bookingRepository.findByItemOwnerIdAndCurrentOrderByStartDesc(ownerId, currentTime);
            case PAST -> bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, currentTime);
            case FUTURE -> bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId, currentTime);
            case WAITING -> bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
        };

        log.trace("Bookings are requested by owner with id {} by state: {}", ownerId, state);
        return bookingMapper.toDto(bookings);
    }
}