package ru.practicum.shareit.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final String error;
    private List<String> details = new ArrayList<>();
}