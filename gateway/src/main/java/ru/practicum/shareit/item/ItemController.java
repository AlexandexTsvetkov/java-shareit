package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("Get items, userId={}", userId);
        return itemClient.findAll(userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody NewItemDto newItemRequest, @RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("Post item {}, userId={}", newItemRequest, userId);
        return itemClient.addItem(userId, newItemRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findItem(@PathVariable long id) {
        log.info("Get item, id={}", id);
        return itemClient.getItem(id);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@Valid @RequestBody UpdateItemDto updateItemRequest, @PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Patch item {}, itemId={}, userId={}", updateItemRequest, itemId, userId);
        return itemClient.updateItem(userId, updateItemRequest, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Valid @RequestBody CommentDto newCommentRequest, @PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Post comment {}, itemId={}, userId={}", newCommentRequest, itemId, userId);
        return itemClient.addComment(itemId, userId, newCommentRequest);
    }

    @GetMapping("/search")
    public ResponseEntity<Object>  findByText(@RequestParam(name = "text") @NotNull String text) {
        log.info("Get search, text={}", text);
        return itemClient.findByText(text);
    }
}
