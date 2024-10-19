package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class User {

    private long id;

    @Email
    private String email;

    @NotBlank(message = "Логин не может быть пустым или содержать только пробелы")
    private String name;
}
