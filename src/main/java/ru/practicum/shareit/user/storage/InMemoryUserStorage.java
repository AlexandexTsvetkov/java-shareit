package ru.practicum.shareit.user.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Qualifier("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long counter = 0L;

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {

        user.setId(getNextId());

        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User update(User newUser) {

        long id = newUser.getId();

        users.put(id, newUser);

        return newUser;
    }

    @Override
    public Optional<User> getUser(long id) {

        User user = users.get(id);
        return (user == null) ? Optional.empty() : Optional.of(user);
    }

    @Override
    public void deleteUser(long id) {

        if (users.get(id) != null) {
            users.remove(id);
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return users.values().stream().filter(user -> email.equals(user.getEmail())).findAny();
    }

    private long getNextId() {
        return ++counter;
    }
}
