package ru.yandex.practicum.filmorate.storage.film_genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository("filmGenreDbStorage")
@RequiredArgsConstructor
public class FilmGenreDbStorage implements FilmGenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addGenreToFilm(Film film, Set<Genre> genres) {

        if (genres == null) {
            return;
        }

        String sqlQuery = "INSERT INTO film_genre (film_id, genre_id) VALUES (? , ?)";

        List<Genre> listOfGenre = new ArrayList<>(genres);

        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, film.getId());
                ps.setInt(2, listOfGenre.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
    }

    @Override
    public Map<Integer, Set<Genre>> findGenreOfFilm(List<Film> films) {

        Map<Integer, Set<Genre>> listGenre = new HashMap<>();

        List<Integer> filmsID = films.stream().map(Film::getId).collect(Collectors.toList());

        String placeholders = String.join(",", Collections.nCopies(filmsID.size(), "?"));

        String sqlQuery = String.format("SELECT fg.film_id, fg.genre_id, g.name " +
                "FROM film_genre AS fg " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.id " +
                "WHERE film_id IN (%s) " +
                "ORDER BY fg.film_id, fg.genre_id", placeholders);

        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, filmsID.toArray());

        while (rs.next()) {
            int filmID = rs.getInt("film_id");

            Genre genre = Genre.builder()
                    .id(rs.getInt("genre_id"))
                    .name(rs.getString("name"))
                    .build();

            listGenre.computeIfAbsent(filmID, k -> new LinkedHashSet<>()).add(genre);
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

        if(genres == null) {
            return new LinkedHashSet<>();
        }

        String sqlQuery = "DELETE FROM film_genre WHERE film_id = ?, genre_id = ?)";

        List<Genre> listOfGenre = new ArrayList<>(genres);

        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, film.getId());
                ps.setInt(2, listOfGenre.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
        return new LinkedHashSet<>(genres);
    }
}