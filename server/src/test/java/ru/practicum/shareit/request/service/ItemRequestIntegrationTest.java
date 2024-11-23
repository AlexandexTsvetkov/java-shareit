package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ItemRequestIntegrationTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserRepository userStorage;

    @Autowired
    private ItemRequestRepository itemRequestStorage;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void createTest() {

        User user = new User();
        user.setName("Test User16");
        user.setEmail("test16@test.com");

        User newUser = userStorage.save(user);

        NewRequest newRequest = new NewRequest();
        newRequest.setDescription("Test Description9");

        ItemRequestDto itemRequestDto = itemRequestService.create(newRequest, newUser.getId());

        assertTrue(itemRequestStorage.findById(itemRequestDto.getId()).isPresent());
    }

    @Test
    public void findAllOtherUsersTest() {

        User user = new User();
        user.setName("Test User18");
        user.setEmail("test18@test.com");

        User newUser = userStorage.save(user);

        NewRequest newRequest = new NewRequest();
        newRequest.setDescription("Test Description11");

        itemRequestService.create(newRequest, newUser.getId());

        User user2 = new User();
        user2.setName("Test User19");
        user2.setEmail("test19@test.com");

        User newUser2 = userStorage.save(user2);

        NewRequest newRequest2 = new NewRequest();
        newRequest.setDescription("Test Description112");

        itemRequestService.create(newRequest2, newUser2.getId());

        assertTrue(!itemRequestService.findAllByUserId(newUser.getId()).isEmpty());
    }

    @Test
    public void findByIdTest() {

        User user = new User();
        user.setName("Test User20");
        user.setEmail("test20@test.com");

        User newUser = userStorage.save(user);

        NewRequest newRequest = new NewRequest();
        newRequest.setDescription("Test Description13");

        ItemRequestDto itemRequestDto = itemRequestService.create(newRequest, newUser.getId());

        assertEquals(itemRequestService.findById(itemRequestDto.getId()).getId(), itemRequestDto.getId());
    }
}
