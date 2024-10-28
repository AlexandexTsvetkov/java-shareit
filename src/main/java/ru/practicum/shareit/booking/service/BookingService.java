package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookingService {

    @Autowired
    private final ItemStorage itemStorage;

    @Autowired
    private final UserStorage userStorage;

    @Autowired
    private final BookingStorage bookingStorage;

    public BookingDto create(NewBookingRequest newBookingRequest, long userId) {

        Optional<User> user = userStorage.getUser(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Пользователь с id {0, number} не найден", userId));
        }

        Optional<Item> item = itemStorage.getItem(newBookingRequest.getItemId());

        if (item.isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Вещь с id {0, number} не найден", newBookingRequest.getItemId()));
        }

        validateDates(newBookingRequest.getStart(), newBookingRequest.getEnd());

        newBookingRequest.setBooker(user.get());
        Booking newBooking = BookingMapper.mapToBooking(newBookingRequest, user.get(), item.get());

        return BookingMapper.mapBookingDto(bookingStorage.create(newBooking));
    }

    public BookingDto update(UpdateBookingRequest updateBookingRequest, long userId, long id) {

        Optional<User> user = userStorage.getUser(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Пользователь с id {0, number} не найден", userId));
        }

        Optional<Booking> optionalUpdatedBooking = bookingStorage.getBooking(id);

        if (optionalUpdatedBooking.isPresent()) {

            Booking booking = optionalUpdatedBooking.get();

            if (!(booking.getBooker().equals(user.get())) && !(booking.getItem().getOwner().equals(user.get()))) {
                throw new InternalServerException("Текущий пользователь не является владельцем вещи или инициатором бронирования");
            }

            LocalDateTime start;
            LocalDateTime end;

            if (updateBookingRequest.hasStart()) {
                start = updateBookingRequest.getStart();
            } else {
                start = booking.getStart();
            }

            if (updateBookingRequest.hasEnd()) {
                end = updateBookingRequest.getEnd();
            } else {
                end = booking.getEnd();
            }

            validateDates(start, end);

            BookingMapper.updateBookingFields(booking, updateBookingRequest);

            Booking updatedBooking = bookingStorage.update(booking);
            return BookingMapper.mapBookingDto(updatedBooking);
        } else {
            throw new NotFoundException(MessageFormat.format("Бронирование с id {0, number} не найден", id));
        }
    }

    public BookingDto findById(long id) {
        Optional<Booking> booking = bookingStorage.getBooking(id);

        if (booking.isPresent()) {
            return BookingMapper.mapBookingDto(booking.get());
        }

        throw new NotFoundException(MessageFormat.format("Бронирование с id {0, number} не найдена", id));
    }

    public Collection<BookingDto> findAll(long userId) {

        Optional<User> user = userStorage.getUser(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Пользователь с id {0, number} не найден", userId));
        }

        return bookingStorage.findAll(userId).stream()
                .map(BookingMapper::mapBookingDto)
                .toList();
    }

    private void validateDates(LocalDateTime start, LocalDateTime end) {

        if (!start.isBefore(end)) {
            throw new InternalServerException("Дата начала должна быть раньше даты окончания");
        }
    }
}

