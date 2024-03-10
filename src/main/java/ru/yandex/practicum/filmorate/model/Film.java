package ru.yandex.practicum.filmorate.model;


import lombok.*;
import ru.yandex.practicum.filmorate.annotation.DateCheck;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
public class Film {
    private int id;
    @NotBlank(message = "Название не может быть пустым ")
    private String name;
    @Size(max = 200, message = "Описание должно быть не больше 200 слов")
    private final String description;
    @DateCheck
    private final LocalDate releaseDate;
    @Min(value = 1, message = "Продолжительность должна быть не меньше 1")
    private final int duration;
    private Set<Integer> likesOfUsers = new HashSet<>();
    private int like = 0;
    private Set<Genre> genres = new HashSet<>();
    private Mpa mpa;


}
