package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestAnswer;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestService {

    @Autowired
    private final UserRepository userStorage;

    @Autowired
    private final ItemRequestRepository itemRequestStorage;

    @Autowired
    private final ItemRepository itemRepository;

    @Transactional
    public ItemRequestDto create(NewRequest newRequest, long userId) {

        Optional<User> user = userStorage.findById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Пользователь с id {0, number} не найден", userId));
        }

        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(newRequest, user.get());

        return ItemRequestMapper.mapItemRequestDto(itemRequestStorage.save(itemRequest));
    }

    public Collection<ItemRequestDto> findAllByUserId(long userId) {

        Optional<User> user = userStorage.findById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Пользователь с id {0, number} не найден", userId));
        }

        List<ItemRequest> requests = itemRequestStorage.findItemRequestsByRequesterIdOrderByCreatedDesc(userId);
        return addAnswers(requests);

    }

    public Collection<ItemRequestDto> findAllOtherUsers(long userId) {

        List<ItemRequest> requests = itemRequestStorage.findItemRequestsByRequesterIdNotOrderByCreatedDesc(userId);

        return addAnswers(requests);
    }

    private Collection<ItemRequestDto> addAnswers(List<ItemRequest> requests) {

        List<Long> requestsIds = requests.stream().map(ItemRequest::getId).toList();

        final Map<ItemRequest, List<ItemRequestAnswer>> answersByItemRequests = itemRepository.findAllByItemRequestIdIn(requestsIds)
                .stream()
                .collect(Collectors.groupingBy(
                        ItemRequestAnswer::getItemRequest));

        return requests.stream().map(request -> ItemRequestMapper.mapItemRequestDto(request, answersByItemRequests.get(request))).toList();
    }

    public ItemRequestDto findById(long id) {
        Optional<ItemRequest> itemRequest = itemRequestStorage.findById(id);

        if (itemRequest.isEmpty()) {
            throw new NotFoundException("Запрос вещи с id " + id + " не найден.");
        }

        List<ItemRequestAnswer> answers = itemRepository.findAllByItemRequestId(id);

        return ItemRequestMapper.mapItemRequestDto(itemRequest.get(), answers);
    }
}
