package ru.yandex.practicum.filmorate.integration.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
