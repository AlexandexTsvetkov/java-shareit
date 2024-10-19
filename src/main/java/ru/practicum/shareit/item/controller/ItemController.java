package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.InternalServerException;
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
    ItemService itemService;

    @GetMapping
    public Collection<ItemDto> findAll(@RequestHeader HttpHeaders headers) {

        log.info("пришел Get запрос /users");
        String textUserId = headers.getFirst("X-Sharer-User-Id");

        if (textUserId == null) {
            throw (new InternalServerException("Не передано значение заголовка X-Sharer-User-Id"));
        }
        Collection<ItemDto> items = itemService.findAll(Long.parseLong(textUserId));
        log.info("Отправлен ответ Get /users с телом: {}", items);
        return items;
    }

    @PostMapping
    public ItemDto create(@Valid @RequestBody NewItemRequest newItemRequest, @RequestHeader HttpHeaders headers) {

        log.info("пришел Post запрос /items с телом: {}", newItemRequest);

        String textUserId = headers.getFirst("x-sharer-user-id");

        if (textUserId == null) {
            throw (new InternalServerException("Не передано значение заголовка X-Sharer-User-Id"));
        }
        ItemDto newItem = itemService.create(newItemRequest, Long.parseLong(textUserId));
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
    public ItemDto update(@Valid @RequestBody UpdateItemRequest updateItemRequest, @PathVariable long itemId, @RequestHeader HttpHeaders headers) {

        log.info("пришел PUT запрос /users с телом: {}", updateItemRequest);

        String textUserId = headers.getFirst("X-Sharer-User-Id");

        if (textUserId == null) {
            throw (new InternalServerException("Не передано значение заголовка X-Sharer-User-Id"));
        }
        ItemDto item = itemService.update(updateItemRequest, Long.parseLong(textUserId), itemId);
        log.info("Отправлен ответ PUT /users с телом: {}", item);
        return item;
    }

    @GetMapping("/search")
    public Collection<ItemDto> findByText(@RequestParam(name = "text") @NotNull String text) {
        return itemService.findByText(text);
    }
}