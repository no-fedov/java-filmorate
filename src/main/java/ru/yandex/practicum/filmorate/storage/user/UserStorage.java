package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.function.Predicate;

public interface UserStorage {
    User addUser(User user);

    User deleteUser(Integer id);

    User findUser(Integer id);

    List<User> getAllUser();

    List<User> findUserByCondition(Predicate<User> condition);
}
