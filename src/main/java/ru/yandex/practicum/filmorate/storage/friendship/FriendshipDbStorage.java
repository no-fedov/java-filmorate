package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository("friendshipDbStorage")
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(int id1, int id2) {
        String sqlQuery = "INSERT INTO friendship(user_id, friend_id) " +
                "VALUES(?, ?)";

        jdbcTemplate.update(sqlQuery, id1, id2);
    }

    @Override
    public void deleteFriend(int id1, int id2) {
        String sqlQuery = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, id1, id2);
    }

    @Override
    public List<User> getUserFriends(int id) {
        String sqlQuery = "SELECT friend_id FROM friendship WHERE user_id = ?";

        return getUsers(sqlQuery, new Object[]{id});
    }

    @Override
    public List<User> getMutualFriendsList(int id1, int id2) {

        String sqlQuery = "SELECT fr1.friend_id " +
                "FROM friendship AS fr1 " +
                "INNER JOIN friendship AS fr2 ON fr1.friend_id = fr2.friend_id " +
                "WHERE fr1.user_id = ? AND fr2.user_id = ?";

        return getUsers(sqlQuery, new Object[]{id1, id2});
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .email(resultSet.getString("email"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

    private List<User> getUsers(String sqlQuery, Object[] queryParam) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQuery, queryParam);

        List<Integer> listID = new ArrayList<>();

        while (sqlRowSet.next()) {
            listID.add(sqlRowSet.getInt("friend_id"));
        }

        String placeholders = String.join(",", Collections.nCopies(listID.size(), "?"));

        return jdbcTemplate.query(String.format("SELECT * FROM user_filmorate WHERE id IN (%s)", placeholders),
                listID.toArray(),
                this::mapRowToUser);
    }
}
