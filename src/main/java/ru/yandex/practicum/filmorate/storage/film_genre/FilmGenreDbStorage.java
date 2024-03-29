package ru.yandex.practicum.filmorate.storage.film_genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository("filmGenreDbStorage")
@RequiredArgsConstructor
public class FilmGenreDbStorage implements FilmGenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addGenreToFilm(Film film, Set<Genre> genres) {

        String sqlQuery = "INSERT INTO film_genre (film_id, genre_id) VALUES (? , ?)";

        for (Genre genre : genres) {
            jdbcTemplate.update(sqlQuery,
                    film.getId(), genre.getId());
        }
    }

    @Override
    public Set<Genre> findGenreOfFilm(int id) {

        Set<Genre> listGenre = new HashSet<>();

        String sqlQuery = "SELECT fg.genre_id, g.name " +
                "FROM film_genre AS fg " +
                "LEFT JOIN genre AS g ON fg.GENRE_ID = g.ID " +
                "WHERE film_id = ? " +
                "ORDER BY fg.genre_id";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, id);

        while (rs.next()) {
            listGenre.add(Genre.builder().
                    id(rs.getInt("genre_id")).
                    name(rs.getString("name")).build());
        }

        return listGenre;
    }

    @Override
    public void removeGenreFromFilm(int id) {

        String sqlQuery = "DELETE FROM film_genre " +
                "WHERE film_id = ?";

        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Set<Genre> removeGenreFromFilm(Film film, List<Genre> genres) {

        for (Genre genre : genres) {
            jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?, genre_id = ?)",
                    film.getId(), genre.getId());
        }

        return new HashSet<>(genres);
    }
}
