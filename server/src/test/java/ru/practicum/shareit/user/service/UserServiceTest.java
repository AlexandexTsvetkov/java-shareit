package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    public void createTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");
        user.setId(1L);

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto userDto = new UserDto();
        userDto.setEmail("Test@test.com");
        userDto.setName("Test User");
        userDto.setId(1L);

        userDto = userService.create(userDto);
        assertEquals(1L, userDto.getId());

        verify(userRepository).save(any(User.class));
    }

    @Test
    public void findAllTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");
        user.setId(1L);

        when(userRepository.findAll()).thenReturn(List.of(user));

        Collection<UserDto> users = userService.findAll();
        assertEquals(1, users.size());

        verify(userRepository).findAll();
    }

    @Test
    public void deleteUserTest() {

        userService.deleteUser(1L);

        verify(userRepository).deleteById(anyLong());
    }

    @Test
    public void findByIdTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");
        user.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDto userDto = userService.findById(1L);
        assertEquals(1L, userDto.getId());

        verify(userRepository).findById(anyLong());
    }

    @Test
    public void updateTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");
        user.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UpdateUserRequest updateDto = new UpdateUserRequest();
        updateDto.setEmail("Test@test.com");
        updateDto.setName("Test User");

        UserDto userDto = userService.update(1L, updateDto);
        assertEquals(1L, userDto.getId());

        verify(userRepository).save(any(User.class));
    }
}