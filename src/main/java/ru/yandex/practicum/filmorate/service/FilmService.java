package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
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
public class FilmService {
    private int generatorID = 0;

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    private int generateID() {
        return ++generatorID;
    }

    public Film addFilm(Film film) {
        Film newFilm = film.toBuilder().id(generateID()).build();
        log.info("Добавлен новый фильм: {}", newFilm);
        return filmStorage.addFilm(newFilm);
    }

    public Film findFilm(Integer id) {
        Film findFilm = filmStorage.findFilm(id);
        if (findFilm == null) {
            log.warn("Ошибка при выполнении запроса. Фильм с id = {} не существует", id);
            throw new FilmNotFoundException("Фильм с id = " + id + " не найден");
        }
        return findFilm;
    }

    public Film updateFilm(Film film) {
        Film updateFilm = findFilm(film.getId());
        log.info("Обновлен фильм: {}", film);
        return filmStorage.addFilm(film);
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
        log.info("Пользователь ID = {} поставил отметку 'нравится' фильму ID = {}", userID, filmID);
        findFilmForAddLike(filmID, userID).getLike().add(userID);
    }

    public void deleteLikeFilm(Integer filmID, Integer userID) {
        log.info("Пользователь ID = {} удалил отметку 'нравится' фильму ID = {}", userID, filmID);
        findFilmForAddLike(filmID, userID).getLike().remove(userID);
    }

    private Film findFilmForAddLike(Integer filmID, Integer userID) {
        Film film = filmStorage.findFilm(filmID);
        User user = userStorage.findUser(userID);
        if (film == null) {
            log.warn("Ошибка при выполнении запроса. Фильм с id = {} не существует", filmID);
            throw new FilmNotFoundException("Фильм с id = " + filmID + " не найден");
        }
        if (user == null) {
            log.warn("Ошибка при выполнении запроса. Пользователь с id = {} не существует", userID);
            throw new UserNotFoundException("Пользователь с id = " + userID + " не найден");
        }
        return film;
    }

    public List<Film> getPopularFilmList(Integer count) {
        return filmStorage.getAllFilm().stream()
                .sorted(Comparator.comparing(film -> ((Film) film).getLike().size()).reversed())
                .limit(count).collect(Collectors.toList());
    }
}
