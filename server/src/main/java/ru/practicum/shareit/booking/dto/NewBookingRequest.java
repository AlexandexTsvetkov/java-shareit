package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class NewBookingRequest {

    private LocalDateTime start;

    private LocalDateTime end;

    private BookingStatus status;

    private Long itemId;

    @JsonIgnore
    private User booker;
}
