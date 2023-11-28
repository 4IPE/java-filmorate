package ru.yandex.practicum.filmorate.exception;

public class ExceedingDate extends RuntimeException {
    public ExceedingDate(String message) {
        super(message);
    }
}
