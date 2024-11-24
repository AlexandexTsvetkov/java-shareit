package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createTest() throws Exception {

        NewRequest itemRequest = new NewRequest();
        itemRequest.setDescription("ds");

        when(itemRequestService.create(Mockito.any(NewRequest.class), Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    ItemRequestDto itemRequestDto = new ItemRequestDto();
                    itemRequestDto.setId(1);
                    return itemRequestDto;
                });

        mvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(itemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1), Integer.class));
    }

    @Test
    public void findAllByUser() throws Exception {

        NewRequest itemRequest = new NewRequest();
        itemRequest.setDescription("ds");

        when(itemRequestService.findAllByUserId(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    ItemRequestDto itemRequestDto = new ItemRequestDto();
                    itemRequestDto.setId(1);
                    return List.of(itemRequestDto);
                });

        mvc.perform(get("/requests")
                        .content(objectMapper.writeValueAsString(itemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void findAllOtherUsers() throws Exception {

        NewRequest itemRequest = new NewRequest();
        itemRequest.setDescription("ds");

        when(itemRequestService.findAllOtherUsers(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    ItemRequestDto itemRequestDto = new ItemRequestDto();
                    itemRequestDto.setId(1);
                    return List.of(itemRequestDto);
                });

        mvc.perform(get("/requests/all")
                        .content(objectMapper.writeValueAsString(itemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void findRequest() throws Exception {

        NewRequest itemRequest = new NewRequest();
        itemRequest.setDescription("ds");

        when(itemRequestService.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    ItemRequestDto itemRequestDto = new ItemRequestDto();
                    itemRequestDto.setId(1);
                    return itemRequestDto;
                });

        mvc.perform(get("/requests/1")
                        .content(objectMapper.writeValueAsString(itemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }
}