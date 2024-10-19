package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;


import java.text.MessageFormat;
import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private final UserStorage userStorage;

    public Collection<UserDto> findAll() {
        return userStorage.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public UserDto findById(Long id) {
        Optional<User> user = userStorage.getUser(id);

        if (user.isPresent()) {
            return UserMapper.mapToUserDto(user.get());
        }

        throw new NotFoundException(MessageFormat.format("Пользователь с id {0, number} не найден", id));
    }

    public void deleteUser(Long id) {

        userStorage.deleteUser(id);
    }

    public UserDto create(UserDto newUserRequest) {

        if (userStorage.getUserByEmail(newUserRequest.getEmail()).isPresent()) {
            throw new InternalServerException("Пользователь с данным email уже существует");
        }

        User newUser = UserMapper.mapToUser(newUserRequest);

        return UserMapper.mapToUserDto(userStorage.create(newUser));
    }

    public UserDto update(long id, UpdateUserRequest updateUserRequest) {

        Optional<User> optionalUpdatedUser = userStorage.getUser(id);

        if (optionalUpdatedUser.isPresent()) {

            User user = optionalUpdatedUser.get();

            if (updateUserRequest.hasEmail() && userStorage.getUserByEmail(updateUserRequest.getEmail()).isPresent()) {
                throw new InternalServerException("Пользователь с данным email уже существует");
            }

            UserMapper.updateUserFields(user, updateUserRequest);

            User updatedUser = userStorage.update(user);
            return UserMapper.mapToUserDto(updatedUser);
        } else {
            throw new NotFoundException(MessageFormat.format("Пользователь с id {0, number} не найден", id));
        }
    }

    private void checkUser(long id) {
        if (userStorage.getUser(id).isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Пользователь с id {0, number} не найден", id));
        }
    }
}

