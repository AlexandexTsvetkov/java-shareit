package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {

    public static Booking mapToBooking(NewBookingRequest request, User booker, Item item) {

        Booking booking = new Booking();
        booking.setStart(request.getStart());
        booking.setEnd(request.getEnd());
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        return booking;
    }

    public static BookingDto mapBookingDto(Booking booking) {

        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setStatus(booking.getStatus());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());

        return dto;
    }

    public static void updateBookingFields(Booking booking, UpdateBookingRequest request) {
        if (request.hasStart()) {
            booking.setStart(request.getStart());
        }
        if (request.hasEnd()) {
            booking.setEnd(request.getEnd());
        }
        if (request.hasStatus()) {
            booking.setStatus(request.getStatus());
        }

    }
}
