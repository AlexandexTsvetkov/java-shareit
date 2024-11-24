package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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
        item.setDescription("Some description");
        item.setAvailable(true);

        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemStorage.save(any(Item.class))).thenReturn(item);

        NewItemRequest newItemRequest = new NewItemRequest();
        newItemRequest.setAvailable(true);
        newItemRequest.setName("Name");
        newItemRequest.setDescription("Description");

        ItemDto itemDto = itemService.create(newItemRequest, 1L);
        assertEquals(1L, itemDto.getId());

        verify(itemStorage).save(any(Item.class));
    }

    @Test
    public void createShouldThrowNotFoundWhenUserNotFound() {
        NewItemRequest newItemRequest = new NewItemRequest();
        newItemRequest.setAvailable(true);
        newItemRequest.setName("Name");
        newItemRequest.setDescription("Description");

        when(userStorage.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () ->
                itemService.create(newItemRequest, 1L)
        );
        assertEquals("Пользователь с id 1 не найден", thrown.getMessage());
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
        item.setDescription("Some description");
        item.setAvailable(true);

        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(item));

        ItemDto itemDto = itemService.findById(1L);
        assertEquals(1L, itemDto.getId());

        verify(itemStorage).findById(anyLong());
    }

    @Test
    public void findByIdShouldThrowNotFoundWhenItemNotFound() {
        when(itemStorage.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () ->
                itemService.findById(1L)
        );
        assertEquals("Вещь с id 1 не найдена", thrown.getMessage());
    }

    @Test
    public void findAllTest() {
        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some description");
        item.setAvailable(true);

        when(userStorage.findById(1L)).thenReturn(Optional.of(user));
        when(itemStorage.findItemsByOwnerId(1L)).thenReturn(Collections.singletonList(item));

        Collection<ItemDto> items = itemService.findAll(1L);
        assertNotNull(items);
        assertEquals(1, items.size());
    }

    @Test
    public void findAllShouldThrowNotFoundWhenUserNotFound() {
        when(userStorage.findById(1L)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () ->
                itemService.findAll(1L)
        );
        assertEquals("Пользователь с id 1 не найден", thrown.getMessage());
    }

    @Test
    public void findAllShouldReturnEmptyListWhenNoItems() {
        User user = new User();
        user.setId(1L);

        when(userStorage.findById(1L)).thenReturn(Optional.of(user));
        when(itemStorage.findItemsByOwnerId(1L)).thenReturn(Collections.emptyList());

        Collection<ItemDto> items = itemService.findAll(1L);
        assertNotNull(items);
        assertTrue(items.isEmpty());
    }


    @Test
    public void findAllShouldReturnItemsWhenMultipleExists() {
        User user = new User();
        user.setId(1L);

        Item item1 = new Item();
        item1.setId(1L);
        item1.setOwner(user);
        item1.setName("Item 1");
        item1.setAvailable(true);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setOwner(user);
        item2.setName("Item 2");
        item2.setAvailable(true);

        when(userStorage.findById(1L)).thenReturn(Optional.of(user));
        when(itemStorage.findItemsByOwnerId(1L)).thenReturn(Arrays.asList(item1, item2));

        Collection<ItemDto> items = itemService.findAll(1L);
        assertNotNull(items);
        assertEquals(2, items.size());
    }

    @Test
    public void createShouldMapFieldsCorrectly() {
        User user = new User();
        user.setId(1L);

        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));

        NewItemRequest newItemRequest = new NewItemRequest();
        newItemRequest.setAvailable(true);
        newItemRequest.setName("Test Item");
        newItemRequest.setDescription("Test Description");

        Item savedItem = new Item();

        savedItem.setId(1L);
        savedItem.setOwner(user);
        savedItem.setName(newItemRequest.getName());
        savedItem.setDescription(newItemRequest.getDescription());
        savedItem.setAvailable(true);

        when(itemStorage.save(any(Item.class))).thenReturn(savedItem);

        ItemDto itemDto = itemService.create(newItemRequest, 1L);

        assertNotNull(itemDto);
        assertEquals("Test Item", itemDto.getName());
        assertEquals("Test Description", itemDto.getDescription());
        assertTrue(itemDto.getAvailable());
    }

    @Test
    public void findByIdShouldRetrieveCommentsWhenExist() {
        User user = new User();
        user.setId(1L);
        Item item = new Item();
        item.setId(1L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(Instant.now());
        comment.setText("Great item!");

        when(itemStorage.findById(1L)).thenReturn(Optional.of(item));
        when(commentRepository.findCommentsByItemId(1L)).thenReturn(Collections.singletonList(comment));

        ItemDto itemDto = itemService.findById(1L);

        assertNotNull(itemDto);
        assertEquals(1, itemDto.getComments().size());
        assertEquals("Great item!", itemDto.getComments().get(0).getText());
    }

    @Test
    public void findByIdShouldReturnNotAvailableWhenItemIsNotAvailable() {
        User user = new User();
        user.setId(1L);
        Item item = new Item();
        item.setId(1L);
        item.setAvailable(false); // элемент недоступен

        when(itemStorage.findById(1L)).thenReturn(Optional.of(item));

        ItemDto itemDto = itemService.findById(1L);
        assertFalse(itemDto.getAvailable());
    }

    @Test
    public void findByIdShouldReturnEmptyCommentsWhenNoCommentsExist() {
        User user = new User();
        user.setId(1L);
        Item item = new Item();
        item.setId(1L);

        when(itemStorage.findById(1L)).thenReturn(Optional.of(item));
        when(commentRepository.findCommentsByItemId(1L)).thenReturn(Collections.emptyList());

        ItemDto itemDto = itemService.findById(1L);

        assertNotNull(itemDto);
        assertTrue(itemDto.getComments().isEmpty());
    }
}
