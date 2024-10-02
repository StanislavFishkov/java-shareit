package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.NullOrNotBlank;

@Data
@Builder(toBuilder = true)
public class UpdateUserDto {
    @Email
    @NullOrNotBlank
    private String email;
    @NullOrNotBlank
    private String name;
}