package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.function.Predicate;

@Repository
class InMemoryFilmStorage implements FilmStorage {

    private int generatorID = 0;
    private final Map<Integer, Film> filmStorage = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        Film newFilm = film.toBuilder().id(generateID()).build();
        filmStorage.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film deleteFilm(Integer id) {
        return filmStorage.remove(id);
    }

    @Override
    public Optional<Film> findFilm(Integer id) {
        return Optional.ofNullable(filmStorage.get(id));
    }

    @Override
    public Film updateFilm(Film film) {
        filmStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAllFilm() {
        return new ArrayList<>(filmStorage.values());
    }

    @Override
    public List<Film> findFilmByCondition(Predicate<Film> condition) {
        List<Film> films = new ArrayList<>();
        for (Film film : filmStorage.values()) {
            if (condition.test(film)) {
                films.add(film);
            }
        }
        return films;
    }

    private int generateID() {
        return ++generatorID;
    }
}
