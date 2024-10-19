package ru.practicum.shareit.request.model;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class ItemRequest {

    private long id;

    @Size(max = 200, message = "Длина должна быть не более 200 символов")
    private String description;

    @NotNull
    private User requester;

    @NotNull
    private LocalDateTime date;
}
