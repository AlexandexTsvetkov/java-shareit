package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemRequestService {

    private final UserStorage userStorage;
    private final ItemRequestStorage itemRequestStorage;

    public ItemRequestDto create(ItemRequestDto itemRequestDto, long userId) {

        Optional<User> user = userStorage.getUser(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Пользователь с id {0, number} не найден", userId));
        }

        itemRequestDto.setDate(LocalDateTime.now());

        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(itemRequestDto, user.get());

        return ItemRequestMapper.mapItemRequestDto(itemRequestStorage.create(itemRequest));
    }
}
