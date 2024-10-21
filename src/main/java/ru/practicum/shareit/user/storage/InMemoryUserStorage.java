package ru.practicum.shareit.user.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
@Qualifier("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emailUniqSet = new HashSet<>();
    private long counter = 0L;

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {

        final String email = user.getEmail();
        if (emailUniqSet.contains(email)) {
            throw new DataAlreadyExistException("Email: " + email + " already exists");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        emailUniqSet.add(email);

        return user;
    }

    @Override
    public User update(User newUser) {

        long id = newUser.getId();
        final String email = newUser.getEmail();
        final String oldEmail = users.get(id).getEmail();

        if (!oldEmail.equals(email)) {
            if (emailUniqSet.contains(email)) {
                throw new DataAlreadyExistException("Email: " + email + " already exists");
            }
            emailUniqSet.remove(oldEmail);
            emailUniqSet.add(email);
        }
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

    private long getNextId() {
        return ++counter;
    }
}
