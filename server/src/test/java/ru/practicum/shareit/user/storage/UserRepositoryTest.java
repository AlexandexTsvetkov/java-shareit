package ru.practicum.shareit.user.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void getUserTest() {

        Optional<User> foundUser = userRepository.findById(999999999L);

        assertTrue(foundUser.isEmpty());

    }

    @Test
    public void saveUserTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(newUser.getId());

        assertTrue(foundUser.isPresent());

    }

    @Test
    public void getUsersTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        userRepository.save(user);

        User user2 = new User();
        user2.setName("Alexander2");
        user2.setEmail("alexander2@gmail.com");

        userRepository.save(user2);

        List<User> users = userRepository.findAll();

        assertEquals(2, users.size());
    }

    @Test
    public void deleteUsersTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        userRepository.save(user);

        User user2 = new User();
        user2.setName("Alexander2");
        user2.setEmail("alexander2@gmail.com");

        userRepository.save(user2);

        assertEquals(2, userRepository.findAll().size());

        userRepository.delete(user2);

        assertEquals(1, userRepository.findAll().size());
    }
}
