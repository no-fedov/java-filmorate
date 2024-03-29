package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;


import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Repository("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("user_filmorate")
                .usingGeneratedKeyColumns("id");
        Number id = simpleJdbcInsert.executeAndReturnKey(userToMap(user));

        return findUser((Integer) id).get();
    }

    @Override
    public User deleteUser(Integer id) {
        User user = findUser(id).get();
        jdbcTemplate.update("DELETE FROM user_filmorate WHERE id = ?", id);
        return user;
    }

    @Override
    public Optional<User> findUser(Integer id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM user_filmorate WHERE id = ?",
                new Object[]{id},
                this::mapRowToUser);
        return users.stream().findFirst();
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE user_filmorate " +
                "SET name = ?, login = ?, email = ?, birthday = ?" +
                "WHERE id = ?";

        jdbcTemplate.update(sqlQuery, user.getName(), user.getLogin(), user.getEmail(),
                user.getBirthday(), user.getId());

        return findUser(user.getId()).get();
    }

    @Override
    public List<User> getAllUser() {
        return jdbcTemplate.query("SELECT * FROM user_filmorate ORDER BY id", this::mapRowToUser);
    }

    @Override
    public List<User> findUserByCondition(Predicate<User> condition) {
        return null;
    }

    private Map<String, Object> userToMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("name", user.getName());
        values.put("login", user.getLogin());
        values.put("birthday", user.getBirthday());
        return values;
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
}
