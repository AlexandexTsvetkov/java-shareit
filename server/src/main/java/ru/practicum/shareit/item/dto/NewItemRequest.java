package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

@Data
public class NewItemRequest {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    private String name;

    private String description;

    private Boolean available;

    private Long requestId;

    @JsonIgnore
    private User owner;
}

