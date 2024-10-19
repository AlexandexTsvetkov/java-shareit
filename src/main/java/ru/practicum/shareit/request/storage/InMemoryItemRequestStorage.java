package ru.practicum.shareit.request.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.HashMap;
import java.util.Map;

@Component
@Qualifier("InMemoryItemRequestStorage")
public class InMemoryItemRequestStorage implements ItemRequestStorage {

    private final Map<Long, ItemRequest> requests = new HashMap<>();
    private long counter = 0L;

    @Override
    public ItemRequest create(ItemRequest itemRequest) {

        itemRequest.setId(getNextId());

        requests.put(itemRequest.getId(), itemRequest);

        return itemRequest;
    }
    private long getNextId() {
        return ++counter;
    }
}
