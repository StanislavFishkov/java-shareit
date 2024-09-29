package ru.practicum.shareit.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public final class DateTimeUtils {
    private DateTimeUtils() {
    }

    public static LocalDateTime currentDateTime() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }
}