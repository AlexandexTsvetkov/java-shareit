package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NewCommentRequest {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    private String text;
}

