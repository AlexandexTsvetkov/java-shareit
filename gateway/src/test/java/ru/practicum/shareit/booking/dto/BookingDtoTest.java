package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.json.JsonTest;

import org.springframework.boot.test.json.JacksonTester;

import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoTest {

    @Autowired
    private JacksonTester<BookItemRequestDto> tester;

    @Test
    public void test() throws IOException {

        BookItemRequestDto response = new BookItemRequestDto(LocalDateTime.now().minusMinutes(1), LocalDateTime.now().plusHours(1), 1L);

        JsonContent<BookItemRequestDto> result = tester.write(response);

        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");

        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualToIgnoringCase(response.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));

        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualToIgnoringCase(response.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));

    }

}
