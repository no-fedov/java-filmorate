package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User addUser(User user) {
        checkEmail(user);
        User userWithName = setUpName(user);
        User newUser = userStorage.addUser(userWithName);
        log.info("Добавлен новый пользователь: {}", newUser);
        return newUser;
    }

    public User findUser(Integer id) {
        User user = userStorage.findUser(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + "не найден"));
        log.info("Обработан запрос по поиску пользователя. Найден пользователь: {}", user);
        return user;
    }

    public User updateUser(User user) {
        User oldUser = findUser(user.getId());

        if (!oldUser.getEmail().equals(user.getEmail())) {
            checkEmail(user);
        }

        log.info("Обновлен пользователь: {}", user);
        return userStorage.updateUser(user);
    }

    public User deleteUser(Integer id) {
        User deletedUser = findUser(id);
        log.info("Пользователь удален {}", deletedUser);
        return userStorage.deleteUser(id);
    }

    public void manageFriendship(Integer id1, Integer id2, FriendshipMethod nameMethod) {
        User user1 = findUser(id1);
        User user2 = findUser(id2);

        switch (nameMethod) {
            case ADD:
                user1.getFriends().add(id2);
                user2.getFriends().add(id1);
                log.info("Эти пользователи теперь друзья: {} и {}", user1, user2);
                break;
            case DEL:
                user1.getFriends().remove(id2);
                user2.getFriends().remove(id1);
                log.info("Эти пользователи теперь не друзья: {} и {}", user1, user2);
                break;
        }
    }

    public List<User> getUserFriends(Integer id) {
        User user = findUser(id);
        List<User> friendsOfUser = user.getFriends().stream()
                .map(userID -> findUser(userID))
                .collect(Collectors.toList());
        log.info("Обработан запрос на получения списка друзей пользователя с id = {}. Список друзей: {}",
                id, friendsOfUser);
        return friendsOfUser;
    }

    public List<User> getMutualFriends(Integer id1, Integer id2) {
        Set<Integer> friendsUser1 = findUser(id1).getFriends();
        Set<Integer> friendsUser2 = findUser(id2).getFriends();

        Set<Integer> intersection = new HashSet<>(friendsUser1);
        intersection.retainAll(friendsUser2);

        List<User> mutualFriends = intersection.stream()
                .map(userID -> findUser(userID))
                .collect(Collectors.toList());

        log.info("Обработан запрос на получение общих друзей пользователя № {} и № {}", id1, id2);
        return mutualFriends;
    }

    public List<User> getAllUser() {
        return userStorage.getAllUser();
    }

    private User setUpName(User user) {
        if (user.getName() == null || user.getName().trim().isBlank()) {
            return user.toBuilder().name(user.getLogin()).build();
        }
        return user;
    }

    private void checkEmail(User user) {
        boolean condition = userStorage.getAllUser().stream()
                .anyMatch(a -> a.getEmail().equals(user.getEmail()));

        if (condition) {
            log.warn("Ошибка при выполнении запроса. Такой email занят: {}", user.getEmail());
            throw new ValidationException("Этот Email занят: " + user.getEmail());
        }
    }

    public enum FriendshipMethod {
        ADD, DEL
    }
}
