package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

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

        Item item = itemStorage.findById(itemDto.getId()).get();

        assertEquals(item.getId(), itemDto.getId());

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

    @Test
    public void addCommentErrorTest() {

        NewCommentRequest newCommentRequest = new NewCommentRequest();
        newCommentRequest.setText("Comment");

        InternalServerException thrown = assertThrows(InternalServerException.class, () -> itemService.addComment(newCommentRequest, 100000L, 10000000L));
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

        assertTrue(itemService.findByText(newItem.getDescription()).size() > 0);
    }

    @Test
    public void findByIdErrorTest() {

        NotFoundException thrown = assertThrows(NotFoundException.class, () ->
                itemService.findById(100000L)
        );

    }

    @Test
    public void updateErrorTest() {

        User user = new User();
        user.setName("Test User14gfyyi");
        user.setEmail("testy780014@test.com");

        User newUser = userStorage.save(user);


        UpdateItemRequest updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setName("Test Item13");
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> itemService.update(updateItemRequest, newUser.getId(), 10000L));

    }

    @Test
    public void updateErrorItemTest() {

        User user = new User();
        user.setName("Test User147t");
        user.setEmail("test1vjv4@test.com");

        User newUser = userStorage.save(user);

        Item newItem = new Item();
        newItem.setName("Test Item15");
        newItem.setOwner(newUser);
        newItem.setDescription("Some descrioption15");
        newItem.setName("Some name");
        newItem.setAvailable(true);

        Item item = itemStorage.save(newItem);

        UpdateItemRequest updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setName("Test Item13");
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> itemService.update(updateItemRequest, 100000L, item.getId()));

    }

    @Test
    public void updateErrorItemNotOwnerTest() {

        User user = new User();
        user.setName("Test User147tfgdf");
        user.setEmail("test1vjv4dgdfgd@test.com");

        User newUser = userStorage.save(user);

        User user2 = new User();
        user2.setName("Test User147tfgdf55");
        user2.setEmail("test1vjv554dgdfgd@test.com");

        User newUser2 = userStorage.save(user2);

        Item newItem = new Item();
        newItem.setName("Test Item15");
        newItem.setOwner(newUser);
        newItem.setDescription("Some descrioption15");
        newItem.setName("Some name");
        newItem.setAvailable(true);

        Item item = itemStorage.save(newItem);

        UpdateItemRequest updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setName("Test Item13");
        InternalServerException thrown = assertThrows(InternalServerException.class, () -> itemService.update(updateItemRequest, 2, item.getId()));

    }

    @Test
    public void createErrorTest() {

        User user = new User();
        user.setName("Test User8756775");
        user.setEmail("test85675677@test.com");

        User newUser = userStorage.save(user);

        NewItemRequest newItemRequest = new NewItemRequest();
        newItemRequest.setName("Test Item8");
        newItemRequest.setDescription("Test Description8");
        newItemRequest.setAvailable(true);
        newItemRequest.setOwner(newUser);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> itemService.create(newItemRequest, 1000000L));
    }

}

