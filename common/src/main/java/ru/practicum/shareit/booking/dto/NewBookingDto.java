package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.DateTimeRange;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
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