package ru.yandex.practicum.filmorate.model;


import lombok.*;
import ru.yandex.practicum.filmorate.annotation.NameNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@NameNull
public class User {
    private int id;
    @Email(message = "Некорректный email")
    private String email;
    private String name;
    @NotBlank(message = "Логин не может быть путсым")
    private final String login;
    @PastOrPresent(message = "День рождения не может быть в будущем")
    private final LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();
    private boolean status = false;
}
