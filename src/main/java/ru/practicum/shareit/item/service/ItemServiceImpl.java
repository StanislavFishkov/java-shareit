package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    private User checkAndGetUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("On Item operations - User doesn't exist with id: " + userId));
    }

    @Override
    @Transactional
    public ItemDto create(Long userId, NewItemDto newItemDto) {
        User owner = checkAndGetUserById(userId);

        Item createdItem = itemRepository.save(itemMapper.toItem(newItemDto, owner));
        log.info("item is created: {}", createdItem);
        return itemMapper.toDto(createdItem);
    }

    @Override
    public ItemWithLastAndNextBookingsAndCommentsDto get(Long userId, Long itemId) {
        checkAndGetUserById(userId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item doesn't exist on get with id: " + itemId));

        LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        List<Comment> comments = commentRepository.findByItemIdOrderByCreatedDesc(itemId);
        Booking lastBooking = null;
        Booking nextBooking = null;

        if (item.getOwner().getId().equals(userId)) {
            lastBooking = bookingRepository.findTopByItemIdAndEndBeforeOrderByEndDesc(itemId, currentDateTime);
            nextBooking = bookingRepository.findTopByItemIdAndStartAfterOrderByStartAsc(itemId, currentDateTime);
        }

        log.trace("Item is requested by user with id {} by item id: {}", userId, item);
        return itemMapper.toDto(item, comments, lastBooking, nextBooking);
    }

    @Override
    public List<ItemWithLastAndNextBookingsAndCommentsDto> getAllByOwnerId(Long userId) {
        User owner = checkAndGetUserById(userId);

        List<Item> items = itemRepository.findAllByOwner(owner);

        Map<Item, List<Comment>> commentsByItems = commentRepository.findByItemIn(items, Sort.by(Sort.Order.desc("created")))
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));

        Map<Item, List<Booking>> bookingsByItems = bookingRepository.findByItemIn(items)
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));

        LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        List<ItemWithLastAndNextBookingsAndCommentsDto> itemsDto = items.stream()
                .map(item -> itemMapper.toDto(
                        item,
                        commentsByItems.getOrDefault(item, List.of()),
                        bookingsByItems.getOrDefault(item, List.of())
                                .stream()
                                .filter(booking -> booking.getEnd().isBefore(currentDateTime))
                                .max(Comparator.comparing(Booking::getEnd))
                                .orElse(null),
                        bookingsByItems.getOrDefault(item, List.of())
                                .stream()
                                .filter(booking -> booking.getStart().isAfter(currentDateTime))
                                .min(Comparator.comparing(Booking::getStart))
                                .orElse(null))
                )
                .toList();
        log.trace("Items are requested by owner with id: {}", userId);
        return itemsDto;
    }

    @Override
    public List<ItemDto> find(Long userId, String text) {
        checkAndGetUserById(userId);

        List<Item> items = itemRepository.findBySearchText(text);
        log.trace("Items are requested by user with id {} by search text: {}", userId, text);
        return itemMapper.toDto(items);

    }

    @Override
    @Transactional
    public ItemDto update(Long userId, Long itemId, UpdateItemDto updateItemDto) {
        checkAndGetUserById(userId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item doesn't exist on update with id: " + itemId));

        item = itemRepository.save(itemMapper.updateItem(item, updateItemDto));
        log.info("Item is updated: {}", item);
        return itemMapper.toDto(item);
    }

    @Override
    @Transactional
    public CommentDto createComment(Long userId, Long itemId, NewCommentDto newCommentDto) {
        User author = checkAndGetUserById(userId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Create Comment - Item doesn't exist with id: " + itemId));

        LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        List<Booking> previousBookings = bookingRepository.findByItemIdAndBookerIdAndStatusAndEndBefore(itemId, userId,
                BookingStatus.APPROVED, currentDateTime);
        if (previousBookings.isEmpty())
            throw new ValidationException(String.format("Create Comment - User with id %d has never booked item " +
                    "with id %d", userId, itemId));

        Comment createdComment = commentRepository.save(commentMapper.toComment(newCommentDto, item, author, currentDateTime));
        log.info("Comment is created: {}", createdComment);
        return commentMapper.toDto(createdComment);
    }
}