package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemStorage {

    Collection<Item> findAll(long userId);

    Item create(Item item);

    Item update(Item newItem);

    Optional<Item> getItem(long id);

    Collection<Item> findByText(String text);
}

