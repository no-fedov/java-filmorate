package ru.yandex.practicum.filmorate.storage.MPA;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository("mpaDbStorage")
@RequiredArgsConstructor
public class MPADbStorage implements MPAStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public MPA addMPA(MPA mpa) {
        String sqlQuery = "INSERT INTO rating_mpa(name) " +
                "VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, mpa.getName());
            return stmt;
        }, keyHolder);

        int id = keyHolder.getKey().intValue();

        return mpa.toBuilder().id(id).build();
    }

    @Override
    public MPA deleteMPA(int id) {
        MPA genre = findMPA(id).get();
        jdbcTemplate.update("DELETE FROM rating_mpa WHERE id = ?", id);
        return genre;
    }

    @Override
    public Optional<MPA> findMPA(int id) {
        List<MPA> mpaList = jdbcTemplate.query("SELECT * FROM rating_mpa WHERE id = ?",
                new Object[]{id},
                this::mapRowToMPA);
        return mpaList.stream().findFirst();
    }

    @Override
    public MPA updateMPA(MPA mpa) {
        String sqlQuery = "UPDATE rating_mpa " +
                "SET name = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(sqlQuery, mpa.getName());

        return findMPA(mpa.getId()).get();
    }

    @Override
    public List<MPA> getAllMPA() {
        return jdbcTemplate.query("SELECT * FROM rating_mpa ORDER BY id", this::mapRowToMPA);
    }

    @Override
    public MPA findMPAofFilm(int filmID) {
        String sqlQuery = "SELECT rm.id , rm.name " +
                "FROM film AS f " +
                "LEFT JOIN rating_mpa AS rm ON f.rating_id = rm.id " +
                "WHERE f.id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, filmID);
        MPA mpa = null;

        if (rs.next()) {
            mpa = MPA.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .build();
        }
        return mpa;
    }

    private MPA mapRowToMPA(ResultSet resultSet, int rowNum) throws SQLException {
        return MPA.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
