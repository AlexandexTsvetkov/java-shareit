package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;


import java.text.MessageFormat;
import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemService {

    @Autowired
    private final ItemStorage itemStorage;

    @Autowired
    private final UserStorage userStorage;

    public Collection<ItemDto> findAll(long userId) {

        Optional<User> user = userStorage.getUser(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Пользователь с id {0, number} не найден", userId));
        }
        return itemStorage.findAll(userId).stream()
                .map(ItemMapper::mapItemDto)
                .toList();
    }

    public ItemDto findById(long id) {
        Optional<Item> item = itemStorage.getItem(id);

        if (item.isPresent()) {
            return ItemMapper.mapItemDto(item.get());
        }

        throw new NotFoundException(MessageFormat.format("Вещь с id {0, number} не найдена", id));
    }

    public ItemDto create(NewItemRequest newItemRequest, long userId) {

        Optional<User> user = userStorage.getUser(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Пользователь с id {0, number} не найден", userId));
        }

        Item newItem = ItemMapper.mapToItem(newItemRequest, user.get());

        return ItemMapper.mapItemDto(itemStorage.create(newItem));
    }

    public ItemDto update(UpdateItemRequest updateItemRequest, long userId, long id) {

        Optional<User> user = userStorage.getUser(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Пользователь с id {0, number} не найден", userId));
        }

        Optional<Item> optionalUpdatedItem = itemStorage.getItem(id);

        if (optionalUpdatedItem.isPresent()) {

            Item item = optionalUpdatedItem.get();

            if (!(item.getOwner().equals(user.get()))) {
                throw new InternalServerException("Текущий пользователь не является владельцем вещи");
            }
            ItemMapper.updateItemFields(item, updateItemRequest);

            Item updatedItem = itemStorage.update(item);
            return ItemMapper.mapItemDto(updatedItem);
        } else {
            throw new NotFoundException(MessageFormat.format("Вещь с id {0, number} не найден", id));
        }
    }

    public Collection<ItemDto> findByText(String text) {

        return itemStorage.findByText(text).stream()
                .map(ItemMapper::mapItemDto)
                .toList();
    }
}

