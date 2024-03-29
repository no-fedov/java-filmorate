package ru.yandex.practicum.filmorate.storage.like_mark_film;

public interface LikeMarkStorage {
    void addLikeFilm(int film_id, int user_id);
    void removeLikeFilm(int film_id, int user_id);
}
