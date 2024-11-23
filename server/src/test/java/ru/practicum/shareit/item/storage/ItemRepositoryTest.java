package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void getItemTest() {

        Optional<Item> foundUser = itemRepository.findById(9999999999L);

        assertTrue(foundUser.isEmpty());
    }

    @Test
    public void saveItemTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");

        Item newItem = itemRepository.save(item);

        Optional<Item> foundItem = itemRepository.findById(newItem.getId());

        assertTrue(foundItem.isPresent());

    }

    @Test
    public void updateItemTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");

        Item newItem = itemRepository.save(item);

        Optional<Item> foundItem = itemRepository.findById(newItem.getId());

        assertTrue(foundItem.isPresent());

        Item updatedItem = foundItem.get();

        updatedItem.setName("kus'ka");

        itemRepository.save(updatedItem);

        Optional<Item> newFoundItem = itemRepository.findById(newItem.getId());

        assertTrue(newFoundItem.isPresent());

        assertEquals("kus'ka", newFoundItem.get().getName());
    }

    @Test
    public void findItemsByOwnerIdTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");

        itemRepository.save(item);

        Item item2 = new Item();
        item2.setName("Vesh'2");
        item2.setOwner(newUser);
        item2.setAvailable(true);
        item2.setDescription("Some description2");

        itemRepository.save(item2);

        assertEquals(2, itemRepository.findItemsByOwnerId(newUser.getId()).size());
    }

    @Test
    public void findByDescriptionContainingIgnoreCaseOrNameContainingIgnoreCaseAAndAvailable() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");

        itemRepository.save(item);

        Item item2 = new Item();
        item2.setName("Vesh'2");
        item2.setOwner(newUser);
        item2.setAvailable(true);
        item2.setDescription("RaseNgan");

        itemRepository.save(item2);

        List<Item> items = itemRepository.findByDescriptionContainingIgnoreCaseOrNameContainingIgnoreCaseAAndAvailable("rasengan");

        assertEquals(1, items.size());
    }

    @Test
    public void findCommentsByItemIdTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");

        Item newItem = itemRepository.save(item);

        Comment comment = new Comment();
        comment.setItem(newItem);
        comment.setText("some comment");
        comment.setAuthor(newUser);

        commentRepository.save(comment);

        assertEquals(1, commentRepository.findCommentsByItemId(newItem.getId()).size());
    }

    @Test
    public void findCommentsByItemIdsTest() {

        User user = new User();
        user.setName("Alexander");
        user.setEmail("alexander@gmail.com");

        User newUser = userRepository.save(user);

        Item item = new Item();
        item.setName("Vesh'");
        item.setOwner(newUser);
        item.setAvailable(true);
        item.setDescription("Some description");

        Item newItem = itemRepository.save(item);

        Item item2 = new Item();
        item2.setName("Vesh'2");
        item2.setOwner(newUser);
        item2.setAvailable(true);
        item2.setDescription("RaseNgan");

        Item newItem2 = itemRepository.save(item2);

        Comment comment = new Comment();
        comment.setItem(newItem);
        comment.setText("some comment");
        comment.setAuthor(newUser);

        commentRepository.save(comment);

        Comment comment2 = new Comment();
        comment2.setItem(newItem2);
        comment2.setText("some comment");
        comment2.setAuthor(newUser);

        commentRepository.save(comment);

        commentRepository.save(comment2);

        assertEquals(2, commentRepository.findCommentsByItemIds(List.of(newItem2.getId(), newItem.getId())).size());
    }
}
