package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

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
        log.info(text, "получить всех пользователей", usersData);
        return new ArrayList<>(usersData.values());
    }

    @PostMapping
    public User postUser(@RequestBody User user) {
        try {
            log.info(text, "Добавить пользователя", user);
            if (UserValidator.verify(user)) {
                if (usersData.containsKey(user.getEmail())) {
                    log.warn("Такой email занят: {}", user.getEmail());
                    throw new ValidationException("Такой email занят");
                }

                user.setId(++generatorID);

                if (user.getName() == null || user.getName().trim().isEmpty()) {
                    user.setName(user.getLogin());
                }

                usersData.put(generatorID, user);
            }
        } catch (ValidationException e) {
            log.info(text, "Добивить пользователя: " + user + ". Ошибка: ", e.getMessage());
            throw e;
        }
        log.info("Добавили юзера: " + user);
        return user;
    }

    @PutMapping
    public User putUser(@RequestBody User user) {
        if (usersData.containsKey(user.getId()))
            try {
                if (UserValidator.verify(user)) {
                    log.info(text, "Обновить пользователя", user);
                    usersData.put(user.getId(), user);
                }
            } catch (ValidationException e) {
                log.info(text, "Обновить пользователя " + user + ". Ошибка: ", e.getMessage());
                throw e;
            }
        else {
            log.warn(text, "Нельзя обновить пользователя с несуществующим id. ", user);
            throw new ValidationException("Нельзя обновить несуществующего пользователя");
        }
        return user;
    }
}