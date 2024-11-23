package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    private final UserRepository userStorage;

    public Collection<UserDto> findAll() {
        return userStorage.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public UserDto findById(Long id) {
        Optional<User> user = userStorage.findById(id);

        if (user.isPresent()) {
            return UserMapper.mapToUserDto(user.get());
        }

        throw new NotFoundException(MessageFormat.format("Пользователь с id {0, number} не найден", id));
    }

    @Transactional
    public void deleteUser(Long id) {

        userStorage.deleteById(id);
    }

    @Transactional
    public UserDto create(UserDto newUserRequest) {

        User newUser = UserMapper.mapToUser(newUserRequest);

        return UserMapper.mapToUserDto(userStorage.save(newUser));
    }

    @Transactional
    public UserDto update(long id, UpdateUserRequest updateUserRequest) {

        Optional<User> optionalUpdatedUser = userStorage.findById(id);

        if (optionalUpdatedUser.isPresent()) {

            User user = optionalUpdatedUser.get();

            user = UserMapper.updateUserFields(user, updateUserRequest);

            User updatedUser = userStorage.save(user);
            return UserMapper.mapToUserDto(updatedUser);
        } else {
            throw new NotFoundException(MessageFormat.format("Пользователь с id {0, number} не найден", id));
        }
    }
}

