package ru.practicum.shareit.request.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestAnswer;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ItemRequestTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void findAllByItemRequestIdTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setRequester(newUser);

        itemRequest.setDescription("Some description");

        ItemRequest newItemRequest = itemRequestRepository.save(itemRequest);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");
        item.setItemRequest(newItemRequest);

        itemRepository.save(item);

        List<ItemRequestAnswer> answers = itemRepository.findAllByItemRequestId(newItemRequest.getId());

        assertEquals(1, answers.size());

    }

    @Test
    public void findAllByItemRequestIdsTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setRequester(newUser);

        itemRequest.setDescription("Some description");

        ItemRequest newItemRequest = itemRequestRepository.save(itemRequest);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");
        item.setItemRequest(newItemRequest);


        itemRepository.save(item);

        ItemRequest itemRequest2 = new ItemRequest();

        itemRequest2.setRequester(newUser);

        itemRequest2.setDescription("Some description2");

        ItemRequest newItemRequest2 = itemRequestRepository.save(itemRequest2);

        Item item2 = new Item();
        item2.setName("Vesh'");
        item2.setOwner(newUser);
        item2.setAvailable(true);
        item2.setDescription("Some description");
        item2.setItemRequest(newItemRequest2);


        itemRepository.save(item2);

        List<ItemRequestAnswer> answers = itemRepository.findAllByItemRequestIdIn(List.of(newItemRequest.getId(), newItemRequest2.getId()));

        assertEquals(2, answers.size());

    }

    @Test
    public void findItemRequestsByRequesterIdOrderByCreatedDescTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");
        User newUser = userRepository.save(user);

        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setRequester(newUser);

        itemRequest.setDescription("Some description");

        ItemRequest newItemRequest = itemRequestRepository.save(itemRequest);

        assertEquals(itemRequestRepository.findItemRequestsByRequesterIdOrderByCreatedDesc(newUser.getId()).size(), 1);
    }

    @Test
    public void findItemRequestsByRequesterIdNotOrderByCreatedDescTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");
        User newUser = userRepository.save(user);

        User user2 = new User();
        user2.setName("Alexander2");
        user2.setEmail("alexande2r@gmail.com");
        User newUser2 = userRepository.save(user2);

        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setRequester(newUser);

        itemRequest.setDescription("Some description");

        ItemRequest newItemRequest = itemRequestRepository.save(itemRequest);

        assertEquals(itemRequestRepository.findItemRequestsByRequesterIdOrderByCreatedDesc(newUser2.getId()).size(), 0);
    }
}
