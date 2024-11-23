package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class ItemMapper {

    public static Item mapToItem(NewItemRequest request, User owner, ItemRequest itemRequest) {

        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setOwner(request.getOwner());
        item.setAvailable(request.getAvailable());
        item.setOwner(owner);
        item.setItemRequest(itemRequest);

        return item;
    }

    public static ItemDto mapItemDto(Item item, List<CommentDto> comments) {

        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setDescription(item.getDescription());
        dto.setName(item.getName());
        dto.setAvailable(item.getAvailable());
        dto.setRetailsNumber(item.getRetailsNumber());
        dto.setComments(comments);

        return dto;
    }

    public static ItemDto mapItemDto(Item item, List<CommentDto> comments, Instant lastBooking, Instant nextBooking) {

        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setDescription(item.getDescription());
        dto.setName(item.getName());
        dto.setAvailable(item.getAvailable());
        dto.setRetailsNumber(item.getRetailsNumber());
        dto.setComments(comments);
        dto.setLastBooking((lastBooking == null) ? null : LocalDateTime.ofInstant(lastBooking, ZoneId.systemDefault()));
        dto.setNextBooking((nextBooking == null) ? null : LocalDateTime.ofInstant(nextBooking, ZoneId.systemDefault()));

        return dto;
    }

    public static ItemDto mapItemDto(Item item) {

        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setDescription(item.getDescription());
        dto.setName(item.getName());
        dto.setAvailable(item.getAvailable());
        dto.setRetailsNumber(item.getRetailsNumber());

        return dto;
    }

    public static Item updateItemFields(Item item, UpdateItemRequest request) {
        if (request.hasName()) {
            item.setName(request.getName());
        }
        if (request.hasDescription()) {
            item.setDescription(request.getDescription());
        }
        if (request.hasAvailable()) {
            item.setAvailable(request.getAvailable());
        }

        return item;
    }
}










