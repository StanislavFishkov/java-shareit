package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.NewBookingDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingClient bookingClient;

    @Autowired
    private MockMvc mvc;

    private static final String API_PREFIX = "/bookings";
    private final LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

    @Test
    @SneakyThrows
    void bookItem_whenValidRequest_thenStatusOkAndMethodInvoked() {
        // Given
        long userId = 23L;
        NewBookingDto newBookingDto = NewBookingDto.builder()
                .start(currentDateTime.plusDays(1))
                .end(currentDateTime.plusDays(2))
                .itemId(1L)
                .build();

        // When
        mvc.perform(post(API_PREFIX)
                        .content(mapper.writeValueAsString(newBookingDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk());

        Mockito.verify(bookingClient).bookItem(userId, newBookingDto);
        Mockito.verifyNoMoreInteractions(bookingClient);
    }

    @Test
    @SneakyThrows
    void bookItem_whenStartInPastOrNull_thenStatusBadRequest() {
        // Given
        NewBookingDto newBookingDtoStartNull = NewBookingDto.builder()
                .end(currentDateTime.plusDays(1))
                .itemId(1L)
                .build();

        NewBookingDto newBookingDto = NewBookingDto.builder()
                .start(currentDateTime.minusDays(1))
                .end(currentDateTime.plusDays(1))
                .itemId(1L)
                .build();

        // When
        mvc.perform(post(API_PREFIX)
                        .content(mapper.writeValueAsString(newBookingDtoStartNull))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isBadRequest());

        // When
        mvc.perform(post(API_PREFIX)
                        .content(mapper.writeValueAsString(newBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(bookingClient);
    }

    @Test
    @SneakyThrows
    void bookItem_whenIncorrectDateTimeRange_thenStatusBadRequest() {
        // Given
        NewBookingDto newBookingDto = NewBookingDto.builder()
                .start(currentDateTime.plusDays(2))
                .end(currentDateTime.plusDays(1))
                .itemId(1L)
                .build();

        // When
        mvc.perform(post(API_PREFIX)
                        .content(mapper.writeValueAsString(newBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(bookingClient);
    }

    @Test
    @SneakyThrows
    void bookItem_whenItemIdNull_thenStatusBadRequest() {
        // Given
        NewBookingDto newBookingDto = NewBookingDto.builder()
                .start(currentDateTime.plusDays(1))
                .end(currentDateTime.plusDays(2))
                .itemId(1L)
                .build();

        // When
        mvc.perform(post(API_PREFIX)
                        .content(mapper.writeValueAsString(newBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(bookingClient);
    }
}