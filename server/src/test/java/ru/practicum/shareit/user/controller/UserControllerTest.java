package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createTest() throws Exception {

        UserDto userDto = new UserDto();
        userDto.setEmail("Test@test.com");
        userDto.setName("Test User");
        userDto.setId(1L);

        when(userService.create(Mockito.any(UserDto.class)))
                .thenAnswer(invocationOnMock -> {
                    UserDto returnUserDto = new UserDto();
                    returnUserDto.setEmail("Test@test.com");
                    returnUserDto.setName("Test User");
                    returnUserDto.setId(1L);
                    return returnUserDto;
                });

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1), Integer.class));
    }

    @Test
    public void findAllTest() throws Exception {

        when(userService.findAll())
                .thenAnswer(invocationOnMock -> {
                    UserDto returnUserDto = new UserDto();
                    returnUserDto.setEmail("Test@test.com");
                    returnUserDto.setName("Test User");
                    returnUserDto.setId(1L);
                    return List.of(returnUserDto);
                });

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void findUserTest() throws Exception {

        when(userService.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    UserDto returnUserDto = new UserDto();
                    returnUserDto.setEmail("Test@test.com");
                    returnUserDto.setName("Test User");
                    returnUserDto.setId(1L);
                    return returnUserDto;
                });

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void findUserErrorTest() throws Exception {

        when(userService.findById(Mockito.anyLong()))
                .thenThrow(NotFoundException.class);

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.message")
                                .value(containsString(
                                        "Not found exception"
                                ))
                );
    }

    @Test
    public void updateTest() throws Exception {

        UpdateUserRequest userDto = new UpdateUserRequest();
        userDto.setEmail("Test@test.com");
        userDto.setName("Test User");

        when(userService.update(Mockito.anyLong(), Mockito.any(UpdateUserRequest.class)))
                .thenAnswer(invocationOnMock -> {
                    UserDto returnUserDto = new UserDto();
                    returnUserDto.setEmail("Test@test.com");
                    returnUserDto.setName("Test User");
                    returnUserDto.setId(1L);
                    return returnUserDto;
                });

        mvc.perform(patch("/users/1")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1), Integer.class));
    }

    @Test
    public void deleteUserTest() throws Exception {

        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testError() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("Test@test.com");
        userDto.setName("Test User");
        userDto.setId(1L);

        when(userService.create(Mockito.any(UserDto.class)))
                .thenThrow(InternalServerException.class);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().is5xxServerError())
                .andExpect(
                        jsonPath("$.message")
                                .value(containsString(
                                        "Internal exception"
                                ))
                );


    }
}