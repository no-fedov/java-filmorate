package ru.yandex.practicum.filmorate.storage.like_mark_film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("likeMarkDbStorage")
@RequiredArgsConstructor
public class LikeMarkDbStorage implements LikeMarkStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLikeFilm(int film_id, int user_id) {
        String sqlQueryForLikeMark = "INSERT INTO like_mark_film (film_id, user_id) VALUES (?, ?)";

        jdbcTemplate.update(sqlQueryForLikeMark, film_id, user_id);

        String sqlQueryForLikeToFilm = "UPDATE film SET rate = rate + 1 WHERE id = ?";

        jdbcTemplate.update(sqlQueryForLikeToFilm, film_id);
    }

    @Override
    public void removeLikeFilm(int film_id, int user_id) {
        String sqlQueryForUnLikeMark = "DELETE FROM like_mark_film WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(sqlQueryForUnLikeMark, film_id, user_id);

        String sqlQueryForUnLikeToFilm = "UPDATE film SET rate = rate - 1 WHERE id = ?";

        jdbcTemplate.update(sqlQueryForUnLikeToFilm, film_id);
    }
}
