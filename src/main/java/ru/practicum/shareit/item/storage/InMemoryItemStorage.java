package ru.practicum.shareit.item.storage;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
@Qualifier("InMemoryItemStorage")
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();
    private long counter = 0L;

    @Override
    public Collection<Item> findAll(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == userId).toList();
    }

    @Override
    public Item create(Item item) {

        item.setId(getNextId());

        items.put(item.getId(), item);

        return item;
    }

    @Override
    public Item update(Item newItem) {

        long id = newItem.getId();

        items.put(id, newItem);

        return newItem;
    }

    @Override
    public Optional<Item> getItem(long id) {

        Item item = items.get(id);
        return (item == null) ? Optional.empty() : Optional.of(item);
    }

    @Override
    public Collection<Item> findByText(String text) {

        if (text.isBlank()) {
            return new ArrayList<>();
        } else {

            String upperText = text.toUpperCase();

            return items.values().stream()
                    .filter(item -> (item.getName().toUpperCase().contains(upperText)
                            || item.getDescription().toUpperCase().contains(upperText))
                            && item.getAvailable()).toList();
        }
    }

    private long getNextId() {
        return ++counter;
    }
}