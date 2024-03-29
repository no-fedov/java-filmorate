package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository("genreDbStorage")
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre addGenre(Genre genre) {
        String sqlQuery = "INSERT INTO genre(name) " +
                "VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, genre.getName());
            return stmt;
        }, keyHolder);

        int id = keyHolder.getKey().intValue();

        return genre.toBuilder().id(id).build();
    }

    @Override
    public Genre deleteGenre(int id) {
        Genre genre = findGenre(id).get();
        jdbcTemplate.update("DELETE FROM genre WHERE id = ?", id);
        return genre;
    }

    @Override
    public Optional<Genre> findGenre(int id) {
        List<Genre> genres = jdbcTemplate.query("SELECT * FROM genre WHERE id = ?",
                new Object[]{id},
                this::mapRowToGenre);
        return genres.stream().findFirst();
    }

    @Override
    public Genre updateGenre(Genre genre) {
        String sqlQuery = "UPDATE genre " +
                "SET name = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(sqlQuery, genre.getName(), genre.getId());

        return findGenre(genre.getId()).get();
    }

    @Override
    public List<Genre> getAllGenre() {
        return jdbcTemplate.query("SELECT * FROM genre", this::mapRowToGenre);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder().
                id(resultSet.getInt("id")).
                name(resultSet.getString("name")).
                build();
    }
}
