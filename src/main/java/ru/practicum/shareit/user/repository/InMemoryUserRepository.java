package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long idCounter = 0L;

    @Override
    public User create(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> getById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public User checkAndGetById(long userId) {
        Optional<User> existingUser = getById(userId);
        if (existingUser.isEmpty()) {
            throw new NotFoundException("User doesn't exist with id: " + userId);
        }
        return existingUser.get();
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return users.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(long userId) {
        users.remove(userId);
    }

    private long getNextId() {
        return ++idCounter;
    }
}