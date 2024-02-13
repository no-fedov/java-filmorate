package ru.yandex.practicum.filmorate.integration.validator;


import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.integration.exception.ValidationException;
import ru.yandex.practicum.filmorate.integration.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmValidator {
    private FilmValidator() {
    }

    public static void verify(Film film) {
        String nameFilm = film.getName();

        if (nameFilm == null || nameFilm.isEmpty()) {
            log.warn("Ошибка: Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getDescription().length() > 200) {
            log.warn("Ошибка: Описание фильма не может быть больше 200 символов");
            throw new ValidationException("Описание фильма не может быть больше 200 символов");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата фильма не может быть раньше 28 декабря 1895 года");
            throw new ValidationException("Дата фильма не может быть раньше 28 декабря 1895 года");
        }

        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма не может быть отрицательной");
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        }
    }
}
