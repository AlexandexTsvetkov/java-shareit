package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static Item mapToItem(NewItemRequest request) {

        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setOwner(request.getOwner());
        item.setAvailable(request.getAvailable());
        item.setOwner(request.getOwner());

        return item;
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










