package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/bookings")
@AllArgsConstructor
public class BookingController {

    @Autowired
    private final BookingService bookingService;

    @GetMapping
    public Collection<BookingDto> findAll(@RequestHeader("X-Sharer-User-Id") long userId
            ,@RequestParam(defaultValue = "ALL") State state) {

        log.info("пришел Get запрос /bookings");
        Collection<BookingDto> bookings = bookingService.findAllByUser(userId, state);
        log.info("Отправлен ответ Get /items с телом: {}", bookings);
        return bookings;
    }

    @GetMapping("/owner")
    public Collection<BookingDto> findAllByOwner(@RequestHeader("X-Sharer-User-Id") long userId
            ,@RequestParam(defaultValue = "ALL") State state) {

        log.info("пришел Get запрос /bookings/owner");
        Collection<BookingDto> bookings = bookingService.findAllByOwner(userId, state);
        log.info("Отправлен ответ Get /bookings/owner с телом: {}", bookings);
        return bookings;
    }

    @PostMapping
    public BookingDto create(@Valid @RequestBody NewBookingRequest newBookingRequest
            , @RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("пришел Post запрос /bookings с телом: {}", newBookingRequest);

        BookingDto newBooking = bookingService.create(newBookingRequest, userId);
        log.info("Отправлен ответ Post /bookings с телом: {}", newBooking);
        return newBooking;
    }

    @GetMapping("/{bookingId}")
    public BookingDto findBooking(@PathVariable long bookingId, @RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("пришел Get запрос bookings/{}", bookingId);
        BookingDto bookingDto = bookingService.getBooking(bookingId, userId);
        log.info("Отправлен ответ Get с телом: {}", bookingDto);
        return bookingDto;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@PathVariable long bookingId, @RequestHeader("X-Sharer-User-Id") long userId
                                ,@RequestParam(name = "approved") @NotNull Boolean approved) {

        log.info("пришел PATCH запрос /bookings/{}?approved={}", bookingId, approved);
        BookingDto booking = bookingService.approve(bookingId, userId, approved);
        log.info("Отправлен ответ PATCH /bookings с телом: {}", booking);
        return booking;
    }
}