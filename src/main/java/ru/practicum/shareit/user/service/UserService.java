package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto create(NewUserDto newUserDto);

    UserDto get(long userId);

    UserDto update(long userId, UpdateUserDto updateUserDto);

    void delete(long userId);
}