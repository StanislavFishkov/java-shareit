package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import ru.practicum.shareit.validation.NullOrNotBlank;

@Data
public class UpdateUserDto {
    @Email
    @NullOrNotBlank
    private String email;
    @NullOrNotBlank
    private String name;
}