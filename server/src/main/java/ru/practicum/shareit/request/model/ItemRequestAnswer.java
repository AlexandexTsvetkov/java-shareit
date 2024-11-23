package ru.practicum.shareit.request.model;

import ru.practicum.shareit.user.model.User;

public interface ItemRequestAnswer {

    Long getId();

    String getName();

    User getOwner();

    ItemRequest getItemRequest();
}
