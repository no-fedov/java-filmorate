package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator {
    private UserValidator() {
    }

    public static boolean verify(User user) {
        String email = user.getEmail();

        if (email == null || !email.contains("@") || email.trim().isEmpty()) {
            throw new ValidationException("Неверный формат email");
        }

        String login = user.getLogin();

        if (login == null || login.trim().isEmpty()) {
            throw new ValidationException("Логин не может быть пустым");
        }

        if (login.split(" ").length > 1) {
            throw new ValidationException("Логин не может содержать пробелов");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("День рождения не может быть в будущем");
        }

        return true;
    }
}
