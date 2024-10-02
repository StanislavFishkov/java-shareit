package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody NewUserDto newUserDto) {
        return userClient.create(newUserDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> get(@PathVariable Long userId) {
        return userClient.get(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable Long userId, @Valid @RequestBody UpdateUserDto updateUserDto) {
        return userClient.update(userId, updateUserDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> remove(@PathVariable Long userId) {
        return userClient.delete(userId);
    }
}