package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;


import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film addFilm(Film film) {
        Film newFilm = filmStorage.addFilm(film);
        log.info("Добавлен новый фильм: {}", newFilm);
        return newFilm;
    }

    public Film findFilm(Integer id) {
        Film findFilm = filmStorage.findFilm(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + id + " не найден"));
        log.info("Обработан запрос на по поиску фильма. Найден фильм: {}.", findFilm);
        return findFilm;
    }

    public Film updateFilm(Film film) {
        findFilm(film.getId());
        log.info("Обновлен фильм: {}", film);
        return filmStorage.updateFilm(film);
    }

    public Film deleteFilm(Integer id) {
        Film deletedFilm = findFilm(id);
        log.info("Фильм удален {}", deletedFilm);
        return filmStorage.deleteFilm(id);
    }

    public List<Film> getAllFilm() {
        log.info("Запрос на список всех фильмов");
        return filmStorage.getAllFilm();
    }

    public List<Film> findFilmByCondition(Predicate<Film> condition) {
        return filmStorage.findFilmByCondition(condition);
    }

    public void addLikeFilm(Integer filmID, Integer userID) {
        findFilmForAddLike(filmID, userID).getLike().add(userID);
        log.info("Пользователь ID = {} поставил отметку 'нравится' фильму ID = {}", userID, filmID);
    }

    public void deleteLikeFilm(Integer filmID, Integer userID) {
        findFilmForAddLike(filmID, userID).getLike().remove(userID);
        log.info("Пользователь ID = {} удалил отметку 'нравится' фильму ID = {}", userID, filmID);
    }

    public List<Film> getPopularFilmList(Integer count) {
        return filmStorage.getAllFilm().stream()
                .sorted(Comparator.comparing(film -> ((Film) film).getLike().size()).reversed())
                .limit(count).collect(Collectors.toList());
    }

    private Film findFilmForAddLike(Integer filmID, Integer userID) {
        Film film = findFilm(filmID);
        User user = userStorage.findUser(userID)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userID + "не найден"));
        return film;
    }
}
