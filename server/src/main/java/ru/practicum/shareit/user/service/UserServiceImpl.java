package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DataConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto create(NewUserDto newUserDto) {
        checkEmailForDuplicates(newUserDto.getEmail());

        User createdUser = userRepository.save(userMapper.toUser(newUserDto));
        log.info("User is created: {}", createdUser);
        return userMapper.toDto(createdUser);
    }

    @Override
    public UserDto get(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User doesn't exist on get with id: " + userId));
        log.trace("User is requested by id: {}", user);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto update(Long userId, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User doesn't exist on update with id: " + userId));

        if (updateUserDto.getEmail() != null && !updateUserDto.getEmail().equals(user.getEmail())) {
            checkEmailForDuplicates(updateUserDto.getEmail());
        }

        user = userRepository.save(userMapper.updateUser(user, updateUserDto));
        log.info("User is updated: {}", user);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    private void checkEmailForDuplicates(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DataConflictException("User already exists with email: " + email);
        }
    }
}