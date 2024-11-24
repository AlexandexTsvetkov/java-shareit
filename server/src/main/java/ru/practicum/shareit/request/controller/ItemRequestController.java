package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/requests")
@AllArgsConstructor
public class ItemRequestController {

    @Autowired
    private final ItemRequestService itemRequestService;

    @GetMapping
    public Collection<ItemRequestDto> findAllByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("пришел Get запрос /requests");
        Collection<ItemRequestDto> itemRequests = itemRequestService.findAllByUserId(userId);
        log.info("Отправлен ответ Get /requests с телом: {}", itemRequests);
        return itemRequests;
    }

    @PostMapping
    public ItemRequestDto create(@RequestBody NewRequest newRequest, @RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("пришел Post запрос /requests с телом: {}", newRequest);

        ItemRequestDto newItemRequest = itemRequestService.create(newRequest, userId);
        log.info("Отправлен ответ Post /requests с телом: {}", newItemRequest);
        return newItemRequest;
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> findAllOtherUsers(@RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("пришел Get запрос /requests/all");
        Collection<ItemRequestDto> itemRequests = itemRequestService.findAllOtherUsers(userId);
        log.info("Отправлен ответ Get /requests/all с телом: {}", itemRequests);
        return itemRequests;
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findRequest(@PathVariable long requestId) {

        log.info("пришел Get запрос requests/{}", requestId);
        ItemRequestDto itemRequestDto = itemRequestService.findById(requestId);
        log.info("Отправлен ответ Get с телом: {}", itemRequestDto);
        return itemRequestDto;
    }
}