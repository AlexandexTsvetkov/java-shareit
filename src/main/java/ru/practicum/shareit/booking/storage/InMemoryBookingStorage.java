package ru.practicum.shareit.booking.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Qualifier("InMemoryBookingStorage")
public class InMemoryBookingStorage implements BookingStorage {

    private final Map<Long, Booking> bookings = new HashMap<>();
    private long counter = 0L;

    @Override
    public Collection<Booking> findAll(long userId) {
        return bookings.values().stream()
                .filter(booking -> booking.getBooker().getId() == userId).toList();
    }

    @Override
    public Booking create(Booking booking) {

        booking.setId(getNextId());

        bookings.put(booking.getId(), booking);

        return booking;
    }

    @Override
    public Booking update(Booking booking) {

        long id = booking.getId();

        bookings.put(id, booking);

        return booking;
    }

    @Override
    public Optional<Booking> getBooking(long id) {

        Booking booking = bookings.get(id);
        return (booking == null) ? Optional.empty() : Optional.of(booking);
    }

    private long getNextId() {
        return ++counter;
    }
}