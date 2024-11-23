package ru.practicum.shareit.booking;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.InternalServerException;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getBookings(long userId, BookingState state) {
        Map<String, Object> parameters = Map.of(
                "state", state.name()
        );
        return get("?state={state}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingsByOwner(long userId, BookingState state) {
        Map<String, Object> parameters = Map.of(
                "state", state.name()
        );
        return get("/owner?state={state}", userId, parameters);
    }

    public ResponseEntity<Object> bookItem(long userId, BookItemRequestDto requestDto) {

        validateDates(requestDto.getStart(), requestDto.getEnd());

        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> approve(long bookingId, long userId, boolean approved) {

        Map<String, Object> parameters = Map.of(
                "approved", approved
        );

        return patch("/" + bookingId + "?approved={approved}", userId, parameters, null);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    private void validateDates(LocalDateTime start, LocalDateTime end) {

        if (!start.isBefore(end)) {
            throw new InternalServerException("Дата начала должна быть раньше даты окончания");
        }
    }
}