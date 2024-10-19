package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

@Data
public class Item {

    private long id;

    @NotNull
    private User owner;

    @NotBlank(message = "Название не может быть пустым или содержать только пробелы")
    @Size(max = 100, message = "Длина должна быть не более 100 символов")
    private String name;

    @Size(max = 200, message = "Длина должна быть не более 200 символов")
    private String description;

    @NotNull
    private Boolean available;

    private int retailsNumber;
}
