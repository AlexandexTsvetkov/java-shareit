package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class ItemRequestMapper {

    public static ItemRequest mapToItemRequest(ItemRequestDto request, User requester) {

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(request.getDescription());
        itemRequest.setRequester(requester);
        itemRequest.setId(request.getId());
        itemRequest.setCreated(request.getDate().toInstant(ZoneOffset.MAX));

        return itemRequest;
    }

    public static ItemRequestDto mapItemRequestDto(ItemRequest itemRequest) {

        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setDate(LocalDateTime.ofInstant(itemRequest.getCreated(), ZoneOffset.UTC));
        return dto;
    }
}










