package ru.yandex.practicum.filmorate.storage.like_mark_film;

public interface LikeMarkStorage {
    void addLikeFilm(int filmId, int userId);
    void removeLikeFilm(int filmId, int userId);
}
