package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film addFilm(Film film);

    Film deleteFilm(Integer id);

    Optional<Film> findFilm(Integer id);

    Film updateFilm(Film film);

    List<Film> getAllFilm();

    List<Film> getAllPopularFilm(int count);
}