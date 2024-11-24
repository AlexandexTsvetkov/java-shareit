package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestAnswer;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class ItemRequestMapper {

    public static ItemRequest mapToItemRequest(NewRequest request, User requester) {

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(request.getDescription());
        itemRequest.setRequester(requester);
        itemRequest.setCreated(Instant.now());

        return itemRequest;
    }

    public static ItemRequestDto mapItemRequestDto(ItemRequest itemRequest) {

        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setCreated(LocalDateTime.ofInstant(itemRequest.getCreated(), ZoneOffset.UTC));
        return dto;
    }

    public static ItemRequestDto mapItemRequestDto(ItemRequest itemRequest, List<ItemRequestAnswer> answers) {

        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setCreated(LocalDateTime.ofInstant(itemRequest.getCreated(), ZoneOffset.UTC));
        dto.setItems(mapItemRequestAnswerDtos(answers));
        return dto;
    }

    public static List<ItemRequestAnswerDto> mapItemRequestAnswerDtos(List<ItemRequestAnswer> itemRequestAnswers) {

        if (itemRequestAnswers != null) {
            return itemRequestAnswers.stream().map(itemRequestAnswer -> {
                ItemRequestAnswerDto dto = new ItemRequestAnswerDto();
                dto.setItemId(itemRequestAnswer.getId());
                dto.setName(itemRequestAnswer.getName());
                dto.setOwnerId(itemRequestAnswer.getOwner().getId());
                return dto;
            }).toList();
        } else {
            return new ArrayList<>();
        }
    }
}










