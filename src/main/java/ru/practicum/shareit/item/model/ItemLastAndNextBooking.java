package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class ItemLastAndNextBooking {

    private Long itemId;

    private Instant lastBooking;

    private Instant nextBooking;
}
