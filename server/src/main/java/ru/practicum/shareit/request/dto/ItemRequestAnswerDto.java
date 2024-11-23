package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ItemRequestAnswerDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long itemId;

    @NotNull
    @Size(max = 150, message = "Длина должна быть не более 150 символов")
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long ownerId;
}
