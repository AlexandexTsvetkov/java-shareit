package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    ItemService itemService;

    @Mock
    private ItemRepository itemStorage;

    @Mock
    private UserRepository userStorage;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @BeforeEach
    public void setUp() {
        itemService = new ItemService(itemStorage, userStorage, bookingRepository, commentRepository, itemRequestRepository);
    }

    @Test
    public void createWithoutItemRequestTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemStorage.save(any(Item.class))).thenReturn(item);

        NewItemRequest newItemRequest = new NewItemRequest();
        newItemRequest.setAvailable(true);
        newItemRequest.setName("Name");
        newItemRequest.setDescription("Description");
        newItemRequest.setOwner(user);


        ItemDto itemDto = itemService.create(newItemRequest, 1L);
        assertEquals(1L, itemDto.getId());

        verify(itemStorage).save(any(Item.class));
    }

    @Test
    public void findByIdTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(item));

        ItemDto itemDto = itemService.findById(1L);
        assertEquals(1L, itemDto.getId());

        verify(itemStorage).findById(anyLong());
    }

    @Test
    public void finAllTest() {
        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemStorage.findItemsByOwnerId(anyLong())).thenReturn(List.of(item));
        when(commentRepository.findCommentsByItemIds(anyList())).thenReturn(new ArrayList<>());
        when(bookingRepository.findLastAndNextBookings(anyLong(), any(Instant.class))).thenReturn(List.of());

        Collection<ItemDto> itemDtos = itemService.findAll(1L);
        assertEquals(1L, itemDtos.size());

        verify(itemStorage).findItemsByOwnerId(anyLong());
    }

    @Test
    public void createWithItemRequestTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");
        user.setId(1L);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequester(user);
        itemRequest.setDescription("Description");
        itemRequest.setCreated(Instant.now());
        itemRequest.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setItemRequest(itemRequest);
        item.setAvailable(true);

        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemStorage.save(any(Item.class))).thenReturn(item);

        NewItemRequest newItemRequest = new NewItemRequest();
        newItemRequest.setAvailable(true);
        newItemRequest.setName("Name");
        newItemRequest.setDescription("Description");
        newItemRequest.setOwner(user);
        newItemRequest.setRequestId(1L);

        ItemDto itemDto = itemService.create(newItemRequest, 1L);
        assertEquals(1L, itemDto.getId());

        verify(itemStorage).save(any(Item.class));
    }

    @Test
    public void updateTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemStorage.save(any(Item.class))).thenReturn(item);

        UpdateItemRequest updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setAvailable(true);
        updateItemRequest.setName("Name");
        updateItemRequest.setDescription("Description");
        updateItemRequest.setOwner(user);

        ItemDto itemDto = itemService.update(updateItemRequest, 1L, 1L);
        assertEquals(1L, itemDto.getId());

        verify(itemStorage).save(any(Item.class));
    }

    @Test
    public void addCommentTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");
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

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setText("Some comment");
        comment.setCreated(Instant.now());

        when(bookingRepository.findPastBookingsOfUserByItem(anyLong(), anyLong(), any(Instant.class))).thenReturn(List.of(booking));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        NewCommentRequest newCommentRequest = new NewCommentRequest();
        newCommentRequest.setText("Some comment");

        CommentDto commentDto = itemService.addComment(newCommentRequest, 1L, 1L);
        assertEquals(1L, commentDto.getId());

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    public void findByTextTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        when(itemStorage.findByDescriptionContainingIgnoreCaseOrNameContainingIgnoreCaseAAndAvailable(anyString())).thenReturn(List.of(item));

        Collection<ItemDto> itemDtos = itemService.findByText("F");
        assertEquals(1, itemDtos.size());

        verify(itemStorage).findByDescriptionContainingIgnoreCaseOrNameContainingIgnoreCaseAAndAvailable(anyString());
    }
}
