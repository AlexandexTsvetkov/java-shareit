package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userStorage;

    @Test
    public void createTest() {

        UserDto user = new UserDto();
        user.setName("Test User98");
        user.setEmail("test98@test.com");

        UserDto newUser = userService.create(user);

        assertTrue(userStorage.findById(newUser.getId()).isPresent());
    }


    @Test
    public void updateTest() {

        UserDto user = new UserDto();
        user.setName("Test User23");
        user.setEmail("test23@test.com");

        UserDto newUser = userService.create(user);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setName("Test User24");

        UserDto newUserUpdated = userService.update(newUser.getId(), updateUserRequest);

        assertEquals(newUserUpdated.getName(), "Test User24");
    }

    @Test
    public void deleteTest() {

        UserDto user = new UserDto();
        user.setName("Test User25");
        user.setEmail("test25@test.com");

        UserDto newUser = userService.create(user);

        assertTrue(userStorage.findById(newUser.getId()).isPresent());

        userService.deleteUser(newUser.getId());

        assertTrue(userStorage.findById(newUser.getId()).isEmpty());
    }

    @Test
    public void findByIdTest() {

        UserDto user = new UserDto();
        user.setName("Test User26");
        user.setEmail("test26@test.com");

        UserDto newUser = userService.create(user);

        assertEquals(userService.findById(newUser.getId()).getId(), newUser.getId());
    }

    @Test
    public void findByIdErrorTest() {

        NotFoundException thrown = assertThrows(NotFoundException.class, () ->
                userService.findById(100000L)
        );

    }

    @Test
    public void findAllTest() {

        UserDto user = new UserDto();
        user.setName("Test User27");
        user.setEmail("test27@test.com");

        UserDto newUser = userService.create(user);

        assertTrue(!userService.findAll().isEmpty());
    }

    @Test
    public void updateErrorTest() {


        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setName("Test User277");
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> userService.update(100000L, updateUserRequest)
        );

    }
}
