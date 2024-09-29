package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemEmbeddedInItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestWithItemsDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemEmbeddedInItemRequestDto> items;
}