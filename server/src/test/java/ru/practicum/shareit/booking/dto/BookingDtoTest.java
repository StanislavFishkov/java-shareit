package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDtoTest {

    private final JacksonTester<BookingDto> json;

    @Test
    void testBookingDto() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .booker(UserDto.builder()
                                .id(2L)
                                .name("John Booker")
                                .email("john.booker@mail.com")
                                .build()
                )
                .item(ItemDto.builder()
                        .id(3L)
                        .owner(UserDto.builder()
                                .id(4L)
                                .name("Michael Owner")
                                .email("michael.owner@mail.com")
                                .build())
                        .name("item")
                        .description("description")
                        .available(true)
                        .build()
                )
                .start(LocalDateTime.of(2024, 9, 29, 0, 0))
                .end(LocalDateTime.of(2024, 9, 30, 12, 35, 57))
                .status(BookingStatus.APPROVED)
                .build();

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(bookingDto.getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.booker.id")
                .isEqualTo(bookingDto.getBooker().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.booker.name")
                .isEqualTo(bookingDto.getBooker().getName());
        assertThat(result).extractingJsonPathStringValue("$.booker.email")
                .isEqualTo(bookingDto.getBooker().getEmail());
        assertThat(result).extractingJsonPathNumberValue("$.item.id")
                .isEqualTo(bookingDto.getItem().getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.item.owner.id")
                .isEqualTo(bookingDto.getItem().getOwner().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.item.owner.name")
                .isEqualTo(bookingDto.getItem().getOwner().getName());
        assertThat(result).extractingJsonPathStringValue("$.item.owner.email")
                .isEqualTo(bookingDto.getItem().getOwner().getEmail());
        assertThat(result).extractingJsonPathStringValue("$.item.name")
                .isEqualTo(bookingDto.getItem().getName());
        assertThat(result).extractingJsonPathStringValue("$.item.description")
                .isEqualTo(bookingDto.getItem().getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.item.available")
                .isEqualTo(bookingDto.getItem().getAvailable());
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(bookingDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(bookingDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(bookingDto.getStatus().name());
    }
}