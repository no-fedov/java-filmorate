package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;


import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    @Qualifier("userDbStorage")
    private final UserStorage userDbStorage;

    private final FriendshipStorage friendshipStorage;

    public User addUser(User user) {
        checkEmail(user);
        User userWithName = setUpName(user);
        User newUser = userDbStorage.addUser(userWithName);
        log.info("Добавлен новый пользователь: {}", newUser);
        return newUser;
    }

    public User findUser(Integer id) {
        User user = userDbStorage.findUser(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
        log.info("Обработан запрос по поиску пользователя. Найден пользователь: {}", user);
        return user;
    }

    public User updateUser(User user) {
        User oldUser = findUser(user.getId());

        if (!oldUser.getEmail().equals(user.getEmail())) {
            checkEmail(user);
        }

        log.info("Обновлен пользователь: {}", user);
        return userDbStorage.updateUser(user);
    }

    public User deleteUser(Integer id) {
        User deletedUser = findUser(id);
        log.info("Пользователь удален {}", deletedUser);
        return userDbStorage.deleteUser(id);
    }

    public void manageFriendship(Integer id1, Integer id2, FriendshipMethod nameMethod) {
        User user1 = findUser(id1);
        User user2 = findUser(id2);

        switch (nameMethod) {
            case ADD:
                friendshipStorage.addFriend(id1, id2);
                log.info("Пользователь № {} отправил заявку в друзья пользователю № {}", id1, id2);
                break;
            case DEL:
                friendshipStorage.deleteFriend(id1, id2);
                log.info("Эти пользователи теперь не друзья: {} и {}", user1, user2);
                break;
        }
    }

    public List<User> getUserFriends(Integer id) {
        findUser(id);
        List<User> friendList = friendshipStorage.getUserFriends(id);
        log.info("Обработан запрос на получения списка друзей пользователя с id = {}. Список друзей: {}",
                id, friendList);
        return friendList;
    }

    public List<User> getMutualFriends(Integer id1, Integer id2) {
        findUser(id1);
        findUser(id2);

        List<User> mutualFriends = friendshipStorage.getMutualFriendsList(id1, id2);
        log.info("Обработан запрос на получение общих друзей пользователя № {} и № {}", id1, id2);

        return mutualFriends;
    }

    public List<User> getAllUser() {
        List<User> listUser = userDbStorage.getAllUser();
        log.info("Обработан запрос на получение всех пользователей {}", listUser);
        return listUser;
    }

    private User setUpName(User user) {
        if (user.getName() == null || user.getName().trim().isBlank()) {
            return user.toBuilder().name(user.getLogin()).build();
        }
        return user;
    }

    private void checkEmail(User user) {
        boolean condition = userDbStorage.getAllUser().stream()
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
