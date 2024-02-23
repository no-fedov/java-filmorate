package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Repository
class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> userStorage = new HashMap<>();

    @Override
    public User addUser(User user) {
        userStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public User deleteUser(Integer id) {
        return userStorage.remove(id);
    }

    @Override
    public User findUser(Integer id) {
        return userStorage.get(id);
    }

    @Override
    public List<User> getAllUser() {
        return new ArrayList<>(userStorage.values());
    }

    @Override
    public List<User> findUserByCondition(Predicate<User> condition) {
        List<User> users = new ArrayList<>();
        for (User user : userStorage.values()) {
            if (condition.test(user)) {
                users.add(user);
            }
        }
        return users;
    }
}
