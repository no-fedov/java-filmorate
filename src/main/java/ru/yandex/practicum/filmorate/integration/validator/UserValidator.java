package ru.yandex.practicum.filmorate.integration.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.integration.exception.ValidationException;
import ru.yandex.practicum.filmorate.integration.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidator {
    private UserValidator() {
    }

    public static void verify(User user) {
        String email = user.getEmail();

        if (email == null || !email.contains("@") || email.trim().isEmpty()) {
            log.warn("Ошибка: Неверный формат email");
            throw new ValidationException("Неверный формат email");
        }

        String login = user.getLogin();

        if (login == null || login.trim().isEmpty()) {
            log.warn("Ошибка: Логин не может быть пустым");
            throw new ValidationException("Логин не может быть пустым");
        }

        if (login.split(" ").length > 1) {
            log.warn("Ошибка: Логин не может содержать пробелов");
            throw new ValidationException("Логин не может содержать пробелов");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка: День рождения не может быть в будущем");
            throw new ValidationException("День рождения не может быть в будущем");
        }
    }
}
