package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        bookingService = new BookingService(itemRepository, userRepository, bookingRepository);
    }

    @Test
    public void createTest() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(Instant.now());
        booking.setEnd(Instant.now());
        booking.setStatus(BookingStatus.APPROVED);

        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        NewBookingRequest newBookingRequest = new NewBookingRequest();
        newBookingRequest.setItemId(1L);
        newBookingRequest.setStatus(BookingStatus.APPROVED);
        newBookingRequest.setStart(LocalDateTime.now().plusMinutes(1));
        newBookingRequest.setEnd(LocalDateTime.now().plusMinutes(2));

        BookingDto bookingDto = bookingService.create(newBookingRequest, 1L);
        assertEquals(1L,  bookingDto.getId());

        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    public void approveWhenApprovedTest() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(Instant.now());
        booking.setEnd(Instant.now());
        booking.setStatus(BookingStatus.APPROVED);

        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        BookingDto bookingDto = bookingService.approve(1L, 1L, true);
        assertEquals(1L,  bookingDto.getId());
        assertEquals(BookingStatus.APPROVED,  bookingDto.getStatus());

        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    public void approveWhenNotApprovedTest() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(Instant.now());
        booking.setEnd(Instant.now());
        booking.setStatus(BookingStatus.REJECTED);

        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        BookingDto bookingDto = bookingService.approve(1L, 1L, false);
        assertEquals(1L,  bookingDto.getId());
        assertEquals(BookingStatus.REJECTED,  bookingDto.getStatus());

        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    public void findByIdTest() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(Instant.now());
        booking.setEnd(Instant.now());
        booking.setStatus(BookingStatus.APPROVED);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        BookingDto bookingDto = bookingService.findById(1L);
        assertEquals(1L,  bookingDto.getId());

        verify(bookingRepository).findById(any(Long.class));
    }

    @Test
    public void findAllCurrentByUserTest() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(Instant.now());
        booking.setEnd(Instant.now());
        booking.setStatus(BookingStatus.REJECTED);

        when(bookingRepository.findCurrentBookingsByBookerIdOrderByStart(any(Long.class), any(Instant.class))).thenReturn(List.of(booking));

        Collection<BookingDto> dtos = bookingService.findAllByUser(1L, State.CURRENT);
        assertEquals(1,  dtos.size());

        verify(bookingRepository).findCurrentBookingsByBookerIdOrderByStart(any(Long.class), any(Instant.class));
    }

    @Test
    public void findAllPastByUserTest() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(Instant.now());
        booking.setEnd(Instant.now());
        booking.setStatus(BookingStatus.REJECTED);

        when(bookingRepository.findPastBookingsByBookerIdOrderByStart(any(Long.class), any(Instant.class))).thenReturn(List.of(booking));

        Collection<BookingDto> dtos = bookingService.findAllByUser(1L, State.PAST);
        assertEquals(1,  dtos.size());

        verify(bookingRepository).findPastBookingsByBookerIdOrderByStart(any(Long.class), any(Instant.class));
    }

    @Test
    public void findAllFutureByUserTest() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(Instant.now());
        booking.setEnd(Instant.now());
        booking.setStatus(BookingStatus.REJECTED);

        when(bookingRepository.findFutureBookingsByBookerIdOrderByStart(any(Long.class), any(Instant.class))).thenReturn(List.of(booking));

        Collection<BookingDto> dtos = bookingService.findAllByUser(1L, State.FUTURE);
        assertEquals(1,  dtos.size());

        verify(bookingRepository).findFutureBookingsByBookerIdOrderByStart(any(Long.class), any(Instant.class));
    }

    @Test
    public void findAllOfStatusByUserTest() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(Instant.now());
        booking.setEnd(Instant.now());
        booking.setStatus(BookingStatus.REJECTED);

        when(bookingRepository.findBookingsByBookerIdAndByStatusOrderByStart(any(Long.class), any(BookingStatus.class))).thenReturn(List.of(booking));

        Collection<BookingDto> dtos = bookingService.findAllByUser(1L, State.REJECTED);
        assertEquals(1,  dtos.size());

        verify(bookingRepository).findBookingsByBookerIdAndByStatusOrderByStart(any(Long.class), any(BookingStatus.class));
    }

    @Test
    public void findAllByUserTest() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(Instant.now());
        booking.setEnd(Instant.now());
        booking.setStatus(BookingStatus.REJECTED);

        when(bookingRepository.findBookingsByBookerIdOrderByStart(any(Long.class))).thenReturn(List.of(booking));

        Collection<BookingDto> dtos = bookingService.findAllByUser(1L, State.ALL);
        assertEquals(1,  dtos.size());

        verify(bookingRepository).findBookingsByBookerIdOrderByStart(any(Long.class));
    }

    @Test
    public void findAllCurrentByOwnerTest() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(Instant.now());
        booking.setEnd(Instant.now());
        booking.setStatus(BookingStatus.REJECTED);

        when(bookingRepository.findCurrentBookingsByOwner(any(Long.class), any(Instant.class))).thenReturn(List.of(booking));

        Collection<BookingDto> dtos = bookingService.findAllByOwner(1L, State.CURRENT);
        assertEquals(1,  dtos.size());

        verify(bookingRepository).findCurrentBookingsByOwner(any(Long.class), any(Instant.class));
    }

    @Test
    public void findAllPastByOwnerTest() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(Instant.now());
        booking.setEnd(Instant.now());
        booking.setStatus(BookingStatus.REJECTED);

        when(bookingRepository.findPastBookingsByOwner(any(Long.class), any(Instant.class))).thenReturn(List.of(booking));

        Collection<BookingDto> dtos = bookingService.findAllByOwner(1L, State.PAST);
        assertEquals(1,  dtos.size());

        verify(bookingRepository).findPastBookingsByOwner(any(Long.class), any(Instant.class));
    }

    @Test
    public void findAllFutureByOwnerTest() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(Instant.now());
        booking.setEnd(Instant.now());
        booking.setStatus(BookingStatus.REJECTED);

        when(bookingRepository.findFutureBookingsByOwner(any(Long.class), any(Instant.class))).thenReturn(List.of(booking));

        Collection<BookingDto> dtos = bookingService.findAllByOwner(1L, State.FUTURE);
        assertEquals(1,  dtos.size());

        verify(bookingRepository).findFutureBookingsByOwner(any(Long.class), any(Instant.class));
    }

    @Test
    public void findAllOfStatusByOwnerTest() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(Instant.now());
        booking.setEnd(Instant.now());
        booking.setStatus(BookingStatus.REJECTED);

        when(bookingRepository.findBookingsByOwnerAndStatus(any(Long.class), any(BookingStatus.class))).thenReturn(List.of(booking));

        Collection<BookingDto> dtos = bookingService.findAllByOwner(1L, State.REJECTED);
        assertEquals(1,  dtos.size());

        verify(bookingRepository).findBookingsByOwnerAndStatus(any(Long.class), any(BookingStatus.class));
    }

    @Test
    public void findAllByOwnerTest() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(Instant.now());
        booking.setEnd(Instant.now());
        booking.setStatus(BookingStatus.REJECTED);

        when(bookingRepository.findBookingsByOwner(any(Long.class))).thenReturn(List.of(booking));

        Collection<BookingDto> dtos = bookingService.findAllByOwner(1L, State.ALL);
        assertEquals(1,  dtos.size());

        verify(bookingRepository).findBookingsByOwner(any(Long.class));
    }

    @Test
    public void findbookingTest() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(Instant.now());
        booking.setEnd(Instant.now());
        booking.setStatus(BookingStatus.REJECTED);

        when(bookingRepository.findBookingByIdAndOwnerOrBookerId(any(Long.class), any(Long.class))).thenReturn(Optional.of(booking));

        BookingDto dto = bookingService.getBooking(1L, 1L);
        assertEquals(1L,  dto.getId());

        verify(bookingRepository).findBookingByIdAndOwnerOrBookerId(any(Long.class), any(Long.class));
    }
}