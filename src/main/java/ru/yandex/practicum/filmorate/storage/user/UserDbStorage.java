package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Repository("userDbStorage")
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        log.info("Постукпил пользователь на добавление {} ", user);

        String sqlQuery = "INSERT INTO user_filmorate(name, login, email, birthday) " +
                "VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        log.info("У пользователя был айди равный = {}", user.getId());
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        log.info("fqlb нового пользователя  , будет равен = {}", id);

        User addedUser = findUser(id).get();

        log.info("Добавили его {}", addedUser);

        return addedUser;
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
