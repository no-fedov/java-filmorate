package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Repository
class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> filmStorage = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        filmStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film deleteFilm(Integer id) {
        return filmStorage.remove(id);
    }

    @Override
    public Film findFilm(Integer id) {
        return filmStorage.get(id);
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
}
