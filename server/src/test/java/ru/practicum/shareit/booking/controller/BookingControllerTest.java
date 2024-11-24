package ru.practicum.shareit.booking.controller;


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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createTest() throws Exception {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setId(1L);

        Item item = new Item();
        item.setName("Test Item");
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("Some descrioption");
        item.setName("Some name");
        item.setAvailable(true);

        NewBookingRequest newBookingRequest = new NewBookingRequest();
        newBookingRequest.setItemId(item.getId());
        newBookingRequest.setBooker(user);
        newBookingRequest.setStatus(BookingStatus.APPROVED);
        newBookingRequest.setStart(LocalDateTime.now().plusMinutes(1));
        newBookingRequest.setEnd(LocalDateTime.now().plusMinutes(2));

        when(bookingService.create(Mockito.any(NewBookingRequest.class), Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    BookingDto bookingDto = new BookingDto();
                    bookingDto.setId(1);
                    return bookingDto;
                });

        mvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(newBookingRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1), Integer.class));
    }

    @Test
    public void findAllTest() throws Exception {

        when(bookingService.findAllByUser(Mockito.anyLong(), Mockito.any(State.class)))
                .thenAnswer(invocationOnMock -> {
                    BookingDto bookingDto = new BookingDto();
                    bookingDto.setId(1);
                    return List.of(bookingDto);
                });

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void findAllByOwnerTest() throws Exception {

        when(bookingService.findAllByOwner(Mockito.anyLong(), Mockito.any(State.class)))
                .thenAnswer(invocationOnMock -> {
                    BookingDto bookingDto = new BookingDto();
                    bookingDto.setId(1);
                    return List.of(bookingDto);
                });

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void findBookingTest() throws Exception {

        when(bookingService.getBooking(Mockito.anyLong(), Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    BookingDto bookingDto = new BookingDto();
                    bookingDto.setId(1);
                    return bookingDto;
                });

        mvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void approveTest() throws Exception {
        when(bookingService.approve(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenAnswer(invocationOnMock -> {
                    BookingDto bookingDto = new BookingDto();
                    bookingDto.setId(1);
                    return bookingDto;
                });

        mvc.perform(patch("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }
}