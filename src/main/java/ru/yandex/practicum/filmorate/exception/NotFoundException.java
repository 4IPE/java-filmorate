package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Object object, int id) {
        super(object.toString().substring(42) + "с индефикатором " + id + " не найден");

    }
}
