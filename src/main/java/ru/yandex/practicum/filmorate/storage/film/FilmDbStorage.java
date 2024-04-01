package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addFilm(Film film) {

        Map<String, Object> columns = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("film")
                .usingColumns("name", "description", "duration", "release_date", "rating_id")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKeyHolder(Map.of("name", film.getName(),
                        "description", film.getDescription(),
                        "duration", film.getDuration(),
                        "release_date", Date.valueOf(film.getReleaseDate()),
                        "rating_id", film.getMpa().getId()))
                .getKeys();

        Film createdFilm = film.toBuilder().id((Integer) columns.get("id")).build();

        return createdFilm;
    }

    @Override
    public Film deleteFilm(Integer id) {
        Film deletedFilm = findFilm(id).get();
        String sqlQuery = "DELETE FROM film WHERE id = ?";
        jdbcTemplate.update(sqlQuery, id);
        return deletedFilm;
    }

    @Override
    public Optional<Film> findFilm(Integer id) {

        List<Film> films = jdbcTemplate.query("SELECT f.*, mpa.id AS mpa_id, mpa.name AS mpa_name " +
                        "FROM film f " +
                        "JOIN rating_mpa mpa ON f.rating_id = mpa.id " +
                        "WHERE f.id = ?",
                new Object[]{id},
                this::mapRowToFilm);
        return films.stream().findFirst();
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE film " +
                "SET name = ?, description = ?, duration = ?, release_date = ?, rating_id = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getDuration(), film.getReleaseDate(),
                film.getMpa().getId(), film.getId());

        return film;
    }

    @Override
    public List<Film> getAllFilm() {
        return jdbcTemplate.query("SELECT f.*, mpa.name AS mpa_name " +
                "FROM film AS f " +
                "INNER JOIN rating_mpa AS mpa ON f.rating_id = mpa.id", this::mapRowToFilm);
    }


    //Переписать метод
    public List<Film> getAllPopularFilm(int cout) {
        return jdbcTemplate.query("SELECT f.*, mpa.name AS mpa_name, COUNT(lmf.user_id) AS rating " +
                "FROM film AS f " +
                "INNER JOIN like_mark_film AS lmf ON f.id = lmf.film_id " +
                "INNER JOIN rating_mpa AS mpa ON f.rating_id = mpa.id " +
                "GROUP BY f.id " +
                "ORDER BY rating DESC LIMIT ?", this::mapRowToFilm, cout);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .mpa(MPA.builder().id(resultSet.getInt("rating_id"))
                        .name(resultSet.getString("mpa_name")).build())
                .build();
    }
}