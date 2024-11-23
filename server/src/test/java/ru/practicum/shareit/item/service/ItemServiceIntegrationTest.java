package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ItemServiceIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemStorage;

    @Autowired
    private UserRepository userStorage;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    public void createTest() {

        User user = new User();
        user.setName("Test User8");
        user.setEmail("test8@test.com");

        User newUser = userStorage.save(user);

        NewItemRequest newItemRequest = new NewItemRequest();
        newItemRequest.setName("Test Item8");
        newItemRequest.setDescription("Test Description8");
        newItemRequest.setAvailable(true);
        newItemRequest.setOwner(newUser);

        ItemDto itemDto = itemService.create(newItemRequest, newUser.getId());

        assertTrue(itemStorage.findById(itemDto.getId()).isPresent());
    }

    @Test
    public void findAllTest() {

        User user = new User();
        user.setName("Test User9");
        user.setEmail("test9@test.com");

        User newUser = userStorage.save(user);

        NewItemRequest newItemRequest = new NewItemRequest();
        newItemRequest.setName("Test Item9");
        newItemRequest.setDescription("Test Description8");
        newItemRequest.setAvailable(true);
        newItemRequest.setOwner(newUser);

        itemService.create(newItemRequest, newUser.getId());

        assertTrue(!itemStorage.findAll().isEmpty());
    }

    @Test
    public void findByIdTest() {

        User user = new User();
        user.setName("Test User10");
        user.setEmail("test10@test.com");

        User newUser = userStorage.save(user);

        NewItemRequest newItemRequest = new NewItemRequest();
        newItemRequest.setName("Test Item10");
        newItemRequest.setDescription("Test Description8");
        newItemRequest.setAvailable(true);
        newItemRequest.setOwner(newUser);

        ItemDto itemDto = itemService.create(newItemRequest, newUser.getId());

        assertEquals(itemService.findById(itemDto.getId()).getId(), itemDto.getId());
    }

    @Test
    public void updateTest() {

        User user = new User();
        user.setName("Test User11");
        user.setEmail("test11@test.com");

        User newUser = userStorage.save(user);

        NewItemRequest newItemRequest = new NewItemRequest();
        newItemRequest.setName("Test Item8");
        newItemRequest.setDescription("Test Description8");
        newItemRequest.setAvailable(true);
        newItemRequest.setOwner(newUser);

        ItemDto itemDto = itemService.create(newItemRequest, newUser.getId());

        UpdateItemRequest updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setName("Test Item13");

        itemService.update(updateItemRequest, newUser.getId(), itemDto.getId());

        itemDto = itemService.findById(itemDto.getId());

        assertEquals(itemDto.getName(), updateItemRequest.getName());
    }

    @Transactional
    @Test
    public void addCommentTest() {

        User user = new User();
        user.setName("Test User14");
        user.setEmail("test14@test.com");

        User newUser = userStorage.save(user);

        Item newItem = new Item();
        newItem.setName("Test Item14");
        newItem.setOwner(newUser);
        newItem.setDescription("Some descrioption");
        newItem.setName("Some name");
        newItem.setAvailable(true);

        Item item = itemStorage.save(newItem);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(newUser);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(Instant.now().minusSeconds(2L));
        booking.setEnd(Instant.now().minusSeconds(1L));

        NewCommentRequest newCommentRequest = new NewCommentRequest();
        newCommentRequest.setText("Comment");

        bookingRepository.save(booking);

        CommentDto commentDto = itemService.addComment(newCommentRequest, newUser.getId(), item.getId());

        assertEquals(commentRepository.getReferenceById(commentDto.getId()).getText(), newCommentRequest.getText());
    }

    @Transactional
    @Test
    public void findByTest() {

        User user = new User();
        user.setName("Test User15");
        user.setEmail("test15@test.com");

        User newUser = userStorage.save(user);

        Item newItem = new Item();
        newItem.setName("Test Item15");
        newItem.setOwner(newUser);
        newItem.setDescription("Some descrioption15");
        newItem.setName("Some name");
        newItem.setAvailable(true);

        Item item = itemStorage.save(newItem);

        assertEquals(itemService.findByText(newItem.getDescription()).size(), 1);
    }
}

