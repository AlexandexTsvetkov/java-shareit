package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

@Slf4j
@RestController
@RequestMapping("/requests")
public class ItemRequestController {

    ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@Valid @RequestBody ItemRequestDto itemRequestDto, @RequestHeader HttpHeaders headers) {

        log.info("пришел Post запрос /requests с телом: {}", itemRequestDto);

        String textUserId = headers.getFirst("x-sharer-user-id");

        if (textUserId == null) {
            throw (new InternalServerException("Не передано значение заголовка X-Sharer-User-Id"));
        }

        ItemRequestDto newItemRequestDto = itemRequestService.create(itemRequestDto, Long.parseLong(textUserId));
        log.info("Отправлен ответ Post /requests с телом: {}", itemRequestDto);
        return newItemRequestDto;
    }
}
