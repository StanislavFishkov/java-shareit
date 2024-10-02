package ru.practicum.shareit.user.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toUser(NewUserDto newUserDto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User updateUser(@MappingTarget User user, UpdateUserDto updateUserDto);
}