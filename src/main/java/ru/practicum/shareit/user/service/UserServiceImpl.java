package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataConflictException;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto create(NewUserDto newUserDto) {
        checkEmailForDuplicates(newUserDto.getEmail());

        User createdUser = userRepository.create(userMapper.toUser(newUserDto));
        log.info("User is created: {}", createdUser);
        return userMapper.toDto(createdUser);
    }

    @Override
    public UserDto get(long userId) {
        User user = userRepository.checkAndGetById(userId);
        log.trace("User is requested by id: {}", user);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto update(long userId, UpdateUserDto updateUserDto) {
        User user = userRepository.checkAndGetById(userId);

        if (updateUserDto.getEmail() != null && !updateUserDto.getEmail().equals(user.getEmail())) {
            checkEmailForDuplicates(updateUserDto.getEmail());
        }

        user = userRepository.update(userMapper.updateUser(user, updateUserDto));
        log.info("User is updated: {}", user);
        return userMapper.toDto(user);
    }

    @Override
    public void delete(long userId) {
        userRepository.checkAndGetById(userId); // checking existence
        userRepository.delete(userId);
    }

    private void checkEmailForDuplicates(String email) {
        if (userRepository.getByEmail(email).isPresent()) {
            throw new DataConflictException("User already exists with email: " + email);
        }
    }
}