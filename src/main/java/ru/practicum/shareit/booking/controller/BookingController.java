package ru.practicum.shareit.booking.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.InternalServerException;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/booking")
@AllArgsConstructor
public class BookingController {

    @Autowired
    BookingService bookingService;

    @GetMapping("/sent")
    public Collection<BookingDto> findAll(@RequestHeader HttpHeaders headers) {

        log.info("пришел Get запрос /booking/sent");
        String textUserId = headers.getFirst("X-Sharer-User-Id");

        if (textUserId == null) {
            throw (new InternalServerException("Не передано значение заголовка X-Sharer-User-Id"));
        }
        Collection<BookingDto> items = bookingService.findAll(Long.parseLong(textUserId));
        log.info("Отправлен ответ Get /booking/sent", items);
        return items;
    }

    @GetMapping("/{id}")
    public BookingDto findById(@PathVariable long id) {

        log.info("пришел Get запрос booking/{}", id);
        BookingDto bookingDto = bookingService.findById(id);
        log.info("Отправлен ответ Get /booking с телом: {}", bookingDto);
        return bookingDto;
    }

    @PatchMapping("/{id}")
    public BookingDto update(@Valid @RequestBody UpdateBookingRequest updateBookingRequest, @PathVariable long id, @RequestHeader HttpHeaders headers) {

        log.info("пришел PATCH запрос /booking с телом: {}", updateBookingRequest);

        String textUserId = headers.getFirst("X-Sharer-User-Id");

        if (textUserId == null) {
            throw (new InternalServerException("Не передано значение заголовка X-Sharer-User-Id"));
        }
        BookingDto bookingDto = bookingService.update(updateBookingRequest, Long.parseLong(textUserId), id);
        log.info("Отправлен ответ PATCH /booking с телом: {}", bookingDto);
        return bookingDto;
    }

    @PostMapping
    public BookingDto create(@Valid @RequestBody NewBookingRequest newBookingRequest, @RequestHeader HttpHeaders headers) {

        log.info("пришел Post запрос /booking с телом: {}", newBookingRequest);

        String textUserId = headers.getFirst("x-sharer-user-id");

        if (textUserId == null) {
            throw (new InternalServerException("Не передано значение заголовка X-Sharer-User-Id"));
        }

        BookingDto newBooking = bookingService.create(newBookingRequest, Long.parseLong(textUserId));
        log.info("Отправлен ответ Post /booking с телом: {}", newBooking);
        return newBooking;
    }
}