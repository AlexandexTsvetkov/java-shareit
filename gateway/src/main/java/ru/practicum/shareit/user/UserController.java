package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> findAll() {
        log.info("Find all users");
        return userClient.findAll();
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto newUserRequest) {
        log.info("Create new user {}", newUserRequest);
        return userClient.create(newUserRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findUser(@PathVariable long id) {
        log.info("Find user with id {}", id);
        return userClient.findUser(id);
    }

    @PatchMapping ("/{id}")
    public ResponseEntity<Object> update(@PathVariable long id, @Valid @RequestBody UpdateUserDto updateUserRequest) {
        log.info("Update user with id {}, body {}", id, updateUserRequest);
        return userClient.update(id, updateUserRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteLike(@PathVariable long id) {
        log.info("Delete user with id {}", id);
        return userClient.deleteUser(id);
    }
}
