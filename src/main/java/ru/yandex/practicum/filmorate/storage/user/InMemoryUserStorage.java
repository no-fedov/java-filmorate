package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.function.Predicate;

@Repository("inMemoryUserStorage")
class InMemoryUserStorage implements UserStorage {
    private int generatorID = 0;
    private final Map<Integer, User> userStorage = new HashMap<>();

    @Override
    public User addUser(User user) {
        User newUser = user.toBuilder().id(generateID()).build();
        userStorage.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User deleteUser(Integer id) {
        return userStorage.remove(id);
    }

    @Override
    public Optional<User> findUser(Integer id) {
        return Optional.ofNullable(userStorage.get(id));
    }

    @Override
    public User updateUser(User user) {
        userStorage.put(user.getId(), user);
        return user;
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

    private int generateID() {
        return ++generatorID;
    }
}
