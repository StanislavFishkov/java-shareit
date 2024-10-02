package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final UserRepository userRepository;

    private User checkAndGetUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("On ItemRequest operations - User doesn't exist with id: " + userId));
    }

    @Override
    @Transactional
    public ItemRequestDto create(Long userId, NewItemRequestDto newItemRequestDto) {
        User requester = checkAndGetUserById(userId);

        ItemRequest createdItem = itemRequestRepository.save(itemRequestMapper.toItemRequest(newItemRequestDto, requester));
        log.info("item is created: {}", createdItem);
        return itemRequestMapper.toDto(createdItem);
    }

    @Override
    public ItemRequestWithItemsDto get(Long userId, Long requestId) {
        checkAndGetUserById(userId);

        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("ItemRequest doesn't exist on get with id: " + requestId));

        log.trace("ItemRequest is requested by user with id {} by itemRequest id: {}", userId, requestId);
        return itemRequestMapper.toWithItemsDto(itemRequest);
    }

    @Override
    public List<ItemRequestWithItemsDto> getAllByRequesterId(Long userId) {
        User requester = checkAndGetUserById(userId);

        List<ItemRequest> itemRequests = itemRequestRepository.findByRequester(requester,
                Sort.by(Sort.Order.desc("created")));

        log.trace("ItemRequests are requested by requester with id: {}", userId);
        return itemRequestMapper.toWithItemsDto(itemRequests);
    }

    @Override
    public List<ItemRequestDto> getAll(Long userId) {
        User requester = checkAndGetUserById(userId);

        List<ItemRequest> itemRequests = itemRequestRepository.findByRequesterNot(requester,
                Sort.by(Sort.Order.desc("created")));

        log.trace("ItemRequests are requested by user with id: {}", userId);
        return itemRequestMapper.toDto(itemRequests);
    }
}
