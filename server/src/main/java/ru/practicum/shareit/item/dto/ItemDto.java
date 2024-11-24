package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    private String name;

    @Size(max = 200, message = "Длина должна быть не более 200 символов")
    private String description;

    @NotNull
    private Boolean available;

    private LocalDateTime lastBooking;

    private LocalDateTime nextBooking;

    private List<CommentDto> comments;
}

