package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String email;

    private String name;

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasEmail() {
        return !(email == null || email.isBlank());
    }
}