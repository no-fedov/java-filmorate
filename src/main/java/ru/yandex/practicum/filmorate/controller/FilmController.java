package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable Integer id) {
        return filmService.findFilm(id);
    }

    @GetMapping
    public List<Film> getFilms() {
        List<Film> films = filmService.getAllFilm();
        return films;
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLikeFilm(@PathVariable("id") Integer filmID, @PathVariable Integer userId) {
        filmService.addLikeFilm(filmID, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void delLikeFilm(@PathVariable("id") Integer filmID, @PathVariable Integer userId) {
        filmService.deleteLikeFilm(filmID, userId);
    }

    @GetMapping("/popular")
    public  List<Film> getPopularFilms(@RequestParam(defaultValue = "10") @Positive Integer count) {
        return filmService.getPopularFilmList(count);
    }
}

