package ru.yandex.practicum.filmorate.exception;

public class NotExistEntity extends RuntimeException {
    public NotExistEntity(String message) {
        super(message);
    }
}
