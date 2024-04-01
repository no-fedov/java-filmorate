package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmDbStorage;

    private final UserStorage userDbStorage;

    private final GenreStorage genreStorage;

    private final FilmGenreStorage filmGenreStorage;

    private final MPAStorage mpaStorage;

    private final LikeMarkStorage likeMarkStorage;

    public Film addFilm(Film film) {
        Set<Genre> genres = findGenresToFilm(film).stream()
                .sorted(Comparator.comparing(Genre::getId, Comparator.naturalOrder()))
                .collect(Collectors.toCollection(LinkedHashSet::new)); // ищем жанры из базы данных жанров

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

        Map<Integer, Set<Genre>> genreOfFilms = filmGenreStorage.findGenreOfFilm(List.of(findFilm));

        if (genreOfFilms.get(id) != null) {
            findFilm.addGenre(genreOfFilms.get(id));
        }
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

        List<Film> listFilm = filmDbStorage.getAllFilm();

        Map<Integer, Set<Genre>> mapFilmGenre = filmGenreStorage.findGenreOfFilm(listFilm);

        for (Film film : listFilm) {
            Set<Genre> genres = mapFilmGenre.get(film.getId());
            if (genres != null) {
                film.addGenre(genres);
            }
        }

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
        List<Film> listFilm = filmDbStorage.getAllPopularFilm(count);

        Map<Integer, Set<Genre>> mapFilmGenre = filmGenreStorage.findGenreOfFilm(listFilm);

        for (Film film : listFilm) {
            Set<Genre> genres = mapFilmGenre.get(film.getId());
            if (genres != null) {
                film.addGenre(genres);
            }
        }

        log.info("Обработан запрос на получение популярных фильмов");
        return listFilm;
    }

    private Film findFilmForAddLike(Integer filmID, Integer userID) {
        Film film = findFilm(filmID);
        User user = userDbStorage.findUser(userID)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userID + "не найден"));
        return film;
    }

    private MPA findMPAtoFilm(Film film) {
        int mpaID = film.getMpa().getId();
        MPA mpa = mpaStorage.findMPA(mpaID)
                .orElseThrow(() -> new NotExistEntity("При добавлении фильма вы указали несуществующий MPA рейтинг"));
        return mpa;
    }

    private Set<Genre> findGenresToFilm(Film film) {
        Set<Genre> genres = new HashSet<>();

        for (Genre genre : film.getGenres()) {
            genres.add(genreStorage
                    .findGenre(genre.getId())
                    .orElseThrow(() -> new NotExistEntity("При добавлении фильма вы указали несуществующий жанр")));
        }

        film.addGenre(genres);
        return genres;
    }
}
