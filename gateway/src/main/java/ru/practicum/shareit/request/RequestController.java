package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.NewRequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {

    private final RequestClient requestClient;

    @GetMapping
    public ResponseEntity<Object> findAllByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("Get requests by User, userId={}", userId);
        return requestClient.findAllByUser(userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody NewRequestDto newRequest, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Create new request {}, userId={}", newRequest, userId);
        return requestClient.createRequest(userId, newRequest);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllOtherUsers(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get requests other users, userId={}", userId);
        return requestClient.findAllOtherUsers(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findRequest(@PathVariable long requestId) {
        log.info("Get request by ID {}", requestId);
        return requestClient.findRequest(requestId);
    }
}
