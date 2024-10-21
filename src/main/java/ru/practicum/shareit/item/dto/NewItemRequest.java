package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

@Data
public class NewItemRequest {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    @NotBlank(message = "Название не может быть пустым или содержать только пробелы")
    @Size(max = 100, message = "Длина должна быть не более 100 символов")
    private String name;

    @NotNull
    @Size(max = 200, message = "Длина должна быть не более 200 символов")
    private String description;

    @NotNull
    private Boolean available;

    @JsonIgnore
    private User owner;
}

