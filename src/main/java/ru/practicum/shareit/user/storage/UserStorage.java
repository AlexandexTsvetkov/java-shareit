package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> findAll();

    User create(User user);

    User update(User newUser);

    Optional<User> getUser(long id);

    Optional<User> getUserByEmail(String email);

    void deleteUser(long id);
}
