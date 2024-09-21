package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto create(NewUserDto newUserDto);

    UserDto get(Long userId);

    UserDto update(Long userId, UpdateUserDto updateUserDto);

    void delete(Long userId);
}