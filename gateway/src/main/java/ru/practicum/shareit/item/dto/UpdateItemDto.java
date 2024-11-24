package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateItemDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @Size(max = 100, message = "Длина должна быть не более 100 символов")
    private String name;

    @Size(max = 200, message = "Длина должна быть не более 200 символов")
    private String description;

    private Boolean available;
}
