package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface FilmStorage {
    Film addFilm(Film film);

    Film deleteFilm(Integer id);

    Optional<Film> findFilm(Integer id);

    Film updateFilm(Film film);

    List<Film> getAllFilm();

    List<Film> findFilmByCondition(Predicate<Film> condition);
}
