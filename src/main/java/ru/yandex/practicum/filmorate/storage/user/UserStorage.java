package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface UserStorage {
    User addUser(User user);

    User deleteUser(Integer id);

    Optional<User> findUser(Integer id);

    User updateUser(User user);

    List<User> getAllUser();

    List<User> findUserByCondition(Predicate<User> condition);
}
