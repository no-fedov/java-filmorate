package ru.yandex.practicum.filmorate.integration.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder(toBuilder = true)
public class Film {
    private final int id;
    @NotBlank(message = "Имя фильма не может быть пустым")
    private String name;
    @Size(min = 0, max = 200, message = "Максимальная длина описания фильма - 200 символов")
    private String description;
    @Past(message = "Этой даты еще не было")
    private LocalDate releaseDate;
    @PositiveOrZero(message = "Продолжительность фильма должна быть положительной")
    private int duration;
}
