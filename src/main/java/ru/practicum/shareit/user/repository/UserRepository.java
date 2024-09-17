package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository {
    User create(User user);

    Optional<User> getById(long userId);

    User checkAndGetById(long userId);

    Optional<User> getByEmail(String email);

    User update(User user);

    void delete(long userId);
}
