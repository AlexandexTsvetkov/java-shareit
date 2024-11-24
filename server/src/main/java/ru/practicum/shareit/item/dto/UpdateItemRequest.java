package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

@Data
public class UpdateItemRequest {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    private String name;

    private String description;

    private Boolean available;

    @JsonIgnore
    private User owner;

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

    public boolean hasAvailable() {
        return !(available == null);
    }
}

