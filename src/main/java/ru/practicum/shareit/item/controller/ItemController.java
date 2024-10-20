package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    @Autowired
    private final ItemService itemService;

    @GetMapping
    public Collection<ItemDto> findAll(@RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("пришел Get запрос /users");
        Collection<ItemDto> items = itemService.findAll(userId);
        log.info("Отправлен ответ Get /users с телом: {}", items);
        return items;
    }

    @PostMapping
    public ItemDto create(@Valid @RequestBody NewItemRequest newItemRequest, @RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("пришел Post запрос /items с телом: {}", newItemRequest);

        ItemDto newItem = itemService.create(newItemRequest, userId);
        log.info("Отправлен ответ Post /items с телом: {}", newItem);
        return newItem;
    }

    @GetMapping("/{id}")
    public ItemDto findItem(@PathVariable long id) {

        log.info("пришел Get запрос /{}", id);
        ItemDto item = itemService.findById(id);
        log.info("Отправлен ответ Get /films с телом: {}", item);
        return item;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@Valid @RequestBody UpdateItemRequest updateItemRequest, @PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("пришел PUT запрос /users с телом: {}", updateItemRequest);
        ItemDto item = itemService.update(updateItemRequest, userId, itemId);
        log.info("Отправлен ответ PUT /users с телом: {}", item);
        return item;
    }

    @GetMapping("/search")
    public Collection<ItemDto> findByText(@RequestParam(name = "text") @NotNull String text) {
        return itemService.findByText(text);
    }
}