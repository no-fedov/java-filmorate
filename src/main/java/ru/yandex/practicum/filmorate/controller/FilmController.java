package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> dataFilm = new HashMap<>();
    private final String text = "Получен запрос: {} : {}";
    private int generatorID = 0;

    @GetMapping
    public List<Film> getFilms() {
        log.info(text, "Показать список фильмов", dataFilm.values());
        return new ArrayList<>(dataFilm.values());
    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) {
        log.info(text, "Добавить фильм", film);
        FilmValidator.verify(film);
        Film film1 = film.toBuilder().id(++generatorID).build();
        dataFilm.put(generatorID, film1);
        log.info("Добавлен фильм: " + film1);
        return film1;
    }

    @PutMapping
    public Film putFilm(@RequestBody Film film) {
        log.info(text, "Обновить фильм", film);
        FilmValidator.verify(film);

        if (dataFilm.get(film.getId()) == null) {
            ValidationException exception = new ValidationException("Нельзя обновить несуществующий фильм");
            log.warn(text, "Обновить фильм", "Ошибка: " + exception.getMessage());
            throw exception;
        } else {
            log.info("Обновлен фильм: {}", film);
            dataFilm.put(film.getId(), film);
        }
        return film;
    }
}

