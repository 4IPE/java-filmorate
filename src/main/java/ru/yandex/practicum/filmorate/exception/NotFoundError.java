package ru.yandex.practicum.filmorate.exception;

public class NotFoundError extends RuntimeException {
    public NotFoundError(Object object, int id) {
        super(object.toString().substring(42) + "с индефикатором " + id + " не найден");

    }
}
