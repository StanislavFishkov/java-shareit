package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.validation.DateTimeRange;

import java.time.LocalDateTime;

@Data
@DateTimeRange(before = "start", after = "end")
public class NewBookingDto {
    @NotNull
    @Future
    private LocalDateTime start;
    @NotNull
    @Future
    private LocalDateTime end;
    @NotNull
    private Long itemId;
}