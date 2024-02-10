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
    private final String TEXT = "Получен запрос: {} : {}";
    private int generatorID = 0;

    @GetMapping
    public List<Film> getFilms() {
        log.info(TEXT, "Показать список фильмов", dataFilm.values());
        return new ArrayList<>(dataFilm.values());
    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) {
        try {
            if (FilmValidator.verify(film)) {
                log.info(TEXT, "Добавить фильм", film);
                film.setId(++generatorID);
                dataFilm.put(generatorID, film);
            }
        } catch (ValidationException e) {
            log.info(TEXT, "Добавить фильм: " + film.toString(), e.getMessage());
            throw e;
        }
        return film;
    }

    @PutMapping
    public Film putFilm(@RequestBody Film film) {
        if (dataFilm.get(film.getId()) == null) {
            ValidationException exception = new ValidationException("Нельзя обновить несуществующий фильм");
            log.warn(TEXT, "Обновить фильм", "Ошибка: " + exception.getMessage());
            throw exception;
        } else {
            if (FilmValidator.verify(film)) {
                log.info(TEXT, "Обновить фильм", film);
                dataFilm.put(film.getId(), film);
            }
        }
        return film;
    }
}

