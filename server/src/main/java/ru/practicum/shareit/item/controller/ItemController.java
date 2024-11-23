package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
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

        log.info("пришел Get запрос /items");
        Collection<ItemDto> items = itemService.findAll(userId);
        log.info("Отправлен ответ Get /items с телом: {}", items);
        return items;
    }

    @PostMapping
    public ItemDto create(@RequestBody NewItemRequest newItemRequest, @RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("пришел Post запрос /items с телом: {}", newItemRequest);

        ItemDto newItem = itemService.create(newItemRequest, userId);
        log.info("Отправлен ответ Post /items с телом: {}", newItem);
        return newItem;
    }

    @GetMapping("/{id}")
    public ItemDto findItem(@PathVariable long id) {

        log.info("пришел Get запрос items/{}", id);
        ItemDto item = itemService.findById(id);
        log.info("Отправлен ответ Get с телом: {}", item);
        return item;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody UpdateItemRequest updateItemRequest, @PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("пришел PATCH запрос /items/{} с телом: {}", itemId, updateItemRequest);
        ItemDto item = itemService.update(updateItemRequest, userId, itemId);
        log.info("Отправлен ответ PATCH /items с телом: {}", item);
        return item;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestBody NewCommentRequest newCommentRequest, @PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("пришел POST запрос /items/{}/comment с телом: {}", itemId, newCommentRequest);
        CommentDto comment = itemService.addComment(newCommentRequest, userId, itemId);
        log.info("Отправлен ответ POST /items с телом: {}", comment);
        return comment;
    }

    @GetMapping("/search")
    public Collection<ItemDto> findByText(@RequestParam(name = "text") String text) {
        log.info("пришел GET запрос /items/search параметром {}", text);
        Collection<ItemDto> items =  itemService.findByText(text);
        log.info("Отправлен ответ PATCH /items с телом: {}", items);
        return items;
    }
}