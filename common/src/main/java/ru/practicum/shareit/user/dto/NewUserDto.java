package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class NewUserDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String name;
}