package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.yandex.practicum.filmorate.annotation.DateCheck;

import java.time.LocalDate;

@Data
public class Film {
    private int id;
    @NotBlank(message = "Сообщение не может быть пустым ")
    private String name;
    @Size(max = 200, message = "Описание должно быть не больше 200 слов")
    private final String description;
    @DateCheck
    private final LocalDate releaseDate;
    @PositiveOrZero(message = "Продолжительность должна быть положительной")
    private final int duration;


}
