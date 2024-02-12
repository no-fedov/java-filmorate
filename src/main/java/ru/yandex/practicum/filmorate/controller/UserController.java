package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<User> postUser(@RequestBody User user) {
        log.info(text, "Добавить пользователя", user);

        UserValidator.verify(user);
        checkEmail(user);


        User user1 = user.toBuilder().id(++generatorID).build();
        User changedUser = setUpName(user1);
        usersData.put(generatorID, changedUser);

        log.info("Пользователь добавлен: " + user1);

        return new ResponseEntity<>(changedUser, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> putUser(@RequestBody User user) {
        log.info(text, "Обновить пользователя", user);

        UserValidator.verify(user);

        if (usersData.containsKey(user.getId())) {
            usersData.put(user.getId(), user);
        } else {
            log.warn(text, "Нельзя обновить пользователя с несуществующим id. ", user);
            throw new ValidationException("Нельзя обновить несуществующего пользователя");
        }
        return new ResponseEntity<>(user,HttpStatus.OK);
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