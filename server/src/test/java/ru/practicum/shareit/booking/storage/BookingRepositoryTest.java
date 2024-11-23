package ru.practicum.shareit.booking.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    public void findBookingsByBookerIdOrderByStartTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        User user2 = new User();
        user2.setName("Alexander2");
        user2.setEmail("alexande2r@gmail.com");

        User newUser2 = userRepository.save(user2);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");

        Item newItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(newItem);
        booking.setBooker(newUser2);
        booking.setStart(Instant.now().plusSeconds(30));
        booking.setEnd(Instant.now().plusSeconds(60));
        booking.setStatus(BookingStatus.APPROVED);

        bookingRepository.save(booking);

        assertEquals(bookingRepository.findBookingsByBookerIdOrderByStart(newUser2.getId()).size(), 1);
    }

    @Test
    public void findCurrentBookingsByBookerIdOrderByStartTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        User user2 = new User();
        user2.setName("Alexander2");
        user2.setEmail("alexande2r@gmail.com");

        User newUser2 = userRepository.save(user2);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");

        Item newItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(newItem);
        booking.setBooker(newUser2);
        booking.setStart(Instant.now().plusSeconds(30));
        booking.setEnd(Instant.now().plusSeconds(60));
        booking.setStatus(BookingStatus.APPROVED);

        bookingRepository.save(booking);

        assertEquals(bookingRepository.findCurrentBookingsByBookerIdOrderByStart(newUser2.getId(), Instant.now().plusSeconds(45)).size(), 1);
    }

    @Test
    public void findPastBookingsByBookerIdOrderByStartTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        User user2 = new User();
        user2.setName("Alexander2");
        user2.setEmail("alexande2r@gmail.com");

        User newUser2 = userRepository.save(user2);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");

        Item newItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(newItem);
        booking.setBooker(newUser2);
        booking.setStart(Instant.now().plusSeconds(30));
        booking.setEnd(Instant.now().plusSeconds(60));
        booking.setStatus(BookingStatus.APPROVED);

        bookingRepository.save(booking);

        assertEquals(bookingRepository.findPastBookingsByBookerIdOrderByStart(newUser2.getId(), Instant.now().plusSeconds(70)).size(), 1);
    }

    @Test
    public void findBookingsByBookerIdAndByStatusOrderByStartTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        User user2 = new User();
        user2.setName("Alexander2");
        user2.setEmail("alexande2r@gmail.com");

        User newUser2 = userRepository.save(user2);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");

        Item newItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(newItem);
        booking.setBooker(newUser2);
        booking.setStart(Instant.now().plusSeconds(30));
        booking.setEnd(Instant.now().plusSeconds(60));
        booking.setStatus(BookingStatus.APPROVED);

        bookingRepository.save(booking);

        assertEquals(bookingRepository.findBookingsByBookerIdAndByStatusOrderByStart(newUser2.getId(), BookingStatus.APPROVED).size(), 1);
    }

    @Test
    public void findFutureBookingsByBookerIdOrderByStartTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        User user2 = new User();
        user2.setName("Alexander2");
        user2.setEmail("alexande2r@gmail.com");

        User newUser2 = userRepository.save(user2);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");

        Item newItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(newItem);
        booking.setBooker(newUser2);
        booking.setStart(Instant.now().plusSeconds(30));
        booking.setEnd(Instant.now().plusSeconds(60));
        booking.setStatus(BookingStatus.APPROVED);

        bookingRepository.save(booking);

        assertEquals(bookingRepository.findFutureBookingsByBookerIdOrderByStart(newUser2.getId(), Instant.now()).size(), 1);
    }

    @Test
    public void findBookingByIdAndOwnerOrBookerIdTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        User user2 = new User();
        user2.setName("Alexander2");
        user2.setEmail("alexande2r@gmail.com");

        User newUser2 = userRepository.save(user2);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");

        Item newItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(newItem);
        booking.setBooker(newUser2);
        booking.setStart(Instant.now().plusSeconds(30));
        booking.setEnd(Instant.now().plusSeconds(60));
        booking.setStatus(BookingStatus.APPROVED);

        Booking newBooking = bookingRepository.save(booking);

        assertTrue(bookingRepository.findBookingByIdAndOwnerOrBookerId(newBooking.getId(), user.getId()).isPresent());
    }

    @Test
    public void findBookingsByOwnerTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        User user2 = new User();
        user2.setName("Alexander2");
        user2.setEmail("alexande2r@gmail.com");

        User newUser2 = userRepository.save(user2);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");

        Item newItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(newItem);
        booking.setBooker(newUser2);
        booking.setStart(Instant.now().plusSeconds(30));
        booking.setEnd(Instant.now().plusSeconds(60));
        booking.setStatus(BookingStatus.APPROVED);

        Booking newBooking = bookingRepository.save(booking);

        assertEquals(bookingRepository.findBookingsByOwner(user.getId()).size(), 1);
    }

    @Test
    public void findLastAndNextBookingsTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        User user2 = new User();
        user2.setName("Alexander2");
        user2.setEmail("alexande2r@gmail.com");

        User newUser2 = userRepository.save(user2);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");

        Item newItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(newItem);
        booking.setBooker(newUser2);
        booking.setStart(Instant.now().plusSeconds(30));
        booking.setEnd(Instant.now().plusSeconds(60));
        booking.setStatus(BookingStatus.APPROVED);

        Booking newBooking = bookingRepository.save(booking);

        assertEquals(bookingRepository.findLastAndNextBookings(user.getId(), Instant.now()).size(), 1);
    }

    @Test
    public void findFutureBookingsByOwnerTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        User user2 = new User();
        user2.setName("Alexander2");
        user2.setEmail("alexande2r@gmail.com");

        User newUser2 = userRepository.save(user2);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");

        Item newItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(newItem);
        booking.setBooker(newUser2);
        booking.setStart(Instant.now().plusSeconds(30));
        booking.setEnd(Instant.now().plusSeconds(60));
        booking.setStatus(BookingStatus.APPROVED);

        Booking newBooking = bookingRepository.save(booking);

        assertEquals(bookingRepository.findFutureBookingsByOwner(user.getId(), Instant.now()).size(), 1);
    }

    @Test
    public void findCurrentBookingsByOwnerTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        User user2 = new User();
        user2.setName("Alexander2");
        user2.setEmail("alexande2r@gmail.com");

        User newUser2 = userRepository.save(user2);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");

        Item newItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(newItem);
        booking.setBooker(newUser2);
        booking.setStart(Instant.now().plusSeconds(30));
        booking.setEnd(Instant.now().plusSeconds(60));
        booking.setStatus(BookingStatus.APPROVED);

        Booking newBooking = bookingRepository.save(booking);

        assertEquals(bookingRepository.findCurrentBookingsByOwner(user.getId(), Instant.now().plusSeconds(45)).size(), 1);
    }

    @Test
    public void findPastBookingsByOwnerTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        User user2 = new User();
        user2.setName("Alexander2");
        user2.setEmail("alexande2r@gmail.com");

        User newUser2 = userRepository.save(user2);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");

        Item newItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(newItem);
        booking.setBooker(newUser2);
        booking.setStart(Instant.now().plusSeconds(30));
        booking.setEnd(Instant.now().plusSeconds(60));
        booking.setStatus(BookingStatus.APPROVED);

        Booking newBooking = bookingRepository.save(booking);

        assertEquals(bookingRepository.findPastBookingsByOwner(user.getId(), Instant.now().plusSeconds(70)).size(), 1);
    }

    @Test
    public void findBookingsByOwnerAndStatus() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        User user2 = new User();
        user2.setName("Alexander2");
        user2.setEmail("alexande2r@gmail.com");

        User newUser2 = userRepository.save(user2);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");

        Item newItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(newItem);
        booking.setBooker(newUser2);
        booking.setStart(Instant.now().plusSeconds(30));
        booking.setEnd(Instant.now().plusSeconds(60));
        booking.setStatus(BookingStatus.APPROVED);

        Booking newBooking = bookingRepository.save(booking);

        assertEquals(bookingRepository.findBookingsByOwnerAndStatus(user.getId(), BookingStatus.APPROVED).size(), 1);
    }

    @Test
    public void findPastBookingsOfUserByItemTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        User user2 = new User();
        user2.setName("Alexander2");
        user2.setEmail("alexande2r@gmail.com");

        User newUser2 = userRepository.save(user2);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");

        Item newItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(newItem);
        booking.setBooker(newUser2);
        booking.setStart(Instant.now().plusSeconds(30));
        booking.setEnd(Instant.now().plusSeconds(60));
        booking.setStatus(BookingStatus.APPROVED);

        Booking newBooking = bookingRepository.save(booking);

        assertEquals(bookingRepository.findPastBookingsOfUserByItem(newUser2.getId(), newItem.getId(), Instant.now().plusSeconds(90)).size(), 1);
    }
}
