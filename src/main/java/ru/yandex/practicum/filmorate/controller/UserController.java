package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> usersData = new HashMap<>();
    private final String text = "Получен запрос: {} {}";
    private int generatorID = 0;

    @GetMapping
    public List<User> getUsers() {
        log.info(text, "Получить всех пользователей", usersData);
        return new ArrayList<>(usersData.values());
    }

    @PostMapping
    public User postUser(@Valid @RequestBody User user) {
        log.info(text, "Добавить пользователя", user);
        checkEmail(user);
        User user1 = user.toBuilder().id(++generatorID).build();
        User changedUser = setUpName(user1);
        usersData.put(generatorID, changedUser);
        log.info("Пользователь добавлен: {}", user1);
        return changedUser;
    }

    @PutMapping
    public User putUser(@Valid @RequestBody User user) {
        log.info(text, "Обновить пользователя", user);
        if (usersData.containsKey(user.getId())) {
            if (usersData.get(user.getId()).getEmail().equals(user.getEmail())) {
                usersData.put(user.getId(), user);
            } else {
                checkEmail(user);
                usersData.put(user.getId(), user);
            }
            log.info("Пользователь обновлен: {}", user);
        } else {
            log.warn(text, "Нельзя обновить пользователя с несуществующим id. ", user);
            throw new ValidationException("Нельзя обновить несуществующего пользователя");
        }
        return user;
    }

    private User setUpName(User user) {
        if (user.getName() == null || user.getName().trim().isBlank()) {
            return user.toBuilder().name(user.getLogin()).build();
        }
        return user;
    }

    private void checkEmail(User user) {
        boolean condition = usersData.values().stream()
                .anyMatch(a -> a.getEmail().equals(user.getEmail()));

        if (condition) {
            log.warn("Такой email занят: {}", user.getEmail());
            throw new ValidationException("Такой email занят");
        }
    }
}