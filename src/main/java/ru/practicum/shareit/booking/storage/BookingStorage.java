package ru.practicum.shareit.booking.storage;

import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;
import java.util.Optional;

public interface BookingStorage {

    Collection<Booking> findAll(long userId);

    Booking create(Booking booking);

    Booking update(Booking booking);

    Optional<Booking> getBooking(long id);
}
