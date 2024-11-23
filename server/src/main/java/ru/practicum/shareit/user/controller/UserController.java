package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @GetMapping
    public Collection<UserDto> findAll() {

        log.info("пришел Get запрос /users");
        Collection<UserDto> users = userService.findAll();
        log.info("Отправлен ответ Get /users с телом: {}", users);
        return users;
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto newUserRequest) {

        log.info("пришел Post запрос /users с телом: {}", newUserRequest);
        UserDto newUser = userService.create(newUserRequest);
        log.info("Отправлен ответ Post /users с телом: {}", newUser);
        return newUser;
    }

    @GetMapping("/{id}")
    public UserDto findUser(@PathVariable long id) {

        log.info("пришел Get запрос /{}", id);
        UserDto user = userService.findById(id);
        log.info("Отправлен ответ Get /films с телом: {}", user);
        return user;
    }

    @PatchMapping ("/{id}")
    public UserDto update(@PathVariable long id, @RequestBody UpdateUserRequest updateUserRequest) {

        log.info("пришел PUT запрос /users с телом: {}", updateUserRequest);
        UserDto newUser = userService.update(id, updateUserRequest);
        log.info("Отправлен ответ PUT /users с телом: {}", newUser);
        return newUser;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long id) {
        log.info("пришел Delete запрос /{}", id);
        userService.deleteUser(id);
        log.info("Отправлен Delete ответ 204 /{}", id);
    }
}