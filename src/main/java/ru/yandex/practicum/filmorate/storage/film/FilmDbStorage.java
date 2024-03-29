package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Repository("filmDbStorage")
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addFilm(Film film) {

        Map<String, Object> columns = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("film")
                .usingColumns("name", "description", "duration", "release_date", "rating_id", "rate")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKeyHolder(Map.of("name", film.getName(),
                        "description", film.getDescription(),
                        "duration", film.getDuration(),
                        "release_date", Date.valueOf(film.getReleaseDate()),
                        "rating_id", film.getMpa().getId(),
                        "rate", film.getRate()))
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
        List<Film> films = jdbcTemplate.query("SELECT * FROM film WHERE id = ?",
                new Object[]{id},
                this::mapRowToFilm);
        return films.stream().findFirst();
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE film " +
                "SET name = ?, description = ?, duration = ?, release_date = ?, rating_id = ?, rate = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getDuration(), film.getReleaseDate(),
                film.getMpa().getId(), film.getRate(), film.getId());

        return film;
    }

    @Override
    public List<Film> getAllFilm() {
        return jdbcTemplate.query("SELECT * FROM film", this::mapRowToFilm);
    }

    public List<Film> getAllPopularFilm(int cout) {
        return jdbcTemplate.query("SELECT * FROM film ORDER BY rate DESC LIMIT ?", this::mapRowToFilm, cout);
    }

    @Override
    public List<Film> findFilmByCondition(Predicate<Film> condition) {
        return null;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .rate(resultSet.getInt("rate"))
                .build();
    }
}
