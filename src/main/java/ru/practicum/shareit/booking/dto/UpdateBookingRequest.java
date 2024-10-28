package ru.practicum.shareit.booking.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Data
public class UpdateBookingRequest {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Future(message = "Дата начала должна быть в будущем")
    private LocalDateTime start;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Future(message = "Дата окончания должна быть в будущем")
    private LocalDateTime end;

    @NotNull
    private BookingStatus status;

    public boolean hasStart() {
        return !(start == null);
    }

    public boolean hasEnd() {
        return !(end == null);
    }

    public boolean hasStatus() {
        return !(status == null);
    }
}
