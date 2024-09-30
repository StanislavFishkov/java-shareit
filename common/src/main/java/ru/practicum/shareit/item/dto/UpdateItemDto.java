package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.NullOrNotBlank;

@Data
@Builder(toBuilder = true)
public class UpdateItemDto {
    @NullOrNotBlank
    private String name;
    @NullOrNotBlank
    private String description;
    private Boolean available;
}
