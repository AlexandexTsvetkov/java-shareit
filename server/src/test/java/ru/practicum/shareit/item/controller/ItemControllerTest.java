package ru.practicum.shareit.item.controller;

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
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createTest() throws Exception {

        User user = new User();
        user.setName("Test User8");
        user.setEmail("test8@test.com");
        user.setId(1L);

        NewItemRequest newItemRequest = new NewItemRequest();
        newItemRequest.setName("Test Item8");
        newItemRequest.setDescription("Test Description8");
        newItemRequest.setAvailable(true);
        newItemRequest.setOwner(user);

        when(itemService.create(Mockito.any(NewItemRequest.class), Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    ItemDto itemDto = new ItemDto();
                    itemDto.setId(1);
                    return itemDto;
                });

        mvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(newItemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1), Integer.class));
    }

    @Test
    public void findAllTest() throws Exception {

        when(itemService.findAll(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    ItemDto itemDto = new ItemDto();
                    itemDto.setId(1);
                    return List.of(itemDto);
                });

        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void findItemTest() throws Exception {

        when(itemService.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    ItemDto itemDto = new ItemDto();
                    itemDto.setId(1);
                    return itemDto;
                });

        mvc.perform(get("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void updateTest() throws Exception {

        UpdateItemRequest updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setName("Test Item13");

        when(itemService.update(Mockito.any(UpdateItemRequest.class), Mockito.anyLong(), Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    ItemDto itemDto = new ItemDto();
                    itemDto.setId(1);
                    return itemDto;
                });

        mvc.perform(patch("/items/1")
                        .content(objectMapper.writeValueAsString(updateItemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void newCommentTest() throws Exception {

        NewCommentRequest newCommentRequest = new NewCommentRequest();
        newCommentRequest.setText("Test Item13");

        when(itemService.addComment(Mockito.any(NewCommentRequest.class), Mockito.anyLong(), Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    CommentDto commentDto = new CommentDto();
                    commentDto.setId(1L);
                    return commentDto;
                });

        mvc.perform(post("/items/1/comment")
                        .content(objectMapper.writeValueAsString(newCommentRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void findByTextTest() throws Exception {

        when(itemService.findByText(Mockito.anyString()))
                .thenAnswer(invocationOnMock -> {
                    ItemDto itemDto = new ItemDto();
                    itemDto.setId(1);
                    return List.of(itemDto);
                });

        mvc.perform(get("/items/search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", "Test"))
                .andExpect(status().isOk());
    }
}