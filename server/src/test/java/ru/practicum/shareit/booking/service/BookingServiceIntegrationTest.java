package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BookingServiceIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemStorage;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingStorage;

    @Test
    public void createTest() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");

        User newUser = userRepository.save(user);

        Item newItem = new Item();
        newItem.setName("Test Item");
        newItem.setOwner(newUser);
        newItem.setDescription("Some descrioption");
        newItem.setName("Some name");
        newItem.setAvailable(true);

        Item item = itemStorage.save(newItem);

        NewBookingRequest newBookingRequest = new NewBookingRequest();
        newBookingRequest.setItemId(item.getId());
        newBookingRequest.setBooker(newUser);
        newBookingRequest.setStatus(BookingStatus.APPROVED);
        newBookingRequest.setStart(LocalDateTime.now().plusMinutes(1));
        newBookingRequest.setEnd(LocalDateTime.now().plusMinutes(2));

        BookingDto bookingDto = bookingService.create(newBookingRequest, newUser.getId());

        assertTrue(bookingRepository.findById(bookingDto.getId()).isPresent());
    }

    @Test
    public void approveTest() {

        User user = new User();
        user.setName("Test User2");
        user.setEmail("test2@test.com");

        User newUser = userRepository.save(user);

        Item newItem = new Item();
        newItem.setName("Test Item2");
        newItem.setOwner(newUser);
        newItem.setDescription("Some descrioption");
        newItem.setName("Some name");
        newItem.setAvailable(true);

        Item item = itemStorage.save(newItem);

        NewBookingRequest newBookingRequest = new NewBookingRequest();
        newBookingRequest.setItemId(item.getId());
        newBookingRequest.setBooker(newUser);
        newBookingRequest.setStatus(BookingStatus.APPROVED);
        newBookingRequest.setStart(LocalDateTime.now().plusMinutes(1));
        newBookingRequest.setEnd(LocalDateTime.now().plusMinutes(2));

        BookingDto bookingDto = bookingService.create(newBookingRequest, newUser.getId());
        assertEquals(BookingStatus.WAITING, bookingDto.getStatus());

        bookingService.approve(bookingDto.getId(), newUser.getId(), true);

        Optional<Booking> booking = bookingRepository.findById(bookingDto.getId());

        assertTrue(booking.isPresent());
        assertEquals(booking.get().getStatus(), BookingStatus.APPROVED);
    }

    @Test
    public void findByIdTest() {

        User user = new User();
        user.setName("Test User3");
        user.setEmail("test3@test.com");

        User newUser = userRepository.save(user);

        Item newItem = new Item();
        newItem.setName("Test Item3");
        newItem.setOwner(newUser);
        newItem.setDescription("Some descrioption");
        newItem.setName("Some name");
        newItem.setAvailable(true);

        Item item = itemStorage.save(newItem);

        NewBookingRequest newBookingRequest = new NewBookingRequest();
        newBookingRequest.setItemId(item.getId());
        newBookingRequest.setBooker(newUser);
        newBookingRequest.setStatus(BookingStatus.APPROVED);
        newBookingRequest.setStart(LocalDateTime.now().plusMinutes(1));
        newBookingRequest.setEnd(LocalDateTime.now().plusMinutes(2));

        BookingDto bookingDto = bookingService.create(newBookingRequest, newUser.getId());
        assertEquals(bookingService.findById(bookingDto.getId()).getId(), bookingDto.getId());
    }

    @Test
    public void findByUserTest() {

        User user = new User();
        user.setName("Test User4");
        user.setEmail("test4@test.com");

        User newUser = userRepository.save(user);

        Item newItem = new Item();
        newItem.setName("Test Item4");
        newItem.setOwner(newUser);
        newItem.setDescription("Some descrioption");
        newItem.setName("Some name");
        newItem.setAvailable(true);

        Item item = itemStorage.save(newItem);

        NewBookingRequest newBookingRequest = new NewBookingRequest();
        newBookingRequest.setItemId(item.getId());
        newBookingRequest.setBooker(newUser);
        newBookingRequest.setStatus(BookingStatus.APPROVED);
        newBookingRequest.setStart(LocalDateTime.now().plusMinutes(1));
        newBookingRequest.setEnd(LocalDateTime.now().plusMinutes(2));

        BookingDto bookingDto = bookingService.create(newBookingRequest, newUser.getId());
        assertEquals(bookingService.findAllByUser(newUser.getId(), State.ALL).size(), 1);
    }

    @Test
    public void findByOwnerTest() {

        User user = new User();
        user.setName("Test User5");
        user.setEmail("test5@test.com");

        User newUser = userRepository.save(user);

        Item newItem = new Item();
        newItem.setName("Test Item5");
        newItem.setOwner(newUser);
        newItem.setDescription("Some descrioption");
        newItem.setName("Some name");
        newItem.setAvailable(true);

        Item item = itemStorage.save(newItem);

        NewBookingRequest newBookingRequest = new NewBookingRequest();
        newBookingRequest.setItemId(item.getId());
        newBookingRequest.setBooker(newUser);
        newBookingRequest.setStatus(BookingStatus.APPROVED);
        newBookingRequest.setStart(LocalDateTime.now().plusMinutes(1));
        newBookingRequest.setEnd(LocalDateTime.now().plusMinutes(2));

        BookingDto bookingDto = bookingService.create(newBookingRequest, newUser.getId());
        assertEquals(bookingService.findAllByOwner(newUser.getId(), State.ALL).size(), 1);
    }

    @Test
    public void getBookingTest() {

        User user = new User();
        user.setName("Test User6");
        user.setEmail("test6@test.com");

        User newUser = userRepository.save(user);

        Item newItem = new Item();
        newItem.setName("Test Item6");
        newItem.setOwner(newUser);
        newItem.setDescription("Some descrioption");
        newItem.setName("Some name");
        newItem.setAvailable(true);

        Item item = itemStorage.save(newItem);

        NewBookingRequest newBookingRequest = new NewBookingRequest();
        newBookingRequest.setItemId(item.getId());
        newBookingRequest.setBooker(newUser);
        newBookingRequest.setStatus(BookingStatus.APPROVED);
        newBookingRequest.setStart(LocalDateTime.now().plusMinutes(1));
        newBookingRequest.setEnd(LocalDateTime.now().plusMinutes(2));

        BookingDto bookingDto = bookingService.create(newBookingRequest, newUser.getId());
        assertEquals(bookingService.getBooking(bookingDto.getId(), newUser.getId()).getId(), bookingDto.getId());
    }
}