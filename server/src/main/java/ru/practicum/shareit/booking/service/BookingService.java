package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BookingService {

    @Autowired
    private final ItemRepository itemStorage;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BookingRepository bookingStorage;

    @Transactional
    public BookingDto create(NewBookingRequest newBookingRequest, long userId) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Пользователь с id {0, number} не найден", userId));
        }

        Optional<Item> item = itemStorage.findById(newBookingRequest.getItemId());

        if (item.isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Вещь с id {0, number} не найден", newBookingRequest.getItemId()));
        }

        if (!item.get().getAvailable()) {
            throw new InternalServerException(MessageFormat.format("Вещь с id {0, number} не доступна для бронирования", newBookingRequest.getItemId()));
        }

        Booking newBooking = BookingMapper.mapToBooking(newBookingRequest, user.get(), item.get());

        return BookingMapper.mapBookingDto(bookingStorage.save(newBooking), UserMapper.mapToUserDto(user.get()), ItemMapper.mapItemDto(item.get()));
    }

    @Transactional
    public BookingDto approve(long bookingId, long userId, boolean approved) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new InternalServerException(MessageFormat.format("Пользователь с id {0, number} не найден", userId));
        }

        Optional<Booking> optionalUpdatedBooking = bookingStorage.findById(bookingId);

        if (optionalUpdatedBooking.isPresent()) {

            Booking booking = optionalUpdatedBooking.get();

            if (!(booking.getItem().getOwner().equals(user.get()))) {
                throw new InternalServerException("Текущий пользователь не является владельцем вещи");
            }

            if (approved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }

            booking = bookingStorage.save(booking);

            return BookingMapper.mapBookingDto(booking, UserMapper.mapToUserDto(booking.getBooker()), ItemMapper.mapItemDto(booking.getItem()));
        } else {
            throw new NotFoundException(MessageFormat.format("Бронирование с id {0, number} не найден", bookingId));
        }
    }

    public BookingDto findById(long id) {
        Optional<Booking> booking = bookingStorage.findById(id);

        if (booking.isPresent()) {
            return BookingMapper.mapBookingDto(booking.get(), UserMapper.mapToUserDto(booking.get().getBooker()), ItemMapper.mapItemDto(booking.get().getItem()));
        }

        throw new NotFoundException(MessageFormat.format("Бронирование с id {0, number} не найдена", id));
    }

    public Collection<BookingDto> findAllByUser(long userId, State state) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new InternalServerException(MessageFormat.format("Пользователь с id {0, number} не найден", userId));
        }

        List<Booking> bookings;

        if (state == State.CURRENT) {
            bookings = bookingStorage.findCurrentBookingsByBookerIdOrderByStart(userId, Instant.now());
        } else if (state == State.PAST) {
            bookings = bookingStorage.findPastBookingsByBookerIdOrderByStart(userId, Instant.now());
        } else if (state == State.FUTURE) {
            bookings = bookingStorage.findFutureBookingsByBookerIdOrderByStart(userId, Instant.now());
        } else if (state == State.WAITING || state == State.REJECTED) {
            bookings = bookingStorage.findBookingsByBookerIdAndByStatusOrderByStart(userId, BookingStatus.valueOf(state.toString()));
        } else {
            bookings = bookingStorage.findBookingsByBookerIdOrderByStart(userId);
        }

        return bookings.stream()
                .map(booking -> BookingMapper.mapBookingDto(booking, UserMapper.mapToUserDto(booking.getBooker()), ItemMapper.mapItemDto(booking.getItem())))
                .toList();
    }

    public Collection<BookingDto> findAllByOwner(long userId, State state) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new InternalServerException(MessageFormat.format("Пользователь с id {0, number} не найден", userId));
        }

        List<Booking> bookings;

        if (state == State.CURRENT) {
            bookings = bookingStorage.findCurrentBookingsByOwner(userId, Instant.now());
        } else if (state == State.PAST) {
            bookings = bookingStorage.findPastBookingsByOwner(userId, Instant.now());
        } else if (state == State.FUTURE) {
            bookings = bookingStorage.findFutureBookingsByOwner(userId, Instant.now());
        } else if (state == State.WAITING || state == State.REJECTED) {
            bookings = bookingStorage.findBookingsByOwnerAndStatus(userId, BookingStatus.valueOf(state.toString()));
        } else {
            bookings = bookingStorage.findBookingsByOwner(userId);
        }

        return bookings.stream()
                .map(booking -> BookingMapper.mapBookingDto(booking, UserMapper.mapToUserDto(booking.getBooker()), ItemMapper.mapItemDto(booking.getItem())))
                .toList();
    }

    public BookingDto getBooking(long bookingId, long userId) {

        Optional<Booking> booking = bookingStorage.findBookingByIdAndOwnerOrBookerId(bookingId, userId);

        if (booking.isEmpty()) {
            throw new NotFoundException("Бронирование по указанным параметрам не найдено");
        }

        return BookingMapper.mapBookingDto(booking.get(), UserMapper.mapToUserDto(booking.get().getBooker()), ItemMapper.mapItemDto(booking.get().getItem()));
    }
}

