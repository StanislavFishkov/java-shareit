package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody NewUserDto newUserDto) {
        return userService.create(newUserDto);
    }

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable long userId) {
        return userService.get(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable long userId, @Valid @RequestBody UpdateUserDto updateUserDto) {
        return userService.update(userId, updateUserDto);
    }

    @DeleteMapping("/{userId}")
    public void remove(@PathVariable long userId) {
        userService.delete(userId);
    }
}