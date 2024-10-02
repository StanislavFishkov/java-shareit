package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody NewItemRequestDto newItemRequestDto) {
        return itemRequestService.create(userId, newItemRequestDto);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithItemsDto get(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long requestId) {
        return itemRequestService.get(userId, requestId);
    }

    @GetMapping
    public List<ItemRequestWithItemsDto> getAllByRequesterId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getAllByRequesterId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getAll(userId);
    }
}