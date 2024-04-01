package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public Genre findGenre(int id) {
        Genre genre = genreStorage.findGenre(id)
                .orElseThrow(() -> new NotFoundException("Жанр с id = " + id + " не найден"));
        log.info("Обработан запрос по поиску жанра с id = {} . Найден жанр: {}", id, genre);
        return genre;
    }

    public List<Genre> getAllGenre() {
        List<Genre> listGenre = genreStorage.getAllGenre();
        log.info("Обработан запрос на получение списка всех жанров. Найдены жанры : {}", listGenre);
        return listGenre;
    }
}
