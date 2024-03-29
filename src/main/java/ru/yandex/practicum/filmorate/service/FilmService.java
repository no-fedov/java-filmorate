package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotExistEntity;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.MPA.MPAStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film_genre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like_mark_film.LikeMarkStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;


import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    @Qualifier("filmDbStorage")
    private final FilmStorage filmDbStorage;

    @Qualifier("userDbStorage")
    private final UserStorage userDbStorage;

    private final GenreStorage genreStorage;

    private final FilmGenreStorage filmGenreStorage;

    private final MPAStorage mpaStorage;

    private final LikeMarkStorage likeMarkStorage;

    public Film addFilm(Film film) {
        Set<Genre> genres = findGenresToFilm(film); // ищем жанры из базы данных жанров
        MPA mpa = findMPAtoFilm(film); // ищем МПА из базы данных МПА

        // записываем фильм в базу данных и присваиваем id
        Film newFilm = filmDbStorage.addFilm(film);

        // добавить запись в таблицу жанры_фильмов, и изменить объект film
        filmGenreStorage.addGenreToFilm(newFilm, genres);

        newFilm.setMpa(mpa);
        newFilm.addGenre(genres);

        log.info("Добавлен новый фильм: {}", newFilm);
        return newFilm;
    }

    public Film findFilm(Integer id) {
        Film findFilm = filmDbStorage.findFilm(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + id + " не найден"));

        Set<Genre> genres = filmGenreStorage.findGenreOfFilm(id);
        MPA mpa = mpaStorage.findMPAofFilm(id);
        findFilm.addGenre(genres);
        findFilm.setMpa(mpa);

        log.info("Обработан запрос на по поиску фильма. Найден фильм: {}.", findFilm);
        return findFilm;
    }

    public Film updateFilm(Film film) {
        findFilm(film.getId());

        filmGenreStorage.removeGenreFromFilm(film.getId());
        filmDbStorage.updateFilm(film);
        filmGenreStorage.addGenreToFilm(film, film.getGenres());

        log.info("Обновлен фильм: {}", film);
        return film;
    }

    public Film deleteFilm(Integer id) {
        Film filmForDelete = findFilm(id);

        filmDbStorage.deleteFilm(id);

        log.info("Фильм удален {}", filmForDelete);
        return filmForDelete;
    }

    public List<Film> getAllFilm() {

        List<Film> listFilm = filmDbStorage.getAllFilm().stream().
                peek(film -> {
                    film.addGenre(filmGenreStorage.findGenreOfFilm(film.getId()));
                    film.setMpa(mpaStorage.findMPAofFilm(film.getId()));
                })
                .collect(Collectors.toList());

        log.info("Запрос на список всех фильмов");
        return listFilm;
    }

    public void addLikeFilm(Integer filmID, Integer userID) {
        likeMarkStorage.addLikeFilm(filmID, userID);
        log.info("Пользователь ID = {} поставил отметку 'нравится' фильму ID = {}", userID, filmID);
    }

    public void deleteLikeFilm(Integer filmID, Integer userID) {
        likeMarkStorage.removeLikeFilm(filmID, userID);
        log.info("Пользователь ID = {} удалил отметку 'нравится' фильму ID = {}", userID, filmID);
    }

    public List<Film> getPopularFilmList(Integer count) {
        List<Film> listFilm = filmDbStorage.getAllPopularFilm(count).stream().
                peek(film -> {
                    film.addGenre(filmGenreStorage.findGenreOfFilm(film.getId()));
                    film.setMpa(mpaStorage.findMPAofFilm(film.getId()));
                })
                .collect(Collectors.toList());
        log.info("Обработан запрос на получение всех фильмов");
        return listFilm;
    }

    public List<Film> findFilmByCondition(Predicate<Film> condition) {
        return filmDbStorage.findFilmByCondition(condition);
    }

    private Film findFilmForAddLike(Integer filmID, Integer userID) {
        Film film = findFilm(filmID);
        User user = userDbStorage.findUser(userID)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userID + "не найден"));
        return film;
    }

    private MPA findMPAtoFilm(Film film) {
        // проверить МПА рейтинг на существование
        int mpaID = film.getMpa().getId();
        MPA mpa = mpaStorage.findMPA(mpaID).
                orElseThrow(() -> new NotExistEntity("При добавлении фильма вы указали несуществующий MPA рейтинг"));
        return mpa;
    }

    private Set<Genre> findGenresToFilm(Film film) {
        // проверяем существование жанров
        Set<Genre> genres = new HashSet<>();

        for (Genre genre : film.getGenres()) {
            genres.add(genreStorage.findGenre(genre.getId()).
                    orElseThrow(() -> new NotExistEntity("При добавлении фильма вы указали несуществующий жанр")));
        }

        film.addGenre(genres);
        return genres;
    }
}
