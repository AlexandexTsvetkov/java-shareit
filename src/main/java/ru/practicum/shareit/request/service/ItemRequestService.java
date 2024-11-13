package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestService {

    @Autowired
    private final UserRepository userStorage;

    @Autowired
    private final ItemRequestStorage itemRequestStorage;

    @Transactional
    public ItemRequestDto create(ItemRequestDto itemRequestDto, long userId) {

        Optional<User> user = userStorage.findById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Пользователь с id {0, number} не найден", userId));
        }

        itemRequestDto.setDate(LocalDateTime.now());

        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(itemRequestDto, user.get());

        return ItemRequestMapper.mapItemRequestDto(itemRequestStorage.create(itemRequest));
    }
}
