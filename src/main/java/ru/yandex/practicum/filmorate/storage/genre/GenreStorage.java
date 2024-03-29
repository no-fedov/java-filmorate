package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    Genre addGenre(Genre genre);

    Genre deleteGenre(int id);

    Optional<Genre> findGenre(int id);

    Genre updateGenre(Genre genre);

    List<Genre> getAllGenre();
}
