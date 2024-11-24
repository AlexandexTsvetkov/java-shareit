package ru.practicum.shareit.item;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }


    public ResponseEntity<Object> findAll(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> addItem(long userId, NewItemDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> updateItem(long userId, UpdateItemDto requestDto, long itemId) {
        return patch("/" + itemId, userId, requestDto);
    }

    public ResponseEntity<Object> getBookingsByOwner(long userId, BookingState state) {
        Map<String, Object> parameters = Map.of(
                "state", state.name()
        );
        return get("/owner?state={state}", userId, parameters);
    }

    public ResponseEntity<Object> findByText(String text) {

        Map<String, Object> parameters = Map.of(
                "text", text
        );

        return get("/search" + "?text={text}", null, parameters);
    }

    public ResponseEntity<Object> getItem(long itemId) {
        return get("/" + itemId);
    }

    public ResponseEntity<Object> addComment(long itemId, long userId, CommentDto comment) {
        return post("/" + itemId + "/comment", userId, comment);
    }

    private void validateDates(LocalDateTime start, LocalDateTime end) {

        if (!start.isBefore(end)) {
            throw new InternalServerException("Дата начала должна быть раньше даты окончания");
        }
    }
}
