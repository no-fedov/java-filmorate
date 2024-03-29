package ru.yandex.practicum.filmorate.storage.like_mark_film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;


@Repository("likeMarkDbStorage")
@RequiredArgsConstructor
public class LikeMarkDbStorage implements LikeMarkStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLikeFilm(int filmId, int userId) {
        String sqlQueryForLikeMark = "INSERT INTO like_mark_film (film_id, user_id) VALUES (?, ?)";

        jdbcTemplate.update(sqlQueryForLikeMark, filmId, userId);

        String sqlQueryForLikeToFilm = "UPDATE film SET rate = rate + 1 WHERE id = ?";

        jdbcTemplate.update(sqlQueryForLikeToFilm, filmId);
    }

    @Override
    public void removeLikeFilm(int filmId, int userId) {
        String sqlQueryForUnLikeMark = "DELETE FROM like_mark_film WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(sqlQueryForUnLikeMark, filmId, userId);

        String sqlQueryForUnLikeToFilm = "UPDATE film SET rate = rate - 1 WHERE id = ?";

        jdbcTemplate.update(sqlQueryForUnLikeToFilm, filmId);
    }
}
