package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;


@Data
public class User {
    private int id;
    @Email
    private String email;
    private String name;
    @NotBlank(message = "Логин не может быть путсым")
    private final String login;
    @PastOrPresent(message = "День рождения не может быть в будущем")
    private final LocalDate birthday;


}
