package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody NewItemDto newItemDto) {
        return itemService.create(userId, newItemDto);
    }

    @GetMapping("/{itemId}")
    public ItemWithLastAndNextBookingsAndCommentsDto get(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        return itemService.get(userId, itemId);
    }

    @GetMapping
    public List<ItemWithLastAndNextBookingsAndCommentsDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllByOwnerId(userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody UpdateItemDto updateItemDto
    ) {
        return itemService.update(userId, itemId, updateItemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> find(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam String text) {
        return itemService.find(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable Long itemId, @RequestBody NewCommentDto newCommentDto) {
        return itemService.createComment(userId, itemId, newCommentDto);
    }
}