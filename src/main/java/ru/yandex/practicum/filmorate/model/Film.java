package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
public class Film {
    private final int id;
   @NotNull
   @NotBlank
    private  String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;


}
