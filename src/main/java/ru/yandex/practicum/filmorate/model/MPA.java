package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@Builder(toBuilder = true)
public class MPA {
    @Positive
    private final int id;
    @NotBlank
    private String name;
}
