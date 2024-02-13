package ru.yandex.practicum.filmorate.integration.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.integration.exception.ValidationException;
import ru.yandex.practicum.filmorate.integration.model.Film;
import ru.yandex.practicum.filmorate.integration.validator.FilmValidator;

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
    public ResponseEntity<Film> postFilm(@Valid @RequestBody Film film) {
        log.info(text, "Добавить фильм", film);
        FilmValidator.verify(film);
        Film film1 = film.toBuilder().id(++generatorID).build();
        dataFilm.put(generatorID, film1);
        log.info("Добавлен фильм: " + film1);
        return new ResponseEntity<>(film1, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Film> putFilm(@Valid @RequestBody Film film) {
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
        return new ResponseEntity<>(film, HttpStatus.OK);
    }
}

