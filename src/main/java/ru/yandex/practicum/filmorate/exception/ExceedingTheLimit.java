package ru.yandex.practicum.filmorate.exception;

public class ExceedingTheLimit extends RuntimeException{
    public ExceedingTheLimit(String message){
        super(message);
    }
}
