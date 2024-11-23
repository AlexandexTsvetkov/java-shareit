package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class BookingMapper {

    public static Booking mapToBooking(NewBookingRequest request, User booker, Item item) {

        Booking booking = new Booking();
        booking.setStart(request.getStart().atZone(ZoneId.systemDefault()).toInstant());
        booking.setEnd(request.getEnd().atZone(ZoneId.systemDefault()).toInstant());
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        return booking;
    }

    public static BookingDto mapBookingDto(Booking booking, UserDto user, ItemDto item) {

        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setStatus(booking.getStatus());
        dto.setStart(LocalDateTime.ofInstant(booking.getStart(), ZoneId.systemDefault()));
        dto.setEnd(LocalDateTime.ofInstant(booking.getEnd(), ZoneId.systemDefault()));
        dto.setBooker(user);
        dto.setItem(item);

        return dto;
    }

    public static void updateBookingFields(Booking booking, UpdateBookingRequest request) {
        if (request.hasStart()) {
            booking.setStart(request.getStart().atZone(ZoneId.systemDefault()).toInstant());
        }
        if (request.hasEnd()) {
            booking.setEnd(request.getEnd().atZone(ZoneId.systemDefault()).toInstant());
        }
        if (request.hasStatus()) {
            booking.setStatus(request.getStatus());
        }

    }
}
