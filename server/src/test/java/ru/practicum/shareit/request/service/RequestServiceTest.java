package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestAnswer;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    private ItemRequestService itemRequestService;

    @Mock
    private UserRepository userStorage;

    @Mock
    private ItemRequestRepository itemRequestStorage;

    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    public void setUp() {
        itemRequestService = new ItemRequestService(userStorage, itemRequestStorage, itemRepository);
    }

    @Test
    public void createTest() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setId(1L);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setRequester(user);
        itemRequest.setCreated(Instant.now());
        itemRequest.setDescription("Test Description");


        when(userStorage.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestStorage.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDto itemRequestDto = itemRequestService.create(new NewRequest(), 1L);
        assertEquals(1L,  itemRequestDto.getId());

        verify(itemRequestStorage).save(any(ItemRequest.class));
    }

    @Test
    public void findAllByUserIdTest() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setId(1L);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setRequester(user);
        itemRequest.setCreated(Instant.now());
        itemRequest.setDescription("Test Description");

        when(userStorage.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestStorage.findItemRequestsByRequesterIdOrderByCreatedDesc(anyLong())).thenReturn(List.of(itemRequest));

        Collection<ItemRequestDto> itemRequestDtos = itemRequestService.findAllByUserId(1L);
        assertEquals(1,  itemRequestDtos.size());

        verify(itemRequestStorage).findItemRequestsByRequesterIdOrderByCreatedDesc(anyLong());
    }

    @Test
    public void findAllOtherUsersTest() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setId(1L);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setRequester(user);
        itemRequest.setCreated(Instant.now());
        itemRequest.setDescription("Test Description");

        when(itemRequestStorage.findItemRequestsByRequesterIdNotOrderByCreatedDesc(anyLong())).thenReturn(List.of(itemRequest));

        Collection<ItemRequestDto> itemRequestDtos = itemRequestService.findAllOtherUsers(1L);
        assertEquals(1,  itemRequestDtos.size());

        verify(itemRequestStorage).findItemRequestsByRequesterIdNotOrderByCreatedDesc(anyLong());
    }

    @Test
    public void findByIdTest() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setId(1L);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setRequester(user);
        itemRequest.setCreated(Instant.now());
        itemRequest.setDescription("Test Description");

        ItemRequestAnswer itemRequestAnswer = new ItemRequestAnswer() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getName() {
                return "Name";
            }

            @Override
            public User getOwner() {
                return user;
            }

            @Override
            public ItemRequest getItemRequest() {
                return itemRequest;
            }
        };

        when(itemRequestStorage.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByItemRequestId(anyLong())).thenReturn(List.of(itemRequestAnswer));

        ItemRequestDto itemRequestDto = itemRequestService.findById(1L);
        assertEquals(1L,  itemRequestDto.getId());

        verify(itemRepository).findAllByItemRequestId(anyLong());
    }
}
