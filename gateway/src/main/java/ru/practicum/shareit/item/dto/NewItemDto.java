package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewItemDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    @NotBlank(message = "Название не может быть пустым или содержать только пробелы")
    @Size(max = 150, message = "Длина должна быть не более 150 символов")
    private String name;

    @NotNull
    @Size(max = 200, message = "Длина должна быть не более 200 символов")
    private String description;

    @NotNull
    private Boolean available;

    private Long requestId;
}
